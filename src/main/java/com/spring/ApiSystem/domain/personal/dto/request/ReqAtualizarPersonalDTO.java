package com.spring.ApiSystem.personal.dto.request;

import com.spring.ApiSystem.telefone.Telefone;
import com.spring.ApiSystem.telefone.dto.request.ReqAtualizarTelefoneDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ReqAtualizarPersonalDTO(
        @NotBlank(message = "O nome não pode ficar vazio ou nulo")
        String nome,

        @NotBlank(message = "O sexo não pode ficar vazio ou nulo")
        String sexo,

        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        @NotBlank(message = "O email não pode ficar vazio ou nulo")
        @Email(message = "Email deve ter formato válido")
        String email,

        String caminhoFoto,

        List<ReqAtualizarTelefoneDTO> telefones
) {
}
