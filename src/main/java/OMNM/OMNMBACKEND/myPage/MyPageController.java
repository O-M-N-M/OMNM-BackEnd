package OMNM.OMNMBACKEND.myPage;

/*
 * 프로필 사진 변경 (PATCH)
 * 비밀번호 재설정 (PATCH)
 * 로그아웃 -> With Spring Security
 * 탈퇴 (DELETE)
 * 내 정보 보기 (GET)
 * */

import OMNM.OMNMBACKEND.myPage.dto.ModifyDto;
import OMNM.OMNMBACKEND.myPage.dto.MyPageUserDto;
import OMNM.OMNMBACKEND.myPage.dto.ViewUserDto;
import OMNM.OMNMBACKEND.myPage.service.MyPageService;
import OMNM.OMNMBACKEND.s3Image.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final AwsS3Service awsS3Service;

    @DeleteMapping("")
    public ResponseEntity<String> deleteAccount(){
        Long userId = 9L;
        myPageService.deleteUserAccount(userId);
        return new ResponseEntity<>("회원 탈퇴 완료", HttpStatus.OK);
    }

    @PatchMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(String password){
        Long userId = 9L;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        myPageService.resetUserPassword(userId, bCryptPasswordEncoder.encode(password));
        return new ResponseEntity<>("비밀번호 변경 완료", HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<ViewUserDto> viewMyInformation(){
        Long userId = 10L;
        return new ResponseEntity<>(myPageService.setViewUserDto(userId), HttpStatus.OK);
    }

    @PatchMapping("/matching")
    public ResponseEntity<String> getMatching(){
        Long userId = 9L;
        myPageService.changeMatchingStatus(userId);
        return new ResponseEntity<>("매칭 상태가 변경되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/connection")
    public List<List<String>> getConnection(){
        Long userId = 11L;
        return myPageService.getConnectionList(userId);
    }
}
