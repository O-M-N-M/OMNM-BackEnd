package OMNM.OMNMBACKEND.home.service;

import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HomeService {

    private final UserRepository userRepository;

    public Integer getMemberCount(){
        return userRepository.findAllByStatus(1).size();
    }

    public Integer getMatchedCount(){
        return userRepository.findAllByIsMatchedAndStatus(1, 1).size();
    }
}
