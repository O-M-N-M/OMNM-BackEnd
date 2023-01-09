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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("yourPersonality")
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
    public ResponseEntity<YourPersonalityDto> viewYourPersonality(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        Long yourPersonalityId = user.getYourPersonalityId();

        if(yourPersonalityId == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        YourPersonality yourPersonality = yourPersonalityService.findYourPersonality(yourPersonalityId);
        YourPersonalityDto yourPersonalityDto = new YourPersonalityDto();
        yourPersonalityDto.setDepartment(yourPersonality.getDepartment());
        yourPersonalityDto.setAge(yourPersonality.getAge());
        yourPersonalityDto.setCleaning(yourPersonality.getCleaning());
        yourPersonalityDto.setMbti(yourPersonality.getMbti());
        yourPersonalityDto.setIsSmoking(yourPersonality.getIsSmoking());
        yourPersonalityDto.setNationality(yourPersonality.getNationality());
        yourPersonalityDto.setLifeCycle(yourPersonality.getLifeCycle());

        if(user.getGender() == 1){
            yourPersonalityDto.setArmyService(null);
        }
        else{
            yourPersonalityDto.setArmyService(yourPersonality.getArmyService());
        }

        return new ResponseEntity<>(yourPersonalityDto, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> registerYourPersonality(YourPersonalityDto yourPersonalityDto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);
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
