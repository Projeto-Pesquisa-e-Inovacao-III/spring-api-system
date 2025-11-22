
package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.agendamento.mapper.AgendamentoMapper;
import com.spring.ApiSystem.historicoagendamento.HistoricoAgendamentoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class AgendamentoScheduler {

    private static final Logger log = LoggerFactory.getLogger(AgendamentoScheduler.class);
    private final AgendamentoRepository agendamentoRepository;
    private final HistoricoAgendamentoService historicoAgendamentoService;
    private final AgendamentoMapper agendamentoMapper;
    private final AtomicBoolean running = new AtomicBoolean(false);

    private static final  int TAMANHO_BATCH = 500;
    private static final long INTERVALO_DA_BATCH = 20L;
    private static final long TEMPO_DE_ESPERA_AGENDAMENTO = 60000L;

    public AgendamentoScheduler(AgendamentoRepository agendamentoRepository,
                                HistoricoAgendamentoService historicoAgendamentoService,
                                AgendamentoMapper agendamentoMapper) {
        this.agendamentoRepository = agendamentoRepository;
        this.historicoAgendamentoService = historicoAgendamentoService;
        this.agendamentoMapper = agendamentoMapper;
    }

    @Scheduled(fixedDelayString = "${agendamento.scheduler.delay.millis:" + TEMPO_DE_ESPERA_AGENDAMENTO + "}")
    public void mudarParaPendentePersonalConcluir() {
        if (!running.compareAndSet(false, true)) {
            log.debug("Execução anterior em andamento — pulando.");
            return;
        }
        try {
            LocalDateTime agora = LocalDateTime.now();

            while (true) {
                List<Long> ids = agendamentoRepository.buscarIdsPorDataAnteriorEStatus(
                        agora, AgendamentoStatus.APROVADO, PageRequest.of(0, TAMANHO_BATCH)
                );

                if (ids == null || ids.isEmpty()) {
                    break;
                }

                try {
                    int atualizados = agendamentoRepository.atualizarStatusPorIds(
                            AgendamentoStatus.PENDENTE_PERSONAL_CONCLUIR, ids
                    );
                    log.info("Atualizados {} agendamento(s) para PENDENTE_PERSONAL_CONCLUIR", atualizados);

                    Iterable<Agendamento> agendamentos = agendamentoRepository.findAllById(ids);
                    for (Agendamento ag : agendamentos) {
                        try {
                            historicoAgendamentoService.cadastrar(
                                    agendamentoMapper.toReqCriarHistoricoAgendamentoDTO(ag),
                                    ag
                            );
                        } catch (Exception he) {
                            log.error("Falha ao cadastrar histórico para agendamento id={}", ag.getId(), he);
                        }
                    }
                } catch (Exception e) {
                    log.error("Erro ao atualizar lote de agendamentos", e);
                }

                try {
                    Thread.sleep(INTERVALO_DA_BATCH);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Erro no agendamento", e);
        } finally {
            running.set(false);
        }
    }

}
