package OMNM.OMNMBACKEND.user.service;

import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(User user){
        userRepository.save(user);
    }

    public Boolean idDuplicateCheckService(String id){
        Optional<User> findUser = userRepository.findByLoginIdAndStatus(id,1);
        return findUser.isEmpty();
    }

    public Optional<User> checkLoginId(String id){
        return userRepository.findByLoginIdAndStatus(id, 1);
    }

    public User getUserEntity(Long userId){
        Optional<User> user = userRepository.findByUserIdAndStatus(userId, 1);
        return user.orElse(null);
    }
}
