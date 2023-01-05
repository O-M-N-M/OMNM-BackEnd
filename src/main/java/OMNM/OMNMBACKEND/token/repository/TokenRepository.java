package OMNM.OMNMBACKEND.token.repository;

import OMNM.OMNMBACKEND.token.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessTokenAndRefreshToken(String accessToken, String refreshToken);
}
