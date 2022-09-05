package OMNM.OMNMBACKEND.user.repository;

import OMNM.OMNMBACKEND.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginIdAndStatus(String loginId, Integer status);
    Optional<User> findByNameAndStatus(String name, Integer status);
    Optional<User> findByEmailAndStatus(String email, Integer status);
    Optional<User> findByUserIdAndStatus(Long userId, Integer status);
    List<User> findAllByStatus(Integer status);
    List<User> findAllByIsMatchedAndStatus(Integer isMatched, Integer status);
}
