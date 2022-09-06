package OMNM.OMNMBACKEND.yourPersonality.service;

import OMNM.OMNMBACKEND.yourPersonality.domain.YourPersonality;
import OMNM.OMNMBACKEND.yourPersonality.repository.YourPersonalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class YourPersonalityService {

    /*
        private String age;
        private String mbti;
        private Integer isSmoking;
        private Integer department;
        private Integer lifeCycle;
        private Integer cleaning;
        private Integer nationality;
        private Integer armyService;
         */

    private final YourPersonalityRepository yourPersonalityRepository;

    public void saveYourPersonality(YourPersonality yourPersonality) {
        yourPersonalityRepository.save(yourPersonality);
    }

    public YourPersonality findYourPersonality(Long yourPersonalityId){
        Optional<YourPersonality> yourPersonality = yourPersonalityRepository.findById(yourPersonalityId);
        return yourPersonality.orElse(null);
    }
}
