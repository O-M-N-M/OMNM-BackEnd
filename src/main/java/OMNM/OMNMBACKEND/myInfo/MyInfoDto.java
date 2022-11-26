package OMNM.OMNMBACKEND.myInfo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyInfoDto {
    private Long userId;
    private String kakaoId;
    private String name;
    private Integer dormitory;
    private String profileUrl;
}
