package OMNM.OMNMBACKEND.myInfo;

import OMNM.OMNMBACKEND.connection.domain.Connection;
import OMNM.OMNMBACKEND.connection.repository.ConnectionRepository;
import OMNM.OMNMBACKEND.myPersonality.dto.MyPersonalityDto;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/myInfo")
@RequiredArgsConstructor
public class MyInfoController {

    private final UserService userService;
    private final ConnectionRepository connectionRepository;

    @GetMapping("")
    public ResponseEntity<MyInfoDto> getMyInfo(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        MyInfoDto myInfoDto = new MyInfoDto();
        myInfoDto.setUserId(user.getUserId());
        myInfoDto.setName(user.getName());
        myInfoDto.setKakaoId(user.getKakaoId());
        myInfoDto.setDormitory(user.getDormitory());
        myInfoDto.setProfileUrl(user.getProfileUrl());

        return new ResponseEntity<>(myInfoDto, HttpStatus.OK);
    }

    @GetMapping("/isMatched")
    public ResponseEntity<Boolean> getIsMatched(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        if(user.getIsMatched() == 1){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @GetMapping("/isMale")
    public ResponseEntity<Boolean> getIsMale(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        if(user.getGender() == 1){
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
    }

    @GetMapping("/doneSurvey")
    public ResponseEntity<Boolean> getDoneSurvey(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        if(user.getMyPersonalityId() == null){
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
    }

    @GetMapping("/isProposed/{userId}")
    public ResponseEntity<Boolean> getIsProposed(@PathVariable Long userId){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);

        Optional<Connection> connection = connectionRepository.findByFromIdAndToId(user.getUserId(), userId);

        if(connection.isPresent()){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
}
