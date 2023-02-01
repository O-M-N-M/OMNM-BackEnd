package OMNM.OMNMBACKEND.main;

import OMNM.OMNMBACKEND.main.dto.AllRecommendResponseDto;
import OMNM.OMNMBACKEND.main.dto.ConnectionDto;
import OMNM.OMNMBACKEND.main.dto.DetailRecommendResponseDto;
import OMNM.OMNMBACKEND.main.service.MainService;
import OMNM.OMNMBACKEND.myPersonality.domain.MyPersonality;
import OMNM.OMNMBACKEND.myPersonality.repository.MyPersonalityRepository;
import OMNM.OMNMBACKEND.myPersonality.service.MyPersonalityService;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.service.UserService;
import OMNM.OMNMBACKEND.yourPersonality.domain.YourPersonality;
import OMNM.OMNMBACKEND.yourPersonality.service.YourPersonalityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
    private final UserService userService;
    private final YourPersonalityService yourPersonalityService;
    private final MyPersonalityRepository myPersonalityRepository;
    private final MyPersonalityService myPersonalityService;

    @PostMapping("/propose/{matchingId}")
    public ResponseEntity<String> proposeRoomMate(@PathVariable Long matchingId, ConnectionDto connectionDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);
        Long userId = user.getUserId();
        String message = connectionDto.getMessage();

        if (mainService.isProposedPerson(userId, matchingId, message)){
            return new ResponseEntity<>("이미 신청한 사람입니다.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("신청 완료되었습니다.", HttpStatus.OK);
        }
    }

    /**
     * 전체 추천 리스트 조회 API
     * --------- 개발 FLOW ---------
     * 1. 로그인 상태인 userId 가져오기 (Security Principal)
     * 2. userId를 바탕으로 yourPersonalityId 구하기
     * 3. 나중 parsing을 위한 gender, dormitory 저장
     * 4. yourPersonalityId를 바탕으로 yourPersonality 객체 구하기
     * 5. yourPersonality 객체 정보 저장 - 상관없음이면 true 같은 condition 만들면 좋을듯
     * 6. myPersonality findAll Parsing(with gender, dormitory) 리스트 가져오기
     * 7. dictionary 세팅 {userId : percent} 형식
     * 8. myPersonality 돌면서 dictionary에 append
     * 9. dictionary sorting (내림차순)
     * 10. 상위 9개 끊기, 만약에 9개가 안되면 그냥 all로 보내면 될듯!
     * 11. dto 반환!
     * */
    @PostMapping
    public ResponseEntity<List<AllRecommendResponseDto>> getRecommendList(Integer criteria){

        /**
         * 개발 FLOW 1
         * */
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        if(user.getIsMatched() == 1){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Long userId = user.getUserId();

        /**
         * 개발 FLOW 2
         * */
        Long yourPersonalityId = user.getYourPersonalityId();

        /**
         * 개발 FLOW 3
         * */
        Integer userGender = user.getGender();
        Integer userDormitory = user.getDormitory();

        /**
         * 개발 FLOW 4
         * */
        YourPersonality yourPersonalityEntity = yourPersonalityService.findYourPersonality(yourPersonalityId);

        /**
         * 개발 FLOW 5
         * */
        String mateAge = yourPersonalityEntity.getAge();
        String mateMbti = yourPersonalityEntity.getMbti();
        Integer mateSmoking = yourPersonalityEntity.getIsSmoking();
        Integer mateDepartment = yourPersonalityEntity.getDepartment();
        Integer mateLifeCycle = yourPersonalityEntity.getLifeCycle();
        Integer mateCleaning = yourPersonalityEntity.getCleaning();
        Integer mateArmyService = yourPersonalityEntity.getArmyService();
        Integer mateNationality = yourPersonalityEntity.getNationality();

        /**
         * 개발 FLOW 6
         * */
        List<MyPersonality> candidateList = myPersonalityRepository.findAll();

        /**
         * 개발 FLOW 7
         * */
        HashMap<Long, Integer> recommendPercent = new HashMap<>();

        /**
         * 개발 FLOW 8
         * age : 20대 초반(1), 20대 중반(2), 20대 후반(3), 30대 이상(4), 상관없음(5)
         * */
        for (MyPersonality myPersonality : candidateList) {
            int matchingCount = 0;   // percent 계산을 위한 count 변수 선언
            Integer convertedAge = mainService.ageConverter(myPersonality.getAge());
            User userEntity = userService.getUserEntity(myPersonality.getUserId());
            MyPersonality loginMyPersonality = myPersonalityService.findMyPersonality(user.getMyPersonalityId());
            if(userEntity.getIsMatched() == 1 || !userEntity.getGender().equals(userGender)
                    || !userEntity.getDormitory().equals(userDormitory) || userEntity.getUserId().equals(user.getUserId())){
                continue;
            }
//            MyPersonality myPersonalityEntity = myPersonalityService.findMyPersonality(userEntity.getMyPersonalityId());

            if (mateAge.contains(String.valueOf(convertedAge)) || mateAge.equals("{4}")) {    // 나이 조사
                matchingCount += 1;
            }
            if (mateMbti.contains(myPersonality.getMbti()) || mateMbti.equals("{ALL}")) {  // mbti 조사
                matchingCount += 1;
            }
            if (mateSmoking == 2 || mateSmoking.equals(myPersonality.getIsSmoking())) {    // 흡연유무 조사
                matchingCount += 1;
            }
            if (mateDepartment == 2) {
                matchingCount += 1;
            }
            if (mateDepartment == 1) {
                if (!loginMyPersonality.getDepartment().equals(myPersonality.getDepartment())) {
                    matchingCount += 1;
                }
            }
            if (mateDepartment == 0) {
                if (loginMyPersonality.getDepartment().equals(myPersonality.getDepartment())) {
                    matchingCount += 1;
                }
            }

            if (mateLifeCycle == 2 || mateLifeCycle.equals(myPersonality.getLifeCycle())) {    // 생활습관 조사
                matchingCount += 1;
            }

            if (mateCleaning == 4 || mateCleaning.equals(myPersonality.getCleaning())) {   // 청소주기 조사
                matchingCount += 1;
            }

            if (mateArmyService == 2 || mateArmyService.equals(myPersonality.getArmyService())) {
                matchingCount += 1;
            }

            if (mateNationality == 1 || myPersonality.getNationality()==0) {
                matchingCount += 1;
            }
            if(matchingCount>=criteria){
                recommendPercent.put(myPersonality.getUserId(), matchingCount);
            }
        }

        /**
         * 개발 FLOW 9
         * */
        List<Map.Entry<Long, Integer>> entryList = new LinkedList<>(recommendPercent.entrySet());

        /**
         * 개발 FLOW 10
         * */
        List<AllRecommendResponseDto> allRecommendResponseDtoList = new ArrayList<>();
        for(Map.Entry<Long, Integer> entry : entryList){
            User userEntity = userService.getUserEntity(entry.getKey());
            MyPersonality myPersonality = myPersonalityService.findMyPersonality(userEntity.getMyPersonalityId());
            AllRecommendResponseDto allRecommendResponseDto = new AllRecommendResponseDto();
            allRecommendResponseDto.setUserId(entry.getKey());
            allRecommendResponseDto.setAge(myPersonality.getAge());
            allRecommendResponseDto.setIntroduction(myPersonality.getIntroduction());
            allRecommendResponseDto.setName(userEntity.getName());
            allRecommendResponseDto.setProfileUrl(userEntity.getProfileUrl());
            allRecommendResponseDto.setPercent((float)((entry.getValue()/8.0)*100));
            allRecommendResponseDto.setMbti(myPersonality.getMbti());
            allRecommendResponseDtoList.add(allRecommendResponseDto);
        }

        Collections.shuffle(allRecommendResponseDtoList);
        if(allRecommendResponseDtoList.size() > 9){
            allRecommendResponseDtoList = allRecommendResponseDtoList.subList(0,9);
        }

        /**
         * 개발 FLOW 11
         * */
        return new ResponseEntity<>(allRecommendResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<DetailRecommendResponseDto> getUserDetailProfile(@PathVariable Long userId){

        int percentCount = 0;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User myUser = userService.getUserEntityByLoginId(username); // 로그인한 유저
        User profileUser = userService.getUserEntity(userId); // 탐색하려는 상대 유저
        MyPersonality profileMyPersonality = myPersonalityService.findMyPersonality(profileUser.getMyPersonalityId()); // 상대 유저의 my설문조사
        YourPersonality wantedPersonality = yourPersonalityService.findYourPersonality(myUser.getYourPersonalityId()); // 로그인한 유저의 your설문조사
        MyPersonality applierPersonality = myPersonalityService.findMyPersonality(myUser.getMyPersonalityId()); // 로그인한 유저의 my설문조사

        /**
         * myUser의 yourPersonality랑 profileUser의 myPersonality가 얼마나 맞는지 알아야됨
         * */
        // 상대 유저의 정보를 detailRecommendResponseDto 에 담아줌
        DetailRecommendResponseDto detailRecommendResponseDto = new DetailRecommendResponseDto();
        detailRecommendResponseDto.setProfileUrl(profileUser.getProfileUrl());
        detailRecommendResponseDto.setDormitory(profileUser.getDormitory());
        detailRecommendResponseDto.setName(profileUser.getName());
        detailRecommendResponseDto.setDepartment(profileMyPersonality.getDepartment());
        detailRecommendResponseDto.setMbti(profileMyPersonality.getMbti());
        detailRecommendResponseDto.setAge(profileMyPersonality.getAge());

        if(profileUser.getGender() == 1){
            detailRecommendResponseDto.setArmyService(null);
        }
        else{
            detailRecommendResponseDto.setArmyService(profileMyPersonality.getArmyService());
        }
        detailRecommendResponseDto.setNationality(profileMyPersonality.getNationality());
        detailRecommendResponseDto.setIsCleaning(profileMyPersonality.getCleaning());
        detailRecommendResponseDto.setLifeCycle(profileMyPersonality.getLifeCycle());
        detailRecommendResponseDto.setSleepingPattern(profileMyPersonality.getSleepingPattern());
        detailRecommendResponseDto.setIsSmoking(profileMyPersonality.getIsSmoking());
        detailRecommendResponseDto.setKakaoId(profileUser.getKakaoId());

        /**
         * percent 비교 계산에 들어가는 항목
         * 1. isSmoking - done
         * 2. Cleaning - done
         * 3. mbti - done
         * 4. nationality - done
         * 5. department - done
         * 6. age - done
         * 7. armyService - done
         * 8. lifeCycle
         * */
        if(wantedPersonality.getIsSmoking() == 2 || Objects.equals(wantedPersonality.getIsSmoking(), profileMyPersonality.getIsSmoking())){
            percentCount+=1;
        }
        if(wantedPersonality.getCleaning() == 4 || Objects.equals(wantedPersonality.getCleaning(), profileMyPersonality.getCleaning())){
            percentCount+=1;
        }
        if(Objects.equals(wantedPersonality.getMbti(), "{ALL}") || wantedPersonality.getMbti().contains(profileMyPersonality.getMbti())){
            percentCount+=1;
        }
        if(wantedPersonality.getDepartment() == 2){
            percentCount+=1;
        }
        if(wantedPersonality.getDepartment() == 1){
            if(!profileMyPersonality.getDepartment().equals(applierPersonality.getDepartment())){
                percentCount+=1;
            }
        }
        if(wantedPersonality.getDepartment() == 0){
            if(profileMyPersonality.getDepartment().equals(applierPersonality.getDepartment())){
                percentCount+=1;
            }
        }
        if(wantedPersonality.getNationality() == 1 || profileMyPersonality.getNationality()==0){
            percentCount+=1;
        }
        if(wantedPersonality.getAge().equals("{4}") || wantedPersonality.getAge().contains(String.valueOf(mainService.ageConverter(profileMyPersonality.getAge())))){
            percentCount+=1;
        }
        if(wantedPersonality.getArmyService() == 2 || wantedPersonality.getArmyService().equals(profileMyPersonality.getArmyService())){
            percentCount+=1;
        }
        if(wantedPersonality.getLifeCycle() == 2 || wantedPersonality.getLifeCycle().equals(profileMyPersonality.getLifeCycle())){
            percentCount+=1;
        }
        detailRecommendResponseDto.setPercent((float)((percentCount / 8.0) * 100));
        return new ResponseEntity<>(detailRecommendResponseDto, HttpStatus.OK);
    }
}
