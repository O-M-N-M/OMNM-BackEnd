package OMNM.OMNMBACKEND.security;

import OMNM.OMNMBACKEND.blackList.service.BlackListService;
import OMNM.OMNMBACKEND.utils.JwtAuthenticationFilter;
import OMNM.OMNMBACKEND.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenService jwtTokenService;
    private final BlackListService blackListService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private static final String[] PERMIT_URL_ARRAY={
            "/","/**/*.png","/**/*.jpg","/**/*.js","/**/*.css","/**/*.html","/**/*.gif","/**/*.svg"
            ,"/home", "/home/**"
            ,"/join", "/join/**", "/login", "/login/**"
            ,"/find", "/find/**",
            "/token", "/token/**"
    };

    private static final String[] AUTHENTICATED_URL_ARRAY={
            "/users", "/users/**"
            ,"/myPersonality", "/myPersonality/**"
            ,"/main", "/main/**"
            ,"/yourPersonality", "/yourPersonality/**"
            ,"/test", "/test/**"
            ,"/myInfo", "/myInfo/**"
            ,"/users", "/users/**"
    };

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.httpFirewall(defaultHttpFirewall());
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        //http.httpBasic().disable(); // ???????????? ????????? ?????? ?????? ???????????? ????????? ??????, header??? id, pw??? ?????? token(jwt)??? ?????? ??????. ????????? basic??? ?????? bearer??? ????????????.
        http.httpBasic().disable()
                .authorizeRequests()// ????????? ?????? ???????????? ??????
                .antMatchers(AUTHENTICATED_URL_ARRAY).authenticated()
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenService, blackListService),
                        UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter??? UsernamePasswordAuthenticationFilter ?????? ?????????
        // + ????????? ????????? ??????????????? ??????????????? ?????? ????????? CustomUserDetailService ???????????? ???????????????.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
