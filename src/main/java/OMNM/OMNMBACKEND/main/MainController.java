package OMNM.OMNMBACKEND.main;

import OMNM.OMNMBACKEND.main.service.MainService;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
    private final UserService userService;

    @PostMapping("/propose/{matchingId}")
    public ResponseEntity<String> proposeRoomMate(@PathVariable Long matchingId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserEntityByLoginId(username);
        Long userId = user.getUserId();

        if (mainService.isProposedPerson(userId, matchingId)){
            return new ResponseEntity<>("이미 신청한 사람입니다.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("신청 완료되었습니다.", HttpStatus.OK);
        }
    }
}
