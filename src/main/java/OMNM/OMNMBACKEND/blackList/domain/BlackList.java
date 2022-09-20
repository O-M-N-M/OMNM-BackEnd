package OMNM.OMNMBACKEND.blackList.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "blackList")
public class BlackList {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blackListId")
    private Long blackListId;

    @Column(name = "token")
    private String token;
}
