package OMNM.OMNMBACKEND.yourPersonality.repository;

import OMNM.OMNMBACKEND.yourPersonality.domain.YourPersonality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YourPersonalityRepository extends JpaRepository<YourPersonality, Long> {
}
