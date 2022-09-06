package OMNM.OMNMBACKEND.findUser.service;

import OMNM.OMNMBACKEND.findUser.dto.EmailDto;
import OMNM.OMNMBACKEND.findUser.dto.FindLoginPwDto;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final FindService findService;

    public StringBuilder setTempKey(){
        StringBuilder tempCode = new StringBuilder();
        char[] set = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        for(int i=0; i<15; i++){
            tempCode.append(set[(int) (Math.random() * 36)]);
        }

        return tempCode;
    }

    public EmailDto sendEmail(FindLoginPwDto findLoginPwDto){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        EmailDto emailDto = new EmailDto();

        String tempPw = String.valueOf(setTempKey());

        emailDto.setAddress(findLoginPwDto.getEmail());
        emailDto.setTitle("[OMNM] 임시 비밀번호 코드 이메일입니다.");
        emailDto.setContent("안녕하세요, OMNM 입니다. 임시 비밀번호는 다음과 같습니다." + System.lineSeparator() + tempPw + System.lineSeparator() + "감사합니다.");

        findService.findEmailService(findLoginPwDto.getEmail()).get().setPassword(bCryptPasswordEncoder.encode(tempPw));

        return emailDto;
    }

    public EmailDto sendEmailValidation(String email){
        EmailDto emailDto = new EmailDto();
        Random random = new Random();
        int validationNumber = random.nextInt(888888) + 111111; // 111111 ~ 999999 까지의 랜덤 인증번호 생성

        emailDto.setAddress(email);
        emailDto.setTitle("[OMNM] 이메일 인증 관련 이메일입니다.");
        emailDto.setContent("안녕하세요, OMNM 입니다. 이메일 인증 관련 인증번호는 다음과 같습니다." + System.lineSeparator() + validationNumber + System.lineSeparator() + "감사합니다.");

        // Redis Database에 인증번호 저장 과정 보류

        return emailDto;
    }

    public void sendingSettings(@NotNull EmailDto emailDto){
        SimpleMailMessage messageInfo = new SimpleMailMessage();
        messageInfo.setTo(emailDto.getAddress());
        messageInfo.setSubject(emailDto.getTitle());
        messageInfo.setText(emailDto.getContent());

        javaMailSender.send(messageInfo);
    }
}
