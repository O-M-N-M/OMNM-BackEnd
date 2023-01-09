package OMNM.OMNMBACKEND.myPersonality;

import OMNM.OMNMBACKEND.myPersonality.domain.MyPersonality;
import OMNM.OMNMBACKEND.myPersonality.dto.MyPersonalityDto;
import OMNM.OMNMBACKEND.myPersonality.service.MyPersonalityService;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);
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
        myPersonality.setUserId(user.getUserId());
        myPersonalityService.saveMyPersonality(myPersonality);
        user.setMyPersonalityId(myPersonality.getMyPersonalityId());
        userService.saveUser(user);
        return new ResponseEntity<>("나의 성향 설문 등록 완료", HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<String> modifyMyPersonality(MyPersonalityDto myPersonalityDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);
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
        myPersonality.setUserId(user.getUserId());
        myPersonalityService.saveMyPersonality(myPersonality);
        return new ResponseEntity<>("나의 성향 설문 수정 완료", HttpStatus.OK);
    }

    /**
     * 로그인 되어 있는 상태에서 내 성향 설문조사 들어가면 조사를 안한 상태면 null, 조사를 한 상태면 한 정보를 넘겨주기
     */

    @GetMapping("")
    public ResponseEntity<MyPersonalityDto> showMyPersonality(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        Long myPersonalityId = user.getMyPersonalityId();

        if (myPersonalityId == null){
            return null;
        }
        MyPersonality myPersonality = myPersonalityService.findMyPersonality(myPersonalityId);
        MyPersonalityDto myPersonalityDto = new MyPersonalityDto();

        myPersonalityDto.setAge(myPersonality.getAge());
        myPersonalityDto.setCleaning(myPersonality.getCleaning());
        myPersonalityDto.setDepartment(myPersonality.getDepartment());
        myPersonalityDto.setIntroduction(myPersonality.getIntroduction());
        myPersonalityDto.setIsSmoking(myPersonality.getIsSmoking());
        myPersonalityDto.setNationality(myPersonality.getNationality());
        myPersonalityDto.setLifeCycle(myPersonality.getLifeCycle());
        myPersonalityDto.setSleepingPattern(myPersonality.getSleepingPattern());
        myPersonalityDto.setMbti(myPersonality.getMbti());

        if(user.getGender() == 1){
            myPersonalityDto.setArmyService(null);
        }
        else{
            myPersonalityDto.setArmyService(myPersonality.getArmyService());
        }

        return new ResponseEntity<>(myPersonalityDto, HttpStatus.OK);
    }
}
