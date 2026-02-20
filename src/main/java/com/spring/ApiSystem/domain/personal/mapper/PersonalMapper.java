package com.spring.ApiSystem.personal.mapper;

import com.spring.ApiSystem.personal.Personal;
import com.spring.ApiSystem.personal.dto.request.ReqAtualizarPersonalDTO;
import com.spring.ApiSystem.personal.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResAtualizarPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResBuscarPersonalPorIdDTO;
import com.spring.ApiSystem.personal.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.personal.dto.response.ResListarPersonaisDTO;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.response.ResListarTelefonesPorIdDoUsuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonalMapper {

    @Mapping(target = "tipo", constant = "PERSONAL")
    Personal toEntity(ReqCadastroPersonalDTO reqCadastroPersonalDTO);

    ResCadastrarPersonalDTO toDtoCadastrarPersonal(Personal personal);
    ResAtualizarPersonalDTO toDtoAtualizarPersonal(Personal personal);
    List<ResListarPersonaisDTO> toDtoListarPersonaisDTO(List<Personal> personal);
    ResBuscarPersonalPorIdDTO toDtoBuscarPersonalPorIdDTO(Personal personal);
    default ResListarTelefonesPorIdDoUsuario telefoneToDto(Telefone telefone) {
        if (telefone == null) return null;
        return new ResListarTelefonesPorIdDoUsuario(
                telefone.getId(),
                telefone.getDdd(),
                telefone.getNumero()
        );
    }

    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "telefones", ignore = true)
    void atualizarPersonalParaAtualizarPersonalDto(ReqAtualizarPersonalDTO dto,
                                              @MappingTarget Personal personal);

}
