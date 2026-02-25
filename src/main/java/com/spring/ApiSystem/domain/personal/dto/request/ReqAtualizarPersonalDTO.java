package com.spring.ApiSystem.domain.personal.dto.request;


import com.spring.ApiSystem.domain.telefone.dto.request.ReqAtualizarTelefoneDTO;
import jakarta.validation.constraints.*;

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

        @NotNull
        @NotEmpty
        List<ReqAtualizarTelefoneDTO> telefones
) {
}
