package com.spring.ApiSystem.domain.aluno.dto.request;

import com.spring.ApiSystem.domain.telefone.dto.request.ReqCadastrarTelefoneDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record ReqCadastroAlunoDTO(
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

    @NotBlank(message = "O cpf é obrigatório")
    String cpf,

    ReqCadastrarTelefoneDTO telefone

) {}