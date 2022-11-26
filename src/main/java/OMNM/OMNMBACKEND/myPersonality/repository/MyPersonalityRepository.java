package OMNM.OMNMBACKEND.myPersonality.repository;

import OMNM.OMNMBACKEND.myPersonality.domain.MyPersonality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyPersonalityRepository extends JpaRepository<MyPersonality, Long> {
}
