package OMNM.OMNMBACKEND.user;

import OMNM.OMNMBACKEND.s3Image.AwsS3Service;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.dto.LoginDto;
import OMNM.OMNMBACKEND.user.dto.UserDto;
import OMNM.OMNMBACKEND.user.service.UserService;
import OMNM.OMNMBACKEND.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AwsS3Service awsS3Service;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/join")
    public ResponseEntity<String> userJoin(UserDto userDto, MultipartFile multipartFile){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String profileUrl = awsS3Service.uploadFile(multipartFile);

        User user = new User();
        user.setLoginId(userDto.getLoginId());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setSchool(userDto.getSchool());
        user.setGender(userDto.getGender());
        user.setProfileUrl(profileUrl);

        userService.saveUser(user);
        return new ResponseEntity<>("회원가입 완료", HttpStatus.OK);
    }

    @PostMapping("/join/idDuplicateCheck")
    public ResponseEntity<String> idDuplicateCheck(String loginId){
        if (userService.idDuplicateCheckService(loginId)){
            return new ResponseEntity<>("가입가능한 아이디", HttpStatus.OK);
        }
        return new ResponseEntity<>("중복 아이디", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(LoginDto loginDto){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(loginDto.getPassword());

        // null 값 반환 시 아이디 틀림
        if(userService.checkLoginId(loginDto.getLoginId()).isEmpty()){
            return new ResponseEntity<>("아이디 없음", HttpStatus.OK);
        }
        // DB에 아이디가 있는 경우
        else{
            // 해당 user의 비밀번호와 같으면
            if(bCryptPasswordEncoder.matches(loginDto.getPassword(), userService.checkLoginId(loginDto.getLoginId()).get().getPassword())){
                System.out.println(jwtTokenService.createJWT(loginDto.getLoginId()));
                return new ResponseEntity<String>(jwtTokenService.createJWT(loginDto.getLoginId()), HttpStatus.OK);
            }
            // 해당 user의 비밀번호와 맞지 않으면
            else{
                return new ResponseEntity<>("비밀번호 틀림", HttpStatus.OK);
            }
        }
    }
}