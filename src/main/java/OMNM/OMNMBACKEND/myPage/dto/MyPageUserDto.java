package OMNM.OMNMBACKEND.myPage.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 이름 (수정불가)
 * 프로필 사진
 * 입실 정보
 * 카카오톡 아이디
 * */

@Getter @Setter
public class MyPageUserDto {
    private String name;
    private String profileUrl;
    private Integer dormitory;
    private String kakaoId;
}
