package OMNM.OMNMBACKEND.main.dto;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class DetailRecommendResponseDto {
    private String profileUrl;
    private String name;
    private Integer age;
    private String mbti;
    private Float percent;
    private Integer isSmoking;
    private Integer lifeCycle;
    private String sleepingPattern;
    private Integer isCleaning;
    private Integer nationality;
    private Integer armyService;
    private Integer dormitory;
    private String department;
    private String kakaoId;
}
