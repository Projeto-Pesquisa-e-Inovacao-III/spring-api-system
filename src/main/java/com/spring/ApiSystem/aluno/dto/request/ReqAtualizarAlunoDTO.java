package com.spring.ApiSystem.aluno.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.request.ReqAtualizarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.telefone.dto.response.ResListarTelefonesPorIdDoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ReqAtualizarAlunoDTO(
        @NotBlank(message = "O nome não pode ficar vazio ou nulo")
        String nome,

        @NotBlank(message = "O sexo não pode ficar vazio ou nulo")
        String sexo,

        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        @NotBlank(message = "O email não pode ficar vazio ou nulo")
        @Email(message = "Email deve ter formato válido")
        String email,

        @NotBlank(message = "O CPF não pode ficar vazio ou nulo")
        String cpf,

        @NotBlank(message = "A senha atual não pode ficar vazia ou nula")
        @Size(min = 6, message = "A senha atual deve ter no mínimo 6 caracteres")
        String senha,

        @Size(min = 6, message = "A senha nova deve ter no mínimo 6 caracteres")
        String senhaNova,

        String caminhoFoto,

        List<ReqAtualizarTelefoneDTO> telefones
) {}
