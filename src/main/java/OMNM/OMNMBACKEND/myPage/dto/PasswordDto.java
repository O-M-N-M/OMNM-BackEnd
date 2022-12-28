package OMNM.OMNMBACKEND.myPage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PasswordDto {
    private String originalPassword;
    private String newPassword;
}
