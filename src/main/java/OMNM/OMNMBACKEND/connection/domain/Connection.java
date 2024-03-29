package OMNM.OMNMBACKEND.connection.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "connection")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_Id")
    private Long connectionId;

    @Column(name = "from_Id")
    private Long fromId;

    @Column(name = "to_Id")
    private Long toId;

    @Column(name = "message")
    private String message;

    @Column(name = "created_time")
    private String createdTime;
}
