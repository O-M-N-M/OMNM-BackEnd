package OMNM.OMNMBACKEND.myPage.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 이름
 * 나이
 * 프로필 사진
 * 시간
 * */
@Getter @Setter
public class PagingViewUserDto {
    private Long userId;
    private String profileUrl;
    private String name;
    private Integer age;
    private String time;
}
