package OMNM.OMNMBACKEND.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @Setter
@Table(name = "user")
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "loginId",nullable = false)
    private String loginId;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "school", nullable = false)
    private String school;

    @Column(name = "gender",nullable = false)
    private Integer gender;

    @Column(name = "profile_url",nullable = true)
    private String profileUrl;

    @Column(name = "is_matched",nullable = false)
    private Integer isMatched = 0;

    @Column(name = "my_personality_id", nullable = true)
    private Long myPersonalityId;

    @Column(name = "your_personality_id",nullable = true)
    private Long yourPersonalityId;

    @Column(name = "status",nullable = false)
    private Integer status = 1;

    @Column(name = "kakao_Id", nullable = false)
    private String kakaoId;

    @Column(name = "dormitory",nullable = false)
    private Integer dormitory;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

