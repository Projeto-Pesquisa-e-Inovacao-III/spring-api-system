package com.spring.ApiSystem.mapper;
import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import com.spring.ApiSystem.dto.usuario.request.LoginUsuarioDTO;
import com.spring.ApiSystem.model.Aluno;
import com.spring.ApiSystem.model.Endereco;
import com.spring.ApiSystem.model.Usuario;
import com.spring.ApiSystem.dto.usuario.request.CadastroUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.request.EditarUsuarioDTO;
import com.spring.ApiSystem.dto.usuario.response.ResUsuarioDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    ResUsuarioDTO toDto(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(CadastroUsuarioDTO usuarioDTO);

    Usuario toEntity(ResUsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(EditarUsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(LoginUsuarioDTO usuarioDTO);

    Aluno toEntityAluno(CadastroUsuarioDTO usuarioDTO);

    @Mapping(target = "senha", ignore = true)
    void atualizarUsuarioFromEditarUsuarioDto(EditarUsuarioDTO dto,
                                  @MappingTarget Usuario usuario);

}
