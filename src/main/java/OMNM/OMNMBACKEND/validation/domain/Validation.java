package OMNM.OMNMBACKEND.validation.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "validation")
public class Validation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "validationId")
    private Long validationId;

    @Column(name = "email")
    private String email;

    @Column(name = "validationNumber")
    private Integer validationNumber;
}
