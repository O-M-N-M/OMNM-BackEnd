package OMNM.OMNMBACKEND.findUser.service;

import OMNM.OMNMBACKEND.findUser.dto.EmailDto;
import OMNM.OMNMBACKEND.findUser.dto.FindLoginPwDto;
import OMNM.OMNMBACKEND.validation.domain.Validation;
import OMNM.OMNMBACKEND.validation.repository.ValidationRepository;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final FindService findService;
    private final ValidationRepository validationRepository;

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
        emailDto.setTitle("[OMNM] 임시 비밀번호 발송 이메일");
        emailDto.setContent("안녕하세요, OMNM 입니다. 임시 비밀번호는 다음과 같습니다." + System.lineSeparator() + System.lineSeparator() + tempPw + System.lineSeparator() + System.lineSeparator() + "감사합니다.");

        findService.findEmailService(findLoginPwDto.getEmail()).get().setPassword(bCryptPasswordEncoder.encode(tempPw));

        return emailDto;
    }

    public EmailDto sendEmailValidation(String email){
        EmailDto emailDto = new EmailDto();
        Random random = new Random();
        int validationNumber = random.nextInt(888888) + 111111; // 111111 ~ 999999 까지의 랜덤 인증번호 생성

        emailDto.setAddress(email);
        emailDto.setTitle("[OMNM] 회원가입 이메일 인증");
        emailDto.setContent("안녕하세요, OMNM 입니다." + System.lineSeparator()+ "회원가입 인증번호는 다음과 같습니다." + System.lineSeparator() + System.lineSeparator() + validationNumber + System.lineSeparator() + System.lineSeparator()+ "감사합니다.");

        // Validation DB에 인증번호 저장
        Optional<Validation> validation = validationRepository.findByEmail(email);
        Validation validationEntity;
        if(validation.isEmpty()){
            validationEntity = new Validation();
        }
        else{
            validationEntity = validation.get();
        }
        validationEntity.setValidationNumber(validationNumber);
        validationEntity.setEmail(email);
        validationRepository.save(validationEntity);

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
