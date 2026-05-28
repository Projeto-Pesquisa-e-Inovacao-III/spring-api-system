package com.spring.ApiSystem.domain.recomendacaotreino;

import com.spring.ApiSystem.domain.agendamento.AgendamentoService;
import com.spring.ApiSystem.domain.agendamento.dto.response.detalhes.AgendamentoDetalheResponse;
import com.spring.ApiSystem.domain.agendamento.dto.response.detalhes.ResDetalhesAgendamentoPersonalDTO;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.anamnese.AnamneseService;
import com.spring.ApiSystem.domain.anamnese.dto.response.ResBuscarAnamneseDTO;
import com.spring.ApiSystem.domain.resumoagendamento.ResumoAgendamentoService;
import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResResumoAgendamentoAlunoDTO;
import com.spring.ApiSystem.shared.dto.PaginaCursor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RecomendacaoTreinoService {
    private final AgendamentoService agendamentoService;
    private final AnamneseService anamneseService;
    private final ResumoAgendamentoService resumoAgendamentoService;
    private final ChatClient.Builder chatClientBuilder;

    public RecomendacaoTreinoService(AgendamentoService agendamentoService,
                                     AnamneseService anamneseService,
                                     ResumoAgendamentoService resumoAgendamentoService,
                                     ChatClient.Builder chatClientBuilder) {
        this.agendamentoService = agendamentoService;
        this.anamneseService = anamneseService;
        this.resumoAgendamentoService = resumoAgendamentoService;
        this.chatClientBuilder = chatClientBuilder;
    }

    public String gerarRecomendacaoTreino(Long agendamentoId) {
        AgendamentoDetalheResponse agendamento = agendamentoService.buscarDadosDoAgendamentoPorId(agendamentoId);

        if(agendamento.status() != AgendamentoStatus.APROVADO){
            throw new RuntimeException("Status inválido");
        }

        ResDetalhesAgendamentoPersonalDTO detalhesPersonal = (ResDetalhesAgendamentoPersonalDTO) agendamento;

        ResBuscarAnamneseDTO anamnese = anamneseService.buscarAnamneseDoAluno(detalhesPersonal.aluno().id());

        PaginaCursor<ResResumoAgendamentoAlunoDTO> resumoAgendamento =
                resumoAgendamentoService.consultarResumoAluno(
                        detalhesPersonal.aluno().id(), null, 5
                );

        if(resumoAgendamento.conteudo().isEmpty()){
            throw new RuntimeException("Aluno precisa ao menos ter concluido um agendamento");
        }

        ChatClient chatClient = chatClientBuilder.build();
        return chatClient.prompt()
                .user(u -> u.text("Extraia as informações dos seguintes valores:\n " +
                                "Tipo de Agendamento: {tipoAgendamento}\n" +
                                "Anamnese: {anamnese}\n" +
                                "Resumos de agendamentos passados: {resumoAgendamentos}\n" +
                                "Grupos musculares tratados no agendamento anterior: {gruposMusculares}\n\n" +
                                "Após a coleta dos dados, você DEVE retornar apenas como saída a recomendação de treino ")
                        .param("tipoAgendamento", agendamento.tipoAula())
                        .param("anamnese", anamnese)
                        .param("resumoAgendamentos", resumoAgendamento.conteudo())
                        .param("gruposMusculares", resumoAgendamento.conteudo().getFirst().grupoMuscular()))
                .call().content();
    }
}
