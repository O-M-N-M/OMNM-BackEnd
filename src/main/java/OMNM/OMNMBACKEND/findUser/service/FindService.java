package OMNM.OMNMBACKEND.findUser.service;

import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FindService {

    private final UserRepository userRepository;

    public Optional<User> findIdService(String name){
        return userRepository.findByNameAndStatus(name, 1);
    }

    public Optional<User> findEmailService(String email){
        return userRepository.findByEmailAndStatus(email, 1);
    }
}
