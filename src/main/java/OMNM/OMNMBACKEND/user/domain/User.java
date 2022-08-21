package OMNM.OMNMBACKEND.user.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "user")
public class User {

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
}
