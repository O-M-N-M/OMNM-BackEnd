package OMNM.OMNMBACKEND.myPage;

import OMNM.OMNMBACKEND.blackList.domain.BlackList;
import OMNM.OMNMBACKEND.blackList.repository.BlackListRepository;
import OMNM.OMNMBACKEND.blackList.service.BlackListService;
import OMNM.OMNMBACKEND.myPage.dto.ViewUserDto;
import OMNM.OMNMBACKEND.myPage.service.MyPageService;
import OMNM.OMNMBACKEND.s3Image.AwsS3Service;
import OMNM.OMNMBACKEND.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

/**
 * 회원탈퇴
 * 비밀번호 재설정
 * 프로필 보기 상세 정보
 * 매칭 완료
 * 신청 받은 리스트
 * 신청 보낸 리스트
 * 신청 받은 리스트 수
 * 신청 보낸 리스트 수
 * 받은 신청 삭제
 * 보낸 신청 삭제
 * 로그아웃
 * */

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final AwsS3Service awsS3Service;
    private final JwtTokenService jwtTokenService;
    private final BlackListRepository blackListRepository;

    /**
     * 회원탈퇴
     * */
    @PatchMapping("")
    public ResponseEntity<String> deleteAccount(@PathVariable Long userId){
        myPageService.deleteUserAccount(userId);
        return new ResponseEntity<>("회원 탈퇴 완료", HttpStatus.OK);
    }

    /**
     * 비밀번호 재설정
     * */
    @PatchMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@PathVariable Long userId, String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        myPageService.resetUserPassword(userId, bCryptPasswordEncoder.encode(password));
        return new ResponseEntity<>("비밀번호 변경 완료", HttpStatus.OK);
    }

    /**
     * 프로필 보기 상세 정보
     * */
    @GetMapping("")
    public ResponseEntity<ViewUserDto> viewMyInformation(@PathVariable Long userId){
        return new ResponseEntity<>(myPageService.setViewUserDto(userId), HttpStatus.OK);
    }

    /**
     * 매칭 완료
     * */
    @PatchMapping("/matching")
    public ResponseEntity<String> getMatching(@PathVariable Long userId){
        myPageService.changeMatchingStatus(userId);
        return new ResponseEntity<>("매칭 상태가 변경되었습니다.", HttpStatus.OK);
    }

    /**
     * 신청 받은 리스트
     * */
    @GetMapping("/connection")
    public List<List<String>> getConnection(@PathVariable Long userId){
        return myPageService.getConnectionList(userId);
    }

    /**
     * 신청 보낸 리스트
     * */
    @GetMapping("/propose")
    public List<List<String>> getProposeList(@PathVariable Long userId){
        return myPageService.getProposeList(userId);
    }

    /**
     * 신청 받은 리스트 수
     * */
    @GetMapping("/connection/count")
    public Integer getConnectionCount(@PathVariable Long userId){
        List<List<String>> connectionList = myPageService.getConnectionList(userId);
        return connectionList.size();
    }

    /**
     * 신청 보낸 리스트 수
     * */
    @GetMapping("/propose/count")
    public Integer getProposeCount(@PathVariable Long userId){
        List<List<String>> connectionList = myPageService.getProposeList(userId);
        return connectionList.size();
    }

    /**
     * 받은 신청 삭제
     * */

    /**
     * 보낸 신청 삭제
     * */
    @DeleteMapping("/connection/{matchingId}")
    public ResponseEntity<String> deleteConnection(@PathVariable Long matchingId, @PathVariable Long userId){
        /**
         * matchingId -> userId로 가는 connection 객체 지워야함
         * */
        myPageService.deleteConnection(matchingId, userId);
        return new ResponseEntity<>("해당 매칭 신청이 삭제되었습니다.", HttpStatus.OK);
    }

    /**
     * 로그아웃
     * */
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        String jwtToken = jwtTokenService.resolveToken(request);
        BlackList blackList = new BlackList();
        blackList.setToken(jwtToken);
        blackListRepository.save(blackList);
        return new ResponseEntity<>("로그아웃 되었습니다.", HttpStatus.OK);
    }
}
