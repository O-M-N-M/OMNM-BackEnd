package OMNM.OMNMBACKEND.main.service;

import OMNM.OMNMBACKEND.connection.domain.Connection;
import OMNM.OMNMBACKEND.connection.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {

    private final ConnectionRepository connectionRepository;

    public boolean isProposedPerson(Long userId, Long matchingId, String message){
        if(connectionRepository.findByFromIdAndToId(userId, matchingId).isPresent()){
            return true;
        }
        else{
            Connection connection = new Connection();
            connection.setFromId(userId);
            connection.setToId(matchingId);
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //MM.dd
            connection.setCreatedTime(time);
            connection.setMessage(message);
            connectionRepository.save(connection);
            return false;
        }
    }

    public Integer ageConverter(Integer age){
        if(age<23){
            return 0;
        }
        else if(age<=26){
            return 1;
        }
        else if(age<=29){
            return 2;
        }
        else{
            return 3;
        }
    }
}
