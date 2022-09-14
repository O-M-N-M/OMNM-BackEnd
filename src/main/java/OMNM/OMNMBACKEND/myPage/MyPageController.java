package OMNM.OMNMBACKEND.myPage;

import OMNM.OMNMBACKEND.myPage.dto.ViewUserDto;
import OMNM.OMNMBACKEND.myPage.service.MyPageService;
import OMNM.OMNMBACKEND.s3Image.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 매칭 API
 * 로그아웃
 * 회원탈퇴
 * 나의 정보 보여주기 & 수정
 * 신청리스트
 * 비밀번호 재설정
 * */

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final AwsS3Service awsS3Service;

    @DeleteMapping("")
    public ResponseEntity<String> deleteAccount(@PathVariable Long userId){
        myPageService.deleteUserAccount(userId);
        return new ResponseEntity<>("회원 탈퇴 완료", HttpStatus.OK);
    }

    @PatchMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@PathVariable Long userId, String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        myPageService.resetUserPassword(userId, bCryptPasswordEncoder.encode(password));
        return new ResponseEntity<>("비밀번호 변경 완료", HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<ViewUserDto> viewMyInformation(@PathVariable Long userId){
        return new ResponseEntity<>(myPageService.setViewUserDto(userId), HttpStatus.OK);
    }

    @PatchMapping("/matching")
    public ResponseEntity<String> getMatching(@PathVariable Long userId){
        myPageService.changeMatchingStatus(userId);
        return new ResponseEntity<>("매칭 상태가 변경되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/connection")
    public List<List<String>> getConnection(@PathVariable Long userId){
        return myPageService.getConnectionList(userId);
    }

    @DeleteMapping("/connection/{matchingId}")
    public ResponseEntity<String> deleteConnection(@PathVariable Long matchingId, @PathVariable Long userId){
        /**
         * matchingId -> userId로 가는 connection 객체 지워야함
         * */
        myPageService.deleteConnection(matchingId, userId);
        return new ResponseEntity<>("해당 매칭 신청이 삭제되었습니다.", HttpStatus.OK);
    }
}
