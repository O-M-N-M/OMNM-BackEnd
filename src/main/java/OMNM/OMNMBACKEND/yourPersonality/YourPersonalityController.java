package OMNM.OMNMBACKEND.yourPersonality;

import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.service.UserService;
import OMNM.OMNMBACKEND.yourPersonality.domain.YourPersonality;
import OMNM.OMNMBACKEND.yourPersonality.dto.YourPersonalityDto;
import OMNM.OMNMBACKEND.yourPersonality.repository.YourPersonalityRepository;
import OMNM.OMNMBACKEND.yourPersonality.service.YourPersonalityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/yourPersonality")
public class YourPersonalityController {
    private final YourPersonalityService yourPersonalityService;
    private final YourPersonalityRepository yourPersonalityRepository;
    private final UserService userService;

    /*
    private String age;
    private String mbti;
    private Integer isSmoking;
    private Integer department;
    private Integer lifeCycle;
    private Integer cleaning;
    private Integer nationality;
    private Integer armyService;
     */

    @GetMapping("")
    public ResponseEntity<YourPersonality> viewYourPersonality(){
        Long id = 11L;
        User user = userService.getUserEntity(id);
        Long yourPersonalityId = user.getYourPersonalityId();
        YourPersonality yourPersonality = yourPersonalityService.findYourPersonality(yourPersonalityId);
        if(yourPersonality == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(yourPersonality, HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<String> registerYourPersonality(YourPersonalityDto yourPersonalityDto){
        Long id = 11L;
        User user = userService.getUserEntity(id);
        YourPersonality yourPersonality = new YourPersonality();
        yourPersonality.setAge(yourPersonalityDto.getAge());
        yourPersonality.setMbti(yourPersonalityDto.getMbti());
        yourPersonality.setIsSmoking(yourPersonalityDto.getIsSmoking());
        yourPersonality.setDepartment(yourPersonalityDto.getDepartment());
        yourPersonality.setLifeCycle(yourPersonalityDto.getLifeCycle());
        yourPersonality.setCleaning(yourPersonalityDto.getCleaning());
        yourPersonality.setNationality(yourPersonalityDto.getNationality());
        yourPersonality.setArmyService(yourPersonalityDto.getArmyService());
        yourPersonalityService.saveYourPersonality(yourPersonality);
        user.setYourPersonalityId(yourPersonality.getYourPersonalityId());
        userService.saveUser(user);
        return new ResponseEntity<>("상대 성향 설문 등록 완료", HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<String> modifyYourPersonality(YourPersonalityDto yourPersonalityDto){
        Long id = 11L;
        User user = userService.getUserEntity(id);
        Long yourPersonalityId = user.getYourPersonalityId();
        YourPersonality yourPersonality = yourPersonalityService.findYourPersonality(yourPersonalityId);
        yourPersonality.setAge(yourPersonalityDto.getAge());
        yourPersonality.setMbti(yourPersonalityDto.getMbti());
        yourPersonality.setIsSmoking(yourPersonalityDto.getIsSmoking());
        yourPersonality.setDepartment(yourPersonalityDto.getDepartment());
        yourPersonality.setLifeCycle(yourPersonalityDto.getLifeCycle());
        yourPersonality.setCleaning(yourPersonalityDto.getCleaning());
        yourPersonality.setNationality(yourPersonalityDto.getNationality());
        yourPersonality.setArmyService(yourPersonalityDto.getArmyService());
        yourPersonalityService.saveYourPersonality(yourPersonality);
        return new ResponseEntity<>("상대 성향 설문 수정 완료", HttpStatus.OK);
    }
}
