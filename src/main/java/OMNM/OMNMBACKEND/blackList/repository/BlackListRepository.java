package OMNM.OMNMBACKEND.blackList.repository;

import OMNM.OMNMBACKEND.blackList.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    Optional<BlackList> findByToken(String token);
}
