package OMNM.OMNMBACKEND.connection.repository;

import OMNM.OMNMBACKEND.connection.domain.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findAllByToId(Long toId);
}
