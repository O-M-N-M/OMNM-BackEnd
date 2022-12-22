package OMNM.OMNMBACKEND.myPersonality.domain;

/*
 * myPersonalityId
 * age
 * mbti
 * isSmoking
 * department
 * lifeCycle
 * sleepingPattern
 * cleaning
 * nationality
 * armyService
 * introduction
 * */

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "myPersonality")
public class MyPersonality {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "myPersonalityId")
    private Long myPersonalityId;

    @Column(name = "age")
    private Integer age;

    @Column(name = "mbti")
    private String mbti;

    @Column(name = "isSmoking")
    private Integer isSmoking;

    @Column(name = "department")
    private String department;

    @Column(name = "lifeCycle")
    private Integer lifeCycle;

    @Column(name = "sleepingPattern")
    private String sleepingPattern;

    @Column(name = "cleaning")
    private Integer cleaning;

    @Column(name = "nationality")
    private Integer nationality;

    @Column(name = "armyService")
    private Integer armyService;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "user_id")
    private Long userId;
}
