package OMNM.OMNMBACKEND.myPage;

/*
 * 프로필 사진 변경 (PATCH)
 * 비밀번호 재설정 (PATCH)
 * 로그아웃 -> With Spring Security
 * 탈퇴 (DELETE)
 * 내 정보 보기 (GET)
 * */

import OMNM.OMNMBACKEND.myPage.dto.MyPageUserDto;
import OMNM.OMNMBACKEND.myPage.service.MyPageService;
import OMNM.OMNMBACKEND.s3Image.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final AwsS3Service awsS3Service;

    @DeleteMapping("")
    public ResponseEntity<String> deleteAccount(){
        Long userId = 4L;
        myPageService.deleteUserAccount(userId);
        return new ResponseEntity<>("회원 탈퇴 완료", HttpStatus.OK);
    }

    @PatchMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(String password){
        Long userId = 4L;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        myPageService.resetUserPassword(userId, bCryptPasswordEncoder.encode(password));
        return new ResponseEntity<>("비밀번호 변경 완료", HttpStatus.OK);
    }

    @PatchMapping("/changeProfile")
    public ResponseEntity<String> changeProfilePicture(MultipartFile multipartFile){
        Long userId = 4L;
        String newProfileUrl = awsS3Service.uploadFile(multipartFile);
        myPageService.changeProfileUrl(userId, newProfileUrl);
        return new ResponseEntity<>("프로필 이미지 변경 완료", HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<MyPageUserDto> viewMyInformation(){
        Long userId = 4L;
        return new ResponseEntity<>(myPageService.viewUserDto(userId), HttpStatus.OK);
    }
}
