package com.spring.ApiSystem.usuario.dto.request;

import com.spring.ApiSystem.usuario.enums.TipoUsuario;
import jakarta.validation.constraints.*;

import java.util.Date;

public record ReqCadastroUsuarioDTO(
    @NotBlank(message = "O nome é obrigatório")
    String nome,

    String sexo,

    @Past(message = "A data de nascimento deve estar no passado")
    Date dataNascimento,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    String email,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha,

    @NotBlank
    String cpf,

    @NotBlank(message = "O tipo de usuário é obrigatório")
    TipoUsuario tipo
) {}