package OMNM.OMNMBACKEND.myPage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ViewConnectionUserDto {
    private Long userId;
    private String profileUrl;
    private String name;
    private Integer age;
    private String time;
    private String mbti;
    private Integer lifeCycle;
    private boolean isLast;
}
