package OMNM.OMNMBACKEND.findUser;

import OMNM.OMNMBACKEND.findUser.dto.FindLoginIdDto;
import OMNM.OMNMBACKEND.findUser.dto.FindLoginPwDto;
import OMNM.OMNMBACKEND.findUser.service.EmailService;
import OMNM.OMNMBACKEND.findUser.service.FindService;
import OMNM.OMNMBACKEND.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/find")
@RequiredArgsConstructor
public class FindController {

    private final FindService findService;
    private final EmailService emailService;

    @PostMapping("/loginId")
    public ResponseEntity<String> findLoginId(FindLoginIdDto findLoginIdDto){
        Optional<User> user = findService.findIdService(findLoginIdDto.getName());
        if (user.isPresent()){
            if (user.get().getEmail().equals(findLoginIdDto.getEmail())){
                return new ResponseEntity<>(user.get().getLoginId(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/loginPw")
    public ResponseEntity<String> findLoginPw(FindLoginPwDto findLoginPwDto){
        Optional<User> userEmail = findService.findEmailService(findLoginPwDto.getEmail());
        if(userEmail.isEmpty()){
            return new ResponseEntity<>("존재하지 않는 이메일", HttpStatus.OK);
        }
        else{
            if(userEmail.get().getLoginId().equals(findLoginPwDto.getLoginId())){
                emailService.sendingSettings(emailService.sendEmail(findLoginPwDto));
                return new ResponseEntity<>("인증번호 발송 성공", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("이메일과 아이디가 일치하지 않음", HttpStatus.OK);
            }
        }
    }
}
