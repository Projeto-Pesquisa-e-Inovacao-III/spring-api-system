package com.spring.ApiSystem.personal.mapper;

import com.spring.ApiSystem.personal.Personal;
import com.spring.ApiSystem.personal.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResCadastrarPersonalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonalMapper {

    @Mapping(target = "tipo", constant = "PERSONAL")
    Personal toEntity(ReqCadastroPersonalDTO reqCadastroPersonalDTO);

    ResCadastrarPersonalDTO toDtoCadastrarPersonal(Personal personal);
}
