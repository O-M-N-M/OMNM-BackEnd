package OMNM.OMNMBACKEND.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

    private String secretKey = "OMNMprojectLOGINservice";
    private long tokenValidTime = 30 * 60 * 1000L;
    private final UserDetailsService userDetailsService;

    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createJWT(String userLoginId, List<String> roles){
        Claims claims = Jwts.claims().setSubject(userLoginId);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication authentication(String JwtToken){
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(JwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getUserId(String JwtToken){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(JwtToken).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader("OMNM");
    }

    public boolean validateToken(String JwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(JwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e){
            return false;
        }
    }
}
