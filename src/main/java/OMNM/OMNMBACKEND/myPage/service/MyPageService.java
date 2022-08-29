package OMNM.OMNMBACKEND.myPage.service;

import OMNM.OMNMBACKEND.myPage.dto.MyPageUserDto;
import OMNM.OMNMBACKEND.myPage.dto.ViewUserDto;
import OMNM.OMNMBACKEND.myPersonality.domain.MyPersonality;
import OMNM.OMNMBACKEND.myPersonality.repository.MyPersonalityRepository;
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
    private final MyPersonalityRepository myPersonalityRepository;

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

    /**
     * 프로필 사진
     * 이름
     * 나이
     * 입실정보
     * mbti
     * 학과
     * 생활패턴
     * 수면패턴
     * 청소빈도
     * 국적
     * 군복무
     * 자기소개
     * */

    public ViewUserDto setViewUserDto(Long userId){
        ViewUserDto viewUserDto = new ViewUserDto();
        Optional<User> user = userRepository.findById(userId);
        User userEntity = user.get();
        Long myPersonalityId = userEntity.getMyPersonalityId();
        Optional<MyPersonality> myPersonality = myPersonalityRepository.findById(myPersonalityId);
        MyPersonality myPersonalityEntity = myPersonality.get();
        viewUserDto.setProfileUrl(userEntity.getProfileUrl());
        viewUserDto.setName(userEntity.getName());
        viewUserDto.setAge(myPersonalityEntity.getAge());
        viewUserDto.setDormitory(userEntity.getDormitory());
        viewUserDto.setMbti(myPersonalityEntity.getMbti());
        viewUserDto.setDepartment(myPersonalityEntity.getDepartment());
        viewUserDto.setLifeCycle(myPersonalityEntity.getLifeCycle());
        viewUserDto.setSleepingPattern(myPersonalityEntity.getSleepingPattern());
        viewUserDto.setCleaning(myPersonalityEntity.getCleaning());
        viewUserDto.setNationality(myPersonalityEntity.getNationality());
        viewUserDto.setArmyService(myPersonalityEntity.getArmyService());
        viewUserDto.setIntroduction(myPersonalityEntity.getIntroduction());
        return viewUserDto;
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
