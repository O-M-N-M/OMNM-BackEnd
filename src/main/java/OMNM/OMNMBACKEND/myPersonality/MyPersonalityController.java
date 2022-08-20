package OMNM.OMNMBACKEND.myPersonality;

import OMNM.OMNMBACKEND.myPersonality.domain.MyPersonality;
import OMNM.OMNMBACKEND.myPersonality.dto.MyPersonalityDto;
import OMNM.OMNMBACKEND.myPersonality.service.MyPersonalityService;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/myPersonality")
@RequiredArgsConstructor
public class MyPersonalityController {

    /*
    Integer age;
    private String mbti;
    private Integer isSmoking;
    private String department;
    private Integer lifeCycle;
    private String sleepingPattern;
    private Integer cleaning;
    private String nationality;
    private Integer armyService;
    private String introduction;
    * */

    private final MyPersonalityService myPersonalityService;
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<String> registerMyPersonality(MyPersonalityDto myPersonalityDto){
        String loginId = "dlrlxo999";
        Optional<User> user = userService.checkLoginId(loginId);
        MyPersonality myPersonality = new MyPersonality();
        myPersonality.setAge(myPersonalityDto.getAge());
        myPersonality.setMbti(myPersonalityDto.getMbti());
        myPersonality.setIsSmoking(myPersonalityDto.getIsSmoking());
        myPersonality.setDepartment(myPersonalityDto.getDepartment());
        myPersonality.setLifeCycle(myPersonalityDto.getLifeCycle());
        myPersonality.setSleepingPattern(myPersonalityDto.getSleepingPattern());
        myPersonality.setCleaning(myPersonalityDto.getCleaning());
        myPersonality.setNationality(myPersonalityDto.getNationality());
        myPersonality.setArmyService(myPersonalityDto.getArmyService());
        myPersonality.setIntroduction(myPersonalityDto.getIntroduction());
        myPersonalityService.saveMyPersonality(myPersonality);
        user.ifPresent(value -> value.setMyPersonalityId(myPersonality.getMyPersonalityId()));
        userService.saveUser(user.get());
        return new ResponseEntity<>("나의 성향 설문 등록 완료", HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<String> modifyMyPersonality(MyPersonalityDto myPersonalityDto){
        String loginId = "dlrlxo999";
        Optional<User> user = userService.checkLoginId(loginId);
        Long myPersonalityId = user.get().getMyPersonalityId();
        MyPersonality myPersonality = myPersonalityService.findMyPersonality(myPersonalityId);
        myPersonality.setAge(myPersonalityDto.getAge());
        myPersonality.setMbti(myPersonalityDto.getMbti());
        myPersonality.setIsSmoking(myPersonalityDto.getIsSmoking());
        myPersonality.setDepartment(myPersonalityDto.getDepartment());
        myPersonality.setLifeCycle(myPersonalityDto.getLifeCycle());
        myPersonality.setSleepingPattern(myPersonalityDto.getSleepingPattern());
        myPersonality.setCleaning(myPersonalityDto.getCleaning());
        myPersonality.setNationality(myPersonalityDto.getNationality());
        myPersonality.setArmyService(myPersonalityDto.getArmyService());
        myPersonality.setIntroduction(myPersonalityDto.getIntroduction());
        myPersonalityService.saveMyPersonality(myPersonality);
        return new ResponseEntity<>("나의 성향 설문 수정 완료", HttpStatus.OK);
    }
}
