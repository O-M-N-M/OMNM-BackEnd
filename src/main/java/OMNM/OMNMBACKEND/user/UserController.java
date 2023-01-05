package OMNM.OMNMBACKEND.user;

import OMNM.OMNMBACKEND.findUser.service.EmailService;
import OMNM.OMNMBACKEND.s3Image.AwsS3Service;
import OMNM.OMNMBACKEND.token.domain.Token;
import OMNM.OMNMBACKEND.token.repository.TokenRepository;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.dto.LoginDto;
import OMNM.OMNMBACKEND.user.dto.TokenDto;
import OMNM.OMNMBACKEND.user.dto.UserDto;
import OMNM.OMNMBACKEND.user.service.UserService;
import OMNM.OMNMBACKEND.utils.JwtTokenService;
import OMNM.OMNMBACKEND.validation.service.ValidationService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Builder
public class UserController {

    private final UserService userService;
    private final AwsS3Service awsS3Service;
    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;
    private final ValidationService validationService;
    private final TokenRepository tokenRepository;

    /**
     * 아이디
     * 비밀번호
     * 비밀번호 일치
     * 이름
     * 학교
     * 이메일
     * 프로필 이미지 (필수x)
     * 성별
     * 기숙사 정보
     * 카카오톡 아이디
     * */

    @PostMapping(value = "/join",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
        public ResponseEntity<String> userJoin(@RequestPart(value = "key") UserDto userDto, @RequestPart(required = false, value = "file") MultipartFile multipartFile){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String profileUrl = null;
        if (multipartFile != null){
            profileUrl = awsS3Service.uploadFile(multipartFile);
        }

        User user = new User();
        user.setLoginId(userDto.getLoginId());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setSchool(userDto.getSchool());
        user.setGender(userDto.getGender());
        user.setProfileUrl(profileUrl);
        user.setKakaoId(userDto.getKakaoId());
        user.setDormitory(userDto.getDormitory());

        userService.saveUser(user);
        return new ResponseEntity<>("회원가입 완료", HttpStatus.OK);
    }

    @PostMapping("/join/emailValidation")
    public ResponseEntity<String> emailValidation(String email){
        Optional<User> user = userService.checkEmail(email);
        if(user.isPresent()){
            return new ResponseEntity<>("이미 존재하는 이메일입니다.", HttpStatus.OK);
        }
        else{
            emailService.sendingSettings(emailService.sendEmailValidation(email));
            return new ResponseEntity<>("인증번호 발송 성공", HttpStatus.OK);
        }
    }

    @PostMapping("/join/emailValidation/checkNumber")
    public ResponseEntity<String> checkValidationNumber(String email, int userValidationNumber){
        Integer validationNumber = validationService.getValidationNumber(email);
        if(validationNumber == null){
            return new ResponseEntity<>("인증번호가 일치하지 않습니다.", HttpStatus.OK);
        }
        else{
            if(validationNumber == userValidationNumber){
                return new ResponseEntity<>("인증번호가 일치합니다.", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("인증번호가 일치하지 않습니다.", HttpStatus.OK);
            }
        }
    }

    @PostMapping("/join/idDuplicateCheck")
    public ResponseEntity<String> idDuplicateCheck(String loginId){
        if (userService.idDuplicateCheckService(loginId)){
            return new ResponseEntity<>("가입가능한 아이디", HttpStatus.OK);
        }
        return new ResponseEntity<>("중복 아이디", HttpStatus.OK);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(LoginDto loginDto){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//         null 값 반환 시 아이디 틀림
        if(userService.checkLoginId(loginDto.getLoginId()).isEmpty()){
            return new ResponseEntity<>("아이디 없음", HttpStatus.OK);
        }
        // DB에 아이디가 있는 경우
        else{
            User user = userService.checkLoginId(loginDto.getLoginId()).get();
            // 해당 user의 비밀번호와 같으면
            if(bCryptPasswordEncoder.matches(loginDto.getPassword(), user.getPassword())){
                Token token = new Token();
                TokenDto tokenDto = new TokenDto();
                tokenDto.setAccessToken(jwtTokenService.createJWT(loginDto.getLoginId()));
                tokenDto.setRefreshToken(jwtTokenService.createRefreshToken());
                token.setAccessToken(tokenDto.getAccessToken());
                token.setRefreshToken(tokenDto.getRefreshToken());
                token.setLoginId(loginDto.getLoginId());
                tokenRepository.save(token);
                return new ResponseEntity<>(tokenDto, HttpStatus.OK);
            }
            // 해당 user의 비밀번호와 맞지 않으면
            else{
                return new ResponseEntity<>("비밀번호 틀림", HttpStatus.OK);
            }
        }
    }
}