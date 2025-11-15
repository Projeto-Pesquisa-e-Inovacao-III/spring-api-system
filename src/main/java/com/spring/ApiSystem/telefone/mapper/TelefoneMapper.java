package com.spring.ApiSystem.telefone.mapper;

import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.request.ReqAtualizarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.response.ResAtualizarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.response.ResCadastrarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.response.ResListarTelefonesPorIdDoUsuario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TelefoneMapper {
    Telefone toEntity(ReqCadastrarTelefoneDTO telefoneDTO);
    Telefone toEntity(ReqAtualizarTelefoneDTO telefoneDTO);

    ResCadastrarTelefoneDTO toDtoCasdastrarTelefone(Telefone telefone);
    ResAtualizarTelefoneDTO toDtoAtualizarTelefone(Telefone telefone);
    List<ResListarTelefonesPorIdDoUsuario>toDtoListarTelefonesPorIdDoUsuario(List<Telefone> telefones);
}
