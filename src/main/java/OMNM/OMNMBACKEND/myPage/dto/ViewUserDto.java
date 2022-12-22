package OMNM.OMNMBACKEND.myPage.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 프로필 사진
 * 이름
 * 나이
 * 입실정보
 * mbti
 * 학과
 * 생활패턴
 * 수면패턴
 * 청소빈도
 * 국적
 * 군복무
 * 자기소개
 * */

@Getter @Setter
public class ViewUserDto {
    private String profileUrl;
    private String name;
    private Integer age;
    private Integer dormitory;
    private String mbti;
    private String department;
    private Integer lifeCycle;
    private String sleepingPattern;
    private Integer cleaning;
    private Integer nationality;
    private Integer armyService;
    private String introduction;
}
