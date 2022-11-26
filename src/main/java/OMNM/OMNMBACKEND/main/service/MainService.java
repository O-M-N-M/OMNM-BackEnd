package OMNM.OMNMBACKEND.main.service;

import OMNM.OMNMBACKEND.connection.domain.Connection;
import OMNM.OMNMBACKEND.connection.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {

    private final ConnectionRepository connectionRepository;

    public boolean isProposedPerson(Long userId, Long matchingId){
        if(connectionRepository.findByFromIdAndToId(userId, matchingId).isPresent()){
            return true;
        }
        else{
            Connection connection = new Connection();
            connection.setFromId(userId);
            connection.setToId(matchingId);
            connection.setCreatedTime(LocalDateTime.now());
            connectionRepository.save(connection);
            return false;
        }
    }
}
