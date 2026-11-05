package com.spring.ApiSystem.domain.recomendacaotreino.mapper;

import com.spring.ApiSystem.domain.anamnese.dto.CondicoesDTO;
import com.spring.ApiSystem.domain.anamnese.dto.response.ResBuscarAnamneseDTO;
import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResResumoAgendamentoAlunoDTO;
import com.spring.ApiSystem.domain.resumoagendamento.enums.GrupoMuscular;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TreinoLlmMapper {

    public String formatarAnamnese(ResBuscarAnamneseDTO a) {
        return """
                - Altura: %.1f cm
                - Peso: %.1f kg
                - Objetivo principal: %s
                - Rotina: %s
                - Nível de atividade: %s
                - Observação de saúde: %s
                - Condições:
                %s
                """.formatted(
                a.altura(),
                a.peso(),
                a.objectivoPrincipal(),
                a.rotina(),
                a.nivelDeAtividade(),
                a.observacaoSaude(),
                formatarCondicoes(a.condicoes())
        );
    }

    private String formatarCondicoes(List<CondicoesDTO> condicoes) {
        if (condicoes == null || condicoes.isEmpty()) {
            return "- Nenhuma";
        }

        return condicoes.stream()
                .map(c -> "- " + c.tipo() + ": " + c.situacao())
                .collect(Collectors.joining("\n"));
    }

    public String formatarHistorico(List<ResResumoAgendamentoAlunoDTO> lista) {
        if (lista == null || lista.isEmpty()) {
            return "Nenhum histórico disponível.";
        }

        return lista.stream()
                .map(this::formatarAgendamento)
                .collect(Collectors.joining("\n\n---\n\n"));
    }

    private String formatarAgendamento(ResResumoAgendamentoAlunoDTO a) {
        return """
                Data: %s
                Resumo do treino: %s
                Grupos musculares: %s
                """.formatted(
                a.agendamentoData(),
                a.resumo(),
                formatarGrupos(a.grupoMuscular())
        );
    }

    public String formatarGrupos(List<GrupoMuscular> grupos) {
        if (grupos == null || grupos.isEmpty()) {
            return "Não informado";
        }

        return grupos.stream()
                .map(Enum::toString)
                .collect(Collectors.joining(", "));
    }
}