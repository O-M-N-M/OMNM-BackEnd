package OMNM.OMNMBACKEND.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String school;
    private Integer gender;
    private String profileUrl;
    private String kakaoId;
    private Integer dormitory;
}
