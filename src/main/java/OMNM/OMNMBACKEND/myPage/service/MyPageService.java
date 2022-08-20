package OMNM.OMNMBACKEND.myPage.service;

import OMNM.OMNMBACKEND.myPage.dto.MyPageUserDto;
import OMNM.OMNMBACKEND.user.domain.User;
import OMNM.OMNMBACKEND.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    public void deleteUserAccount(Long userId){
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> value.setStatus(0));
    }

    public void resetUserPassword(Long userId, String password){
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> value.setPassword(password));
    }

    public void changeProfileUrl(Long userId, String profileUrl){
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> value.setProfileUrl(profileUrl));
    }

    public MyPageUserDto viewUserDto(Long userId){
        MyPageUserDto myPageUserDto = new MyPageUserDto();
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            myPageUserDto.setName(user.get().getName());
            myPageUserDto.setProfileUrl(user.get().getProfileUrl());
            return myPageUserDto;
        }
        return null;
    }
}
