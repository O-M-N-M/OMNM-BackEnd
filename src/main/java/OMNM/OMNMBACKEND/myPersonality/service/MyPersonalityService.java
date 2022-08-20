package OMNM.OMNMBACKEND.myPersonality.service;

import OMNM.OMNMBACKEND.myPersonality.domain.MyPersonality;
import OMNM.OMNMBACKEND.myPersonality.repository.MyPersonalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPersonalityService {

    private final MyPersonalityRepository myPersonalityRepository;

    public void saveMyPersonality(MyPersonality myPersonality){
        myPersonalityRepository.save(myPersonality);
    }

    public MyPersonality findMyPersonality(Long myPersonalityId){
        Optional<MyPersonality> myPersonality = myPersonalityRepository.findById(myPersonalityId);
        return myPersonality.orElse(null);
    }
}
