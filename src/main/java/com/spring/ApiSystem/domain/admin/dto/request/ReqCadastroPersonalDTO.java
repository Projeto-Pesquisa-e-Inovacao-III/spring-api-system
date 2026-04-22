package com.spring.ApiSystem.domain.admin.dto.request;


import com.spring.ApiSystem.domain.telefone.dto.request.ReqCadastrarTelefoneDTO;
import jakarta.validation.constraints.*;

import java.util.Date;

public record ReqCadastroPersonalDTO(
    @NotBlank(message = "O nome é obrigatório")
    String nome,

    String sexo,

    @Past(message = "A data de nascimento deve estar no passado")
    Date dataNascimento,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    String email,

    @NotBlank
    String cref,

    ReqCadastrarTelefoneDTO telefone

) {}