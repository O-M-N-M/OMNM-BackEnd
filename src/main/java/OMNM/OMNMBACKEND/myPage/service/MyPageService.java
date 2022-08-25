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

    public void changeProfileUrl(Long userId, String profileUrl, String kakaoId, Integer dormitory){
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> value.setProfileUrl(profileUrl));
        user.ifPresent(value -> value.setKakaoId(kakaoId));
        user.ifPresent(value -> value.setDormitory(dormitory));
    }

    public MyPageUserDto viewUserDto(Long userId){
        MyPageUserDto myPageUserDto = new MyPageUserDto();
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            myPageUserDto.setName(user.get().getName());
            myPageUserDto.setProfileUrl(user.get().getProfileUrl());
            myPageUserDto.setDormitory(user.get().getDormitory());
            myPageUserDto.setKakaoId(user.get().getKakaoId());
            return myPageUserDto;
        }
        return null;
    }

    public void changeMatchingStatus(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            if (user.get().getIsMatched() == 0){
                user.get().setIsMatched(1);
            }
            else{
                user.get().setIsMatched(0);
            }
        }
    }
}
