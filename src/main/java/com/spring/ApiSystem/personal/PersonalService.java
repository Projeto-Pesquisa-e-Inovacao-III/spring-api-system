package com.spring.ApiSystem.personal;

import com.spring.ApiSystem.personal.dto.response.BuscarPersonalPorIdDTO;
import com.spring.ApiSystem.personal.exception.PersonalNaoExisteExcpetion;
import org.springframework.stereotype.Service;

@Service
public class PersonalService {

    private final PersonalRepository personalRepository;

    public PersonalService(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    public BuscarPersonalPorIdDTO buscarPersonalPorId(Long id) {
    Personal  personal = findById(id);

        return new BuscarPersonalPorIdDTO(
                personal.getId(),
                personal.getNome(),
                personal.getSexo(),
                personal.getDataNascimento(),
                personal.getEmail(),
                personal.getCref(),
                personal.isAtivo()
        );
    }

    public Personal findById(Long id) {

        return personalRepository
                .findById(id)
                .orElseThrow(PersonalNaoExisteExcpetion::new);
    }
}
