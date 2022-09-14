package OMNM.OMNMBACKEND.test;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/test")
    public String test(){
        return "Spring Security Jwt Token Test 성공!";
    }
}