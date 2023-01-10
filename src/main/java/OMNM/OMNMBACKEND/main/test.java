//package OMNM.OMNMBACKEND.main;
//
//import OMNM.OMNMBACKEND.main.dto.AllRecommendResponseDto;
//import OMNM.OMNMBACKEND.myPersonality.domain.MyPersonality;
//import OMNM.OMNMBACKEND.user.domain.User;
//import OMNM.OMNMBACKEND.yourPersonality.domain.YourPersonality;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.util.*;
//
//public class test {
//
//    @PostMapping
//    public ResponseEntity<List<AllRecommendResponseDto>> getRecommendList(Integer criteria){
//
//        /**
//         * 개발 FLOW 1
//         * */
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = ((UserDetails) principal).getUsername();
//        User user = userService.getUserEntityByLoginId(username);
//
//        if(user.getIsMatched() == 1){
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//
//        Long userId = user.getUserId(); // 로그인한 유저의 userId
//
//        /**
//         * 개발 FLOW 2
//         * */
//        Long yourPersonalityId = user.getYourPersonalityId(); // 로그인한 유저의 yourId
//
//        /**
//         * 개발 FLOW 3
//         * */
//        Integer userGender = user.getGender();
//        Integer userDormitory = user.getDormitory();
//
//        /**
//         * 개발 FLOW 4
//         * */
//        YourPersonality yourPersonalityEntity = yourPersonalityService.findYourPersonality(yourPersonalityId); // 로그인한 유저의 yourPersonality
//
//        /**
//         * 개발 FLOW 5 // 로그인한 유저의 상대 설문조사 결과
//         * */
//        String mateAge = yourPersonalityEntity.getAge();
//        String mateMbti = yourPersonalityEntity.getMbti();
//        Integer mateSmoking = yourPersonalityEntity.getIsSmoking();
//        Integer mateDepartment = yourPersonalityEntity.getDepartment();
//        Integer mateLifeCycle = yourPersonalityEntity.getLifeCycle();
//        Integer mateCleaning = yourPersonalityEntity.getCleaning();
//        Integer mateArmyService = yourPersonalityEntity.getArmyService();
//        Integer mateNationality = yourPersonalityEntity.getNationality();
//
//        /**
//         * 개발 FLOW 6
//         * */
//        List<MyPersonality> candidateList = myPersonalityRepository.findAll();
//
//        /**
//         * 개발 FLOW 7
//         * */
//        HashMap<Long, Integer> recommendPercent = new HashMap<>();
//
//        /**
//         * 개발 FLOW 8
//         * age : 20대 초반(1), 20대 중반(2), 20대 후반(3), 30대 이상(4), 상관없음(5)
//         * */
//        for (MyPersonality myPersonality : candidateList) {
//            int matchingCount = 0;   // percent 계산을 위한 count 변수 선언
//            Integer convertedAge = mainService.ageConverter(myPersonality.getAge());
//            User userEntity = userService.getUserEntity(myPersonality.getUserId());
//            MyPersonality loginMyPersonality = myPersonalityService.findMyPersonality(user.getMyPersonalityId());
//            if(userEntity.getIsMatched() == 1 || !userEntity.getGender().equals(userGender)
//                    || !userEntity.getDormitory().equals(userDormitory) || userEntity.getUserId().equals(user.getUserId())){
//                continue;
//            }
//            MyPersonality myPersonalityEntity = myPersonalityService.findMyPersonality(userEntity.getMyPersonalityId());
//
//            if (mateAge.contains(String.valueOf(convertedAge)) || mateAge.equals("{4}")) {    // 나이 조사
//                matchingCount += 1;
//            }
//            if (mateMbti.contains(myPersonality.getMbti()) || mateMbti.equals("{ALL}")) {  // mbti 조사
//                matchingCount += 1;
//            }
//            if (mateSmoking == 2 || mateSmoking.equals(myPersonality.getIsSmoking())) {    // 흡연유무 조사
//                matchingCount += 1;
//            }
//            if (mateDepartment == 2) {
//                matchingCount += 1;
//            }
//            if (mateDepartment == 1) {
//                if (!loginMyPersonality.getDepartment().equals(myPersonality.getDepartment())) {
//                    matchingCount += 1;
//                }
//            }
//            if (mateDepartment == 0) {
//                if (loginMyPersonality.getDepartment().equals(myPersonality.getDepartment())) {
//                    matchingCount += 1;
//                }
//            }
//
//            if (mateLifeCycle == 2 || mateLifeCycle.equals(myPersonality.getLifeCycle())) {    // 생활습관 조사
//                matchingCount += 1;
//            }
//
//            if (mateCleaning == 4 || mateCleaning.equals(myPersonality.getCleaning())) {   // 청소주기 조사
//                matchingCount += 1;
//            }
//
//            if (mateArmyService == 2 || mateArmyService.equals(myPersonality.getArmyService())) {
//                matchingCount += 1;
//            }
//
//            if (mateNationality == 1 || myPersonality.getNationality()==0) {
//                matchingCount += 1;
//            }
//            if(matchingCount>=criteria){
//                recommendPercent.put(myPersonality.getUserId(), matchingCount);
//            }
//        }
//
//        /**
//         * 개발 FLOW 9
//         * */
//        List<Map.Entry<Long, Integer>> entryList = new LinkedList<>(recommendPercent.entrySet());
//        entryList.sort(((o1, o2) -> recommendPercent.get(o2.getKey()) - recommendPercent.get(o1.getKey())));
//
//        /**
//         * 개발 FLOW 10
//         * */
//        List<AllRecommendResponseDto> allRecommendResponseDtoList = new ArrayList<>();
//        int count = 0;
//        for(Map.Entry<Long, Integer> entry : entryList){
//            User userEntity = userService.getUserEntity(entry.getKey());
//            MyPersonality myPersonality = myPersonalityService.findMyPersonality(userEntity.getMyPersonalityId());
//            AllRecommendResponseDto allRecommendResponseDto = new AllRecommendResponseDto();
//            allRecommendResponseDto.setUserId(entry.getKey());
//            allRecommendResponseDto.setAge(myPersonality.getAge());
//            allRecommendResponseDto.setIntroduction(myPersonality.getIntroduction());
//            allRecommendResponseDto.setName(userEntity.getName());
//            allRecommendResponseDto.setProfileUrl(userEntity.getProfileUrl());
//            allRecommendResponseDto.setPercent((float)((entry.getValue()/8.0)*100));
//            allRecommendResponseDto.setMbti(myPersonality.getMbti());
//            allRecommendResponseDtoList.add(allRecommendResponseDto);
//            count+=1;
//            if(count == 9){
//                break;
//            }
//        }
//
//        /**
//         * 개발 FLOW 11
//         * */
//        return new ResponseEntity<>(allRecommendResponseDtoList, HttpStatus.OK);
//    }
//}
