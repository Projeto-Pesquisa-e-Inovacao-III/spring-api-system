package com.spring.ApiSystem.domain.alunoExperimental.dto;

import com.spring.ApiSystem.domain.agendamento.dto.request.ReqCadastrarAgendamentoDTO;
import jakarta.validation.constraints.*;

import java.time.LocalDate;


public record ReqCriarAgendamentoExperimentalRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nomeAluno,

        String sexo,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        String emailAluno,
        @NotBlank(message = "O cpf é obrigatório")
        String cpfAluno,
        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate dataNascimento,
        @NotBlank(message = "DDD não pode estar vazio")
        @Pattern(regexp = "\\d{2}", message = "DDD deve conter 2 dígitos")
        String ddd,
        @NotBlank(message = "Número não pode estar vazio")
        @Pattern(regexp = "\\d{8,9}", message = "Número deve conter 8 ou 9 dígitos")
        String numero,
        @NotBlank(message = "País não pode estar vazio")
        String pais,
        ReqCadastrarAgendamentoDTO agendamentoDTO
) {
}