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

    public Integer getJoinedUserCount(){
        List<User> userList = userRepository.findAll();
        return userList.size();
    }

    public Integer getUserCount(){
        List<User> userList = userRepository.findAll();
        int userCount = userList.size();

        for (User user : userList) {
            if (user.getStatus() == 0) {
                userCount -= 1;
            }
        }
        return userCount;
    }

    public Integer getMatchingCount(){
        int matchingCount = 0;
        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            if (user.getIsMatched() == 1) {
                matchingCount+=1;
            }
        }
        return matchingCount;
    }
}
