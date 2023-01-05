package OMNM.OMNMBACKEND.token.service;

import OMNM.OMNMBACKEND.token.domain.Token;
import OMNM.OMNMBACKEND.token.repository.TokenRepository;
import OMNM.OMNMBACKEND.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtTokenService jwtTokenService;

    public Boolean isValidRefreshToken(String accessToken, String refreshToken){
        if(jwtTokenService.validateToken(refreshToken)){    // refreshToken이 유효한지 확인
            Optional<Token> token = tokenRepository.findByAccessTokenAndRefreshToken(accessToken, refreshToken);
            return token.isPresent();
        }
        else{
            return false;
        }
    }
}
