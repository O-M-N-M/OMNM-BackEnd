package OMNM.OMNMBACKEND.myPage;

import OMNM.OMNMBACKEND.blackList.domain.BlackList;
import OMNM.OMNMBACKEND.blackList.repository.BlackListRepository;
import OMNM.OMNMBACKEND.blackList.service.BlackListService;
import OMNM.OMNMBACKEND.connection.domain.Connection;
import OMNM.OMNMBACKEND.connection.repository.ConnectionRepository;
import OMNM.OMNMBACKEND.myPage.dto.DeleteDto;
import OMNM.OMNMBACKEND.myPage.dto.ModifyDto;
import OMNM.OMNMBACKEND.myPage.dto.ViewUserDto;
import OMNM.OMNMBACKEND.myPage.service.MyPageService;
import OMNM.OMNMBACKEND.s3Image.AwsS3Service;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.dto.UserDto;
import OMNM.OMNMBACKEND.user.service.UserService;
import OMNM.OMNMBACKEND.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final ConnectionRepository connectionRepository;
    private final UserService userService;

    /**
     * 회원탈퇴
     * */
    @DeleteMapping("")
    public ResponseEntity<String> deleteAccount(@PathVariable Long userId, DeleteDto deleteDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        if (user.getLoginId().equals(deleteDto.getLoginId()) && user.getPassword().equals(deleteDto.getPassword())){
            myPageService.deleteUserAccount(userId);
            return new ResponseEntity<>("회원 탈퇴 완료", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("회원 정보가 올바르지 않습니다", HttpStatus.BAD_REQUEST);
        }

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
    @DeleteMapping("/connection/reverse/{matchingId}")
    public ResponseEntity<String> deleteConnectionReverse(@PathVariable Long matchingId, @PathVariable Long userId){
        /**
         * matchingId -> userId로 가는 connection 객체 지워야함
         * */
        Connection connection = myPageService.getConnectionEntity(matchingId, userId);
        myPageService.deleteConnection(connection);
        return new ResponseEntity<>("해당 매칭 신청이 삭제되었습니다.", HttpStatus.OK);
    }

    /**
     * 보낸 신청 삭제
     * */
    @DeleteMapping("/connection/{matchingId}")
    public ResponseEntity<String> deleteConnection(@PathVariable Long matchingId, @PathVariable Long userId){
        /**
         * matchingId -> userId로 가는 connection 객체 지워야함
         * */
        Connection connection = myPageService.getConnectionEntity(userId, matchingId);
        myPageService.deleteConnection(connection);
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

    /**
     * 개인정보(카카오톡 아이디, 기숙사) 변경
     * */
    @PatchMapping(value = "/modifyInfo", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<String> modifyInfo(@RequestPart(value = "key") ModifyDto modifyDto, @RequestPart(required = false, value = "file") MultipartFile multipartFile){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);
        String originalProfileUrl = user.getProfileUrl();

        String profileUrl = null;
        if (multipartFile != null){
            profileUrl = awsS3Service.uploadFile(multipartFile);
            user.setProfileUrl(profileUrl);
        }
        else{
            user.setProfileUrl(originalProfileUrl);
        }
        user.setDormitory(modifyDto.getDormitory());
        user.setKakaoId(modifyDto.getKakaoId());
        userService.saveUser(user);

        return new ResponseEntity<>("회원정보 수정 완료", HttpStatus.OK);
    }
}
