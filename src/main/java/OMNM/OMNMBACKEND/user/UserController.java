package OMNM.OMNMBACKEND.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @GetMapping("")
    @ResponseBody
    public String test(){
        return "30시간의 삽질 끝에 드디어 배포 성공..!";
    }
}
