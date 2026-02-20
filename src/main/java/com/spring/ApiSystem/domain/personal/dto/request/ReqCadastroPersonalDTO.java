package com.spring.ApiSystem.personal.dto.request;

import com.spring.ApiSystem.telefone.dto.request.ReqCadastrarTelefoneDTO;
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

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha,

    @NotBlank
    String cref,
    ReqCadastrarTelefoneDTO telefone,

    @Min(value = 15, message = "O intervalo deve ser de no mínimo 15 minutos")
    @Max(value = 60, message = "O intervalo deve ser de no máximo 60 minutos")
    Integer bufferMinutos

) {}