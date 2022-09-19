package OMNM.OMNMBACKEND.validation.service;

import OMNM.OMNMBACKEND.validation.domain.Validation;
import OMNM.OMNMBACKEND.validation.repository.ValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ValidationService {

    private final ValidationRepository validationRepository;

    public Integer getValidationNumber(String email){
        Optional<Validation> validation = validationRepository.findByEmail(email);
        return validation.map(Validation::getValidationNumber).orElse(null);
    }
}
