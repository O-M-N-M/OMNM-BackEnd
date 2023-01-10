package OMNM.OMNMBACKEND.myPage;

import OMNM.OMNMBACKEND.blackList.domain.BlackList;
import OMNM.OMNMBACKEND.blackList.repository.BlackListRepository;
import OMNM.OMNMBACKEND.connection.domain.Connection;
import OMNM.OMNMBACKEND.connection.repository.ConnectionRepository;
import OMNM.OMNMBACKEND.connection.repository.ViewConnectionRepository;
import OMNM.OMNMBACKEND.myPage.dto.*;
import OMNM.OMNMBACKEND.myPage.service.MyPageService;
import OMNM.OMNMBACKEND.myPersonality.domain.MyPersonality;
import OMNM.OMNMBACKEND.myPersonality.repository.MyPersonalityRepository;
import OMNM.OMNMBACKEND.myPersonality.service.MyPersonalityService;
import OMNM.OMNMBACKEND.s3Image.AwsS3Service;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.service.UserService;
import OMNM.OMNMBACKEND.utils.JwtTokenService;
import OMNM.OMNMBACKEND.yourPersonality.repository.YourPersonalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    private final MyPersonalityService myPersonalityService;
    private final ViewConnectionRepository viewConnectionRepository;
    private final MyPersonalityRepository myPersonalityRepository;
    private final YourPersonalityRepository yourPersonalityRepository;

    /**
     * 회원탈퇴
     * */
    @PostMapping("")
    public ResponseEntity<String> deleteAccount(@PathVariable Long userId, DeleteDto deleteDto){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        List<Connection> connectionsTo = connectionRepository.findAllByToId(userId);
        List<Connection> connectionsFrom = connectionRepository.findAllByFromId(userId);

        if (user.getLoginId().equals(deleteDto.getLoginId()) && bCryptPasswordEncoder.matches(deleteDto.getPassword(), user.getPassword())){
            myPageService.deleteUserAccount(userId);
            if(user.getMyPersonalityId() != null){
                myPersonalityRepository.deleteById(user.getMyPersonalityId());
            }
            if(user.getYourPersonalityId() != null){
                yourPersonalityRepository.deleteById(user.getYourPersonalityId());
            }
            if(connectionsTo != null){
                for (Connection connection : connectionsTo){
                    connectionRepository.deleteById(connection.getConnectionId());
                }
            }
            if(connectionsFrom != null){
                for (Connection connection : connectionsFrom){
                    connectionRepository.deleteById(connection.getConnectionId());
                }
            }
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
    public ResponseEntity<String> resetPassword(@PathVariable Long userId, PasswordDto passwordDto){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        if(bCryptPasswordEncoder.matches(passwordDto.getOriginalPassword(), user.getPassword())){
            myPageService.resetUserPassword(userId, bCryptPasswordEncoder.encode(passwordDto.getNewPassword()));
            return new ResponseEntity<>("비밀번호 변경 완료", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("회원정보가 올바르지 않습니다", HttpStatus.NOT_FOUND);
        }
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
        Integer status = myPageService.changeMatchingStatus(userId);
        if(status == 1){
            return new ResponseEntity<>("룸메이트 매칭 완료", HttpStatus.OK);
        }
        else if(status == 0){
            return new ResponseEntity<>("룸메이트 구하는 중", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("없는 유저", HttpStatus.OK);
        }
    }

    /**
     * 메인 - 신청 받은 리스트 2단
     * */
    @GetMapping("/connection")
    public HashMap<String, List<GetLatestConnectionsDto>> getConnection(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        UserDetails loginUser = userService.loadUserByUsername(username);
        User user = (User) loginUser;

        List<PagingViewUserDto> pagingViewUserDtoList = new ArrayList<>();
        List<Connection> connectionList = connectionRepository.findAllByToId(user.getUserId());

        Set<String> timeSet = new HashSet<>();
        HashMap<String, List<GetLatestConnectionsDto>> getLatestConnections = new HashMap<>();

        if (connectionList.size() == 0){
            return null;
        }
        else{
            for (Connection connection : connectionList) {
                PagingViewUserDto pagingViewUserDto = new PagingViewUserDto(); //프로필 사진, 이름, 나이, 시간
                User connectionUser = userService.getUserEntityById(connection.getFromId());
                MyPersonality connectionUserMy = myPersonalityService.findMyPersonality(connectionUser.getMyPersonalityId());
                pagingViewUserDto.setUserId(connectionUser.getUserId());
                pagingViewUserDto.setAge(connectionUserMy.getAge());
                pagingViewUserDto.setName(connectionUser.getName());
                pagingViewUserDto.setProfileUrl(connectionUser.getProfileUrl());
                pagingViewUserDto.setTime(connection.getCreatedTime());
                timeSet.add(connection.getCreatedTime());
                pagingViewUserDtoList.add(pagingViewUserDto);
            }
            List<String> timeList = new ArrayList<>(timeSet);
            timeList.sort(Comparator.reverseOrder());
            if(timeList.size()>1) {
                for (int i = 0; i < 2; i++) {
                    List<GetLatestConnectionsDto> temp = new ArrayList<>();
                    for (PagingViewUserDto pagingViewUserDto : pagingViewUserDtoList) {
                        if (Objects.equals(pagingViewUserDto.getTime(), timeList.get(i))) {
                            GetLatestConnectionsDto getLatestConnectionsDto = new GetLatestConnectionsDto();
                            getLatestConnectionsDto.setUserId(pagingViewUserDto.getUserId());
                            getLatestConnectionsDto.setAge(pagingViewUserDto.getAge());
                            getLatestConnectionsDto.setName(pagingViewUserDto.getName());
                            getLatestConnectionsDto.setProfileUrl(pagingViewUserDto.getProfileUrl());
                            temp.add(getLatestConnectionsDto);
                        }
                        if (temp.size() == 4) {
                            break;
                        }
                    }
                    getLatestConnections.put(timeList.get(i).substring(5), temp);
                }
            }
            else{
                List<GetLatestConnectionsDto> temp = new ArrayList<>();
                for (PagingViewUserDto pagingViewUserDto : pagingViewUserDtoList) {
                    if (Objects.equals(pagingViewUserDto.getTime(), timeList.get(0))) {
                        GetLatestConnectionsDto getLatestConnectionsDto = new GetLatestConnectionsDto();
                        getLatestConnectionsDto.setUserId(pagingViewUserDto.getUserId());
                        getLatestConnectionsDto.setAge(pagingViewUserDto.getAge());
                        getLatestConnectionsDto.setName(pagingViewUserDto.getName());
                        getLatestConnectionsDto.setProfileUrl(pagingViewUserDto.getProfileUrl());
                        temp.add(getLatestConnectionsDto);
                    }
                    if (temp.size() == 4) {
                        break;
                    }
                }
                getLatestConnections.put(timeList.get(0).substring(5), temp);
            }
        }
        return getLatestConnections;
    }

    /**
     * 메인 - 신청 보낸 리스트 2단
     * */
    @GetMapping("/propose")
    public HashMap<String, List<GetLatestConnectionsDto>> getPropose(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        UserDetails loginUser = userService.loadUserByUsername(username);
        User user = (User) loginUser;

        List<PagingViewUserDto> pagingViewUserDtoList = new ArrayList<>();
        List<Connection> connectionList = connectionRepository.findAllByFromId(user.getUserId());

        Set<String> timeSet = new HashSet<>();
        HashMap<String, List<GetLatestConnectionsDto>> getLatestConnections = new HashMap<>();

        if (connectionList.size() == 0){
            return null;
        }
        else{
            for (Connection connection : connectionList) {
                PagingViewUserDto pagingViewUserDto = new PagingViewUserDto(); //프로필 사진, 이름, 나이, 시간
                User connectionUser = userService.getUserEntityById(connection.getToId());
                MyPersonality connectionUserMy = myPersonalityService.findMyPersonality(connectionUser.getMyPersonalityId());
                pagingViewUserDto.setUserId(connectionUser.getUserId());
                pagingViewUserDto.setAge(connectionUserMy.getAge());
                pagingViewUserDto.setName(connectionUser.getName());
                pagingViewUserDto.setProfileUrl(connectionUser.getProfileUrl());
                pagingViewUserDto.setTime(connection.getCreatedTime());
                timeSet.add(connection.getCreatedTime());
                pagingViewUserDtoList.add(pagingViewUserDto);
            }
            List<String> timeList = new ArrayList<>(timeSet);
            timeList.sort(Comparator.reverseOrder());
            if(timeList.size()>1) {
                for (int i = 0; i < 2; i++) {
                    List<GetLatestConnectionsDto> temp = new ArrayList<>();
                    for (PagingViewUserDto pagingViewUserDto : pagingViewUserDtoList) {
                        if (Objects.equals(pagingViewUserDto.getTime(), timeList.get(i))) {
                            GetLatestConnectionsDto getLatestConnectionsDto = new GetLatestConnectionsDto();
                            getLatestConnectionsDto.setUserId(pagingViewUserDto.getUserId());
                            getLatestConnectionsDto.setAge(pagingViewUserDto.getAge());
                            getLatestConnectionsDto.setName(pagingViewUserDto.getName());
                            getLatestConnectionsDto.setProfileUrl(pagingViewUserDto.getProfileUrl());
                            temp.add(getLatestConnectionsDto);
                        }
                        if (temp.size() == 4) {
                            break;
                        }
                    }
                    getLatestConnections.put(timeList.get(i).substring(5), temp);
                }
            }
            else {
                List<GetLatestConnectionsDto> temp = new ArrayList<>();
                for (PagingViewUserDto pagingViewUserDto : pagingViewUserDtoList) {
                    if (Objects.equals(pagingViewUserDto.getTime(), timeList.get(0))) {
                        GetLatestConnectionsDto getLatestConnectionsDto = new GetLatestConnectionsDto();
                        getLatestConnectionsDto.setUserId(pagingViewUserDto.getUserId());
                        getLatestConnectionsDto.setAge(pagingViewUserDto.getAge());
                        getLatestConnectionsDto.setName(pagingViewUserDto.getName());
                        getLatestConnectionsDto.setProfileUrl(pagingViewUserDto.getProfileUrl());
                        temp.add(getLatestConnectionsDto);
                    }
                    if (temp.size() == 4) {
                        break;
                    }
                }
                getLatestConnections.put(timeList.get(0).substring(5), temp);
            }
        }
        return getLatestConnections;
    }

    /**
     * 신청 받은 리스트 수
     * */
    @GetMapping("/connection/count")
    public Integer getConnectionCount(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        UserDetails loginUser = userService.loadUserByUsername(username);
        User user = (User) loginUser;

        List<Connection> connectionList = connectionRepository.findAllByToId(user.getUserId());
        return connectionList.size();
    }

    /**
     * 신청 보낸 리스트 수
     * */
    @GetMapping("/propose/count")
    public Integer getProposeCount(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        UserDetails loginUser = userService.loadUserByUsername(username);
        User user = (User) loginUser;

        List<Connection> connectionList = connectionRepository.findAllByFromId(user.getUserId());
        return connectionList.size();
    }

    /**
     * 더보기 - 신청 받은 리스트
     * */
    @GetMapping("/connection/detail")
    public List<ViewConnectionUserDto> getConnectionDetails(Pageable pageable){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        UserDetails loginUser = userService.loadUserByUsername(username);
        User user = (User) loginUser;

        List<ViewConnectionUserDto> allPagingViewUserList = new ArrayList<>();

        Page<Connection> connections = viewConnectionRepository.findAllByToIdOrderByConnectionIdDesc(user.getUserId(), pageable);

        List<Connection> content = connections.getContent();
        boolean isLast = connections.isLast();

        ViewConnectionUserDto viewConnectionUserDto = new ViewConnectionUserDto();

        if (content.size() == 0){
            return null;
        }
        else{
            for (Connection connection : content){
                User connectionUser = userService.getUserEntityById(connection.getFromId());
                MyPersonality connectionUserMy = myPersonalityService.findMyPersonality(connectionUser.getMyPersonalityId());

                viewConnectionUserDto.setUserId(connectionUser.getUserId());
                viewConnectionUserDto.setProfileUrl(connectionUser.getProfileUrl());
                viewConnectionUserDto.setName(connectionUser.getName());
                viewConnectionUserDto.setAge(connectionUserMy.getAge());
                viewConnectionUserDto.setTime(connection.getCreatedTime());
                viewConnectionUserDto.setMbti(connectionUserMy.getMbti());
                viewConnectionUserDto.setLifeCycle(connectionUserMy.getLifeCycle());
                viewConnectionUserDto.setLast(isLast);

                allPagingViewUserList.add(viewConnectionUserDto);
                viewConnectionUserDto = new ViewConnectionUserDto();
            }
        }
        return allPagingViewUserList;
    }

    /**
     * 더보기 - 신청 보낸 리스트
     * */
    @GetMapping("/propose/detail")
    public List<ViewConnectionUserDto> getProposeDetails(Pageable pageable){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        UserDetails loginUser = userService.loadUserByUsername(username);
        User user = (User) loginUser;

        List<ViewConnectionUserDto> allPagingViewUserList = new ArrayList<>();

        Page<Connection> connections = viewConnectionRepository.findAllByFromIdOrderByConnectionIdDesc(user.getUserId(), pageable);

        List<Connection> content = connections.getContent();
        boolean isLast = connections.isLast();

        ViewConnectionUserDto viewConnectionUserDto = new ViewConnectionUserDto();

        if (content.size() == 0){
            return null;
        }
        else{
            for (Connection connection : content){
                User connectionUser = userService.getUserEntityById(connection.getToId());
                MyPersonality connectionUserMy = myPersonalityService.findMyPersonality(connectionUser.getMyPersonalityId());

                viewConnectionUserDto.setUserId(connectionUser.getUserId());
                viewConnectionUserDto.setProfileUrl(connectionUser.getProfileUrl());
                viewConnectionUserDto.setName(connectionUser.getName());
                viewConnectionUserDto.setAge(connectionUserMy.getAge());
                viewConnectionUserDto.setTime(connection.getCreatedTime());
                viewConnectionUserDto.setMbti(connectionUserMy.getMbti());
                viewConnectionUserDto.setLifeCycle(connectionUserMy.getLifeCycle());
                viewConnectionUserDto.setLast(isLast);

                allPagingViewUserList.add(viewConnectionUserDto);
                viewConnectionUserDto = new ViewConnectionUserDto();
            }
        }
        return allPagingViewUserList;
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
