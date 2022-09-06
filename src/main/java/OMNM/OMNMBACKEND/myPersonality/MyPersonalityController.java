package OMNM.OMNMBACKEND.myPersonality;

import OMNM.OMNMBACKEND.myPersonality.domain.MyPersonality;
import OMNM.OMNMBACKEND.myPersonality.dto.MyPersonalityDto;
import OMNM.OMNMBACKEND.myPersonality.service.MyPersonalityService;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/myPersonality")
@RequiredArgsConstructor
public class MyPersonalityController {

    /**
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
        Long id = 11L;
        User user = userService.getUserEntity(id);
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
        user.setMyPersonalityId(myPersonality.getMyPersonalityId());
        userService.saveUser(user);
        return new ResponseEntity<>("나의 성향 설문 등록 완료", HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<String> modifyMyPersonality(MyPersonalityDto myPersonalityDto){
        Long id = 10L;
        User user = userService.getUserEntity(id);
        Long myPersonalityId = user.getMyPersonalityId();
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

    /**
     * 로그인 되어 있는 상태에서 내 성향 설문조사 들어가면 조사를 안한 상태면 null, 조사를 한 상태면 한 정보를 넘겨주기
     */

    @GetMapping("")
    public MyPersonality showMyPersonality(){
        Long id = 11L;
        User user = userService.getUserEntity(id);
        Long myPersonalityId = user.getMyPersonalityId();
        if (myPersonalityId == null){
            return null;
        }
        return myPersonalityService.findMyPersonality(myPersonalityId);
    }
}
