package com.spring.ApiSystem.personal.mapper;

import com.spring.ApiSystem.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.personal.Personal;
import com.spring.ApiSystem.personal.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResBuscarPersonalPorIdDTO;
import com.spring.ApiSystem.personal.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.response.ResListarTelefonesPorIdDoUsuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonalMapper {

    @Mapping(target = "tipo", constant = "PERSONAL")
    Personal toEntity(ReqCadastroPersonalDTO reqCadastroPersonalDTO);

    ResCadastrarPersonalDTO toDtoCadastrarPersonal(Personal personal);
    ResBuscarPersonalPorIdDTO toDtoBuscarPersonalPorIdDTO(Personal personal);
    default ResListarTelefonesPorIdDoUsuario telefoneToDto(Telefone telefone) {
        if (telefone == null) return null;
        return new ResListarTelefonesPorIdDoUsuario(
                telefone.getDdd(),
                telefone.getNumero(),
                "FIXO" // ou telefone.getTipo() se existir
        );
    }
}
