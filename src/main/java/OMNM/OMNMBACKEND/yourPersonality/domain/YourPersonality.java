package OMNM.OMNMBACKEND.yourPersonality.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "yourPersonality")
public class YourPersonality {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yourPersonalityId")
    private Long yourPersonalityId;

    @Column(name = "age")
    private String age;

    @Column(name = "mbti")
    private String mbti;

    @Column(name = "isSmoking")
    private Integer isSmoking;

    @Column(name = "department")
    private Integer department;

    @Column(name = "lifeCycle")
    private Integer lifeCycle;

    @Column(name = "cleaning")
    private Integer cleaning;

    @Column(name = "nationality")
    private Integer nationality;

    @Column(name = "armyService")
    private Integer armyService;
}