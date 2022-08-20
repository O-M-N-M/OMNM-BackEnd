package OMNM.OMNMBACKEND.findUser.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindLoginPwDto {
    private String loginId;
    private String email;
    private String name;
}
