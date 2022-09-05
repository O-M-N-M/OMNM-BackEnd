package OMNM.OMNMBACKEND.home;

import OMNM.OMNMBACKEND.home.dto.HomeDto;
import OMNM.OMNMBACKEND.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class HomeController{

    private final HomeService homeService;

    @GetMapping("")
    public ResponseEntity<HomeDto> welcomePage(HomeDto homeDto){
        homeDto.setUserCount(homeService.getMemberCount());
        homeDto.setMatchedCount(homeService.getMatchedCount());
        return new ResponseEntity<>(homeDto, HttpStatus.OK);
    }
}
