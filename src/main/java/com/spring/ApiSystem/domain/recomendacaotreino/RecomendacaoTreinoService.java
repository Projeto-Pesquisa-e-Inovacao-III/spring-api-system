package com.spring.ApiSystem.domain.recomendacaotreino;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.agendamento.AgendamentoService;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.anamnese.AnamneseService;
import com.spring.ApiSystem.domain.anamnese.dto.response.ResBuscarAnamneseDTO;
import com.spring.ApiSystem.domain.recomendacaotreino.exception.PrecisaAgendamentoException;
import com.spring.ApiSystem.domain.recomendacaotreino.exception.RecomendacaoTreinoExistenteException;
import com.spring.ApiSystem.domain.recomendacaotreino.exception.RecomendacaoTreinoInexistenteException;
import com.spring.ApiSystem.domain.recomendacaotreino.exception.StatusAgendamentoInvalido;
import com.spring.ApiSystem.domain.recomendacaotreino.mapper.TreinoLlmMapper;
import com.spring.ApiSystem.domain.resumoagendamento.ResumoAgendamentoService;
import com.spring.ApiSystem.domain.resumoagendamento.dto.res.ResResumoAgendamentoAlunoDTO;
import com.spring.ApiSystem.shared.dto.PaginaCursor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RecomendacaoTreinoService {
    private final AgendamentoService agendamentoService;
    private final AnamneseService anamneseService;
    private final ResumoAgendamentoService resumoAgendamentoService;
    private final TreinoLlmMapper treinoLlmMapper;
    private final ChatClient.Builder chatClientBuilder;
    private final RecomendacaoTreinoRepository recomendacaoTreinoRepository;

    public RecomendacaoTreinoService(AgendamentoService agendamentoService,
                                     AnamneseService anamneseService,
                                     ResumoAgendamentoService resumoAgendamentoService,
                                     TreinoLlmMapper treinoLlmMapper,
                                     ChatClient.Builder chatClientBuilder,
                                     RecomendacaoTreinoRepository recomendacaoTreinoRepository) {
        this.agendamentoService = agendamentoService;
        this.anamneseService = anamneseService;
        this.resumoAgendamentoService = resumoAgendamentoService;
        this.chatClientBuilder = chatClientBuilder;
        this.treinoLlmMapper = treinoLlmMapper;
        this.recomendacaoTreinoRepository = recomendacaoTreinoRepository;
    }

    private String gerarRecomendacaoTreino(Agendamento agendamento) {
        validarStatusAprovado(agendamento);

        ResBuscarAnamneseDTO anamneseDto = anamneseService.buscarAnamneseDoAluno(agendamento.getAluno().getId());
        PaginaCursor<ResResumoAgendamentoAlunoDTO> resumoAgendamento = carregarResumoAgendamento(agendamento.getId());

        ChatClient chatClient = chatClientBuilder.build();

        PromptTemplate promptSystem = carregarPromptTemplate("prompts/recomendacao_treino_system.st");
        PromptTemplate promptUser = carregarPromptTemplate("prompts/recomendacao_treino.st");

        Prompt promptUserFinal = montarPromptUser(
                promptUser,
                agendamento,
                anamneseDto,
                resumoAgendamento
        );

        return chatClient.prompt()
                .system(promptSystem.getTemplate())
                .user(promptUserFinal.getContents())
                .call().content();

    }

    public String cadastrarRecomendacaoTreino(Long agendamentoId) {
        validarRecomendacaoTreinoExistente(agendamentoId);
        Agendamento agendamento = agendamentoService.buscarAgendamentoPorId(agendamentoId);
        String treino = gerarRecomendacaoTreino(agendamento);
        System.out.println(treino);

        RecomendacaoTreino recomendacaoTreino = new RecomendacaoTreino(
                null,
                agendamento,
                LocalDate.now(),
                treino
        );

        recomendacaoTreinoRepository.save(recomendacaoTreino);

        return treino;
    }

    public String consultarRecomendacaoTreino(Long agendamentoId){
        Optional<RecomendacaoTreino> recomendacaoTreino = recomendacaoTreinoRepository
                .findById(agendamentoId);

        if(recomendacaoTreino.isEmpty()){
            throw new RecomendacaoTreinoInexistenteException(agendamentoId);
        }

        return recomendacaoTreino.get().getTreino();
    }

    // Métodos auxiliares

    private void validarRecomendacaoTreinoExistente(Long recomendacaoTreinoId){
        Optional<RecomendacaoTreino> recomendacaoTreino =
                recomendacaoTreinoRepository.findById(recomendacaoTreinoId);

        if(recomendacaoTreino.isPresent()){
            throw new RecomendacaoTreinoExistenteException();
        }
    }

    private void validarStatusAprovado(Agendamento agendamento) {
        if (agendamento.getStatus() != AgendamentoStatus.APROVADO) {
            throw new StatusAgendamentoInvalido(AgendamentoStatus.APROVADO);
        }
    }

    private PaginaCursor<ResResumoAgendamentoAlunoDTO> carregarResumoAgendamento(
            Long agendamentoId
    ) {
        PaginaCursor<ResResumoAgendamentoAlunoDTO> resumoAgendamento =
                resumoAgendamentoService.consultarResumoAluno(
                        agendamentoId, null, 5
                );

        if (resumoAgendamento.conteudo().isEmpty()) {
            throw new PrecisaAgendamentoException();
        }

        return resumoAgendamento;
    }

    private PromptTemplate carregarPromptTemplate(String caminho) {
        return new PromptTemplate(new ClassPathResource(caminho));
    }

    private Prompt montarPromptUser(
            PromptTemplate promptUser,
            Agendamento agendamento,
            ResBuscarAnamneseDTO anamneseDto,
            PaginaCursor<ResResumoAgendamentoAlunoDTO> resumoAgendamento
    ) {
        String anamnese = treinoLlmMapper.formatarAnamnese(anamneseDto);
        String historico = treinoLlmMapper.formatarHistorico(resumoAgendamento.conteudo());
        String grupos = treinoLlmMapper.formatarGrupos(
                resumoAgendamento.conteudo()
                        .getFirst()
                        .grupoMuscular()
        );

        return promptUser.create(Map.of(
                "tipoAgendamento", agendamento.getStatus(),
                "anamnese", anamnese,
                "resumoAgendamentos", historico,
                "todosGruposMusculares", treinoLlmMapper.formatarGrupos(
                        List.of(resumoAgendamentoService.listarGruposMusculares())
                ),
                "gruposMusculares", grupos
        ));
    }
}
