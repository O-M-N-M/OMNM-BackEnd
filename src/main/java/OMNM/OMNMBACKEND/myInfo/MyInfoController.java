package OMNM.OMNMBACKEND.myInfo;

import OMNM.OMNMBACKEND.myPersonality.dto.MyPersonalityDto;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myInfo")
@RequiredArgsConstructor
public class MyInfoController {

    private final UserService userService;

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
}
