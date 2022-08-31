package OMNM.OMNMBACKEND.connection.repository;

import OMNM.OMNMBACKEND.connection.domain.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findAllByToId(Long toId);
}
