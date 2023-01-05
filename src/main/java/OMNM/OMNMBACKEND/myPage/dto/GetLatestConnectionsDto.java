package OMNM.OMNMBACKEND.myPage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetLatestConnectionsDto {
    private Long userId;
    private String profileUrl;
    private String name;
    private Integer age;
}
