package OMNM.OMNMBACKEND.validation.repository;

import OMNM.OMNMBACKEND.validation.domain.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationRepository extends JpaRepository<Validation, Long> {
    Optional<Validation> findByEmail(String email);
}
