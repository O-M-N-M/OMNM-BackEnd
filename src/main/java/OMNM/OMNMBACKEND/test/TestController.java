package OMNM.OMNMBACKEND.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "스프링 시큐리티 테스트";
    }
}