package OMNM.OMNMBACKEND.connection.repository;

import OMNM.OMNMBACKEND.connection.domain.Connection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewConnectionRepository extends JpaRepository<Connection, Long> {
    Page<Connection> findAllByToIdOrderByConnectionIdDesc(Long toId, Pageable pageable);
    Page<Connection> findAllByFromIdOrderByConnectionIdDesc(Long fromId, Pageable pageable);
}
