package com.spring.ApiSystem.domain.aluno.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.spring.ApiSystem.domain.telefone.dto.request.ReqAtualizarTelefoneDTO;
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


        @NotBlank(message = "O email não pode ficar vazio ou nulo")
        @Email(message = "Email deve ter formato válido")
        String email,

        List<ReqAtualizarTelefoneDTO> telefones
) {}
