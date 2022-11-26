package OMNM.OMNMBACKEND.main.dto;

import lombok.Getter;
import lombok.Setter;

//6. dto 세팅 (profileUrl, userId, age, mbti, percent, introduction, name)
@Getter @Setter
public class AllRecommendResponseDto {
    private Long userId;
    private String profileUrl;
    private Integer age;
    private String mbti;
    private String introduction;
    private String name;
    private Float percent;
}
