package OMNM.OMNMBACKEND.user.service;

import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

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

    public User getUserEntityByLoginId(String loginId){
        Optional<User> user = userRepository.findByLoginIdAndStatus(loginId, 1);
        return user.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLoginIdAndStatus(username, 1);
        if (user.isPresent()){
            return user.get();
        }
        else{
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }
}
