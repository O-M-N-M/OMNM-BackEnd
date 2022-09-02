package OMNM.OMNMBACKEND.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private static final String[] PERMIT_URL_ARRAY={
            "/","/**/*.png","/**/*.jpg","/**/*.js","/**/*.css","/**/*.html","/**/*.gif","/**/*.svg"
            ,"/join", "/join/**", "/login", "/login/**"
            ,"/find", "/find/**"
            ,"/users", "/users/**"
            ,"/test", "/test/**"
            ,"/logout", "/logout/**"
            ,"/myPersonality", "/myPersonality/**"
            ,"/main", "/main/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .cors()
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .logout()
                .logoutUrl("/users/logout")
                .invalidateHttpSession(true)
                .deleteCookies("OMNM")
                .and()
                .authorizeRequests()
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .anyRequest().authenticated();
    }

}
