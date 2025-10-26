package com.spring.ApiSystem.service;

import com.spring.ApiSystem.dto.personal.response.BuscarPersonalPorIdDTO;
import com.spring.ApiSystem.exception.PersonalNaoExisteExcpetion;
import com.spring.ApiSystem.model.Personal;
import com.spring.ApiSystem.repository.PersonalRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonalService {

    private final PersonalRepository personalRepository;

    public PersonalService(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    public BuscarPersonalPorIdDTO buscarPersonalPorId(Integer id) {
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

    public Personal findById(Integer id) {

        return personalRepository
                .findById(id)
                .orElseThrow(PersonalNaoExisteExcpetion::new);
    }
}
