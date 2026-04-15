package com.spring.ApiSystem.domain.telefone.mapper;

import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.telefone.dto.request.ReqAtualizarTelefoneDTO;
import com.spring.ApiSystem.domain.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.domain.telefone.dto.response.*;


import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TelefoneMapper {
    Telefone toEntity(ReqCadastrarTelefoneDTO telefoneDTO);
    Telefone toEntity(ReqAtualizarTelefoneDTO telefoneDTO);
    List<Telefone> toEntityList(List<ReqCadastrarTelefoneDTO> telefonesDTO);

    ResBuscarSolicitacoesPorPersonalTelefoneDTO buscarSolicitacoesPorPersonalTelefone(Telefone endereco);
    ResBuscarSolicitacoesPorAlunoTelefoneDTO buscarSolicitacoesPorAlunoTelefone(Telefone endereco);

    ResCadastrarTelefoneDTO toDtoCasdastrarTelefone(Telefone telefone);
    ResAtualizarTelefoneDTO toDtoAtualizarTelefone(Telefone telefone);
    List<ResListarTelefonesPorIdDoUsuario>toDtoListarTelefonesPorIdDoUsuario(List<Telefone> telefones);
}
