package OMNM.OMNMBACKEND.blackList.service;

import OMNM.OMNMBACKEND.blackList.domain.BlackList;
import OMNM.OMNMBACKEND.blackList.repository.BlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BlackListService {

    private final BlackListRepository blackListRepository;

    public BlackList getBlackListEntity(String token){
        Optional<BlackList> blackList = blackListRepository.findByToken(token);
        return blackList.orElse(null);
    }
}
