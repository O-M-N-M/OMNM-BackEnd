package OMNM.OMNMBACKEND.token;

import OMNM.OMNMBACKEND.token.domain.Token;
import OMNM.OMNMBACKEND.token.repository.TokenRepository;
import OMNM.OMNMBACKEND.token.service.TokenService;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.dto.TokenDto;
import OMNM.OMNMBACKEND.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtTokenService jwtTokenService;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    @PostMapping("")
    public ResponseEntity<String> getnewToken(TokenDto tokenDto){
        if(tokenService.isValidRefreshToken(tokenDto.getAccessToken(), tokenDto.getRefreshToken())){
            Optional<Token> tokenEntity = tokenRepository.findByAccessTokenAndRefreshToken(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
            String newAccessToken = jwtTokenService.createJWT(tokenEntity.get().getLoginId());
            tokenEntity.get().setAccessToken(newAccessToken);
            tokenRepository.save(tokenEntity.get());
            return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("재로그인 요청", HttpStatus.FORBIDDEN);
        }
    }
}
