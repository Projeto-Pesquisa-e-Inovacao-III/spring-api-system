package com.spring.ApiSystem.domain.produtocontratado;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoRepository;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.produtocontratado.dto.response.*;
import com.spring.ApiSystem.domain.produtocontratado.events.ProdutoContratadoEventPublisher;
import com.spring.ApiSystem.domain.produtocontratado.exception.*;

import com.spring.ApiSystem.domain.produtocontratado.mapper.ProdutoContratadoMapper;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicaoService;

import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.external.comprar.exception.AlunoJaTemProdutoContratado;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProdutoContratadoService {

    private final ProdutoContratadoRepository produtoContratadoRepository;
    private final ProdutoContratadoMapper produtoContratadoMapper;
    private final ProdutoExibicaoService produtoExibicaoService;
    private final AlunoService alunoService;
    private final AlunoRepository alunoRepository;
    private final ProdutoContratadoEventPublisher produtoContratadoEventPublisher;
    private final JpaUserDetailsService jpaUserDetailsService;

    public ProdutoContratadoService(ProdutoContratadoRepository produtoContratadoRepository, ProdutoContratadoMapper produtoContratadoMapper, ProdutoExibicaoService produtoExibicaoService, AlunoService alunoService, AlunoRepository alunoRepository, ProdutoContratadoEventPublisher produtoContratadoEventPublisher, JpaUserDetailsService jpaUserDetailsService) {
        this.produtoContratadoRepository = produtoContratadoRepository;
        this.produtoContratadoMapper = produtoContratadoMapper;
        this.produtoExibicaoService = produtoExibicaoService;
        this.alunoService = alunoService;
        this.alunoRepository = alunoRepository;
        this.produtoContratadoEventPublisher = produtoContratadoEventPublisher;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    public void checkProdutoContratadoPacoteAtivo(Long idProdutoExibicao, Aluno aluno) {
        if (temProdutoContratadoAtivo(aluno) &&
                produtoExibicaoService.buscarPorId(idProdutoExibicao).getTipoProduto() == TipoProduto.PACOTE) {
            throw new AlunoJaTemProdutoContratado();
        }
    }


    @Transactional
    public ResProdutoContratadoDto criarProdutoContratadoPeloAluno(Long idProdutoExibicao, Aluno aluno){
        checkProdutoContratadoPacoteAtivo(idProdutoExibicao, aluno);

        ProdutoExibicao produtoExibicao = produtoExibicaoService.buscarPorId(idProdutoExibicao);

        ProdutoContratado produtoContratado = new ProdutoContratado(
                null,
                true,
                LocalDate.now(),
                LocalDate.now().plusMonths(produtoExibicao.getDuracaoMes()),
                produtoExibicao.getQuantidadeAula(),
                aluno,
                produtoExibicao
        );

        produtoContratadoRepository.save(produtoContratado);

        produtoContratadoEventPublisher.publishProdutoContratadoCreatedEvent(produtoContratado);

        return produtoContratadoMapper.toDto(produtoContratado);
    }

    @Transactional
    public ResProdutoContratadoDto criarPordutoContratadoDoAlunoAtual(Long idProdutoExibicao){
        return criarProdutoContratadoPeloAluno(idProdutoExibicao,jpaUserDetailsService.getCurrentAluno());
    }

    @Transactional
    public ResProdutoContratadoDto criarProdutoContratadoPeloIdAluno(Long idProdutoExibicao, Long idAluno){
        Aluno aluno = alunoService.buscarPorId(idAluno);
        return criarProdutoContratadoPeloAluno(idProdutoExibicao, aluno);
    }

    @Transactional
    public void desativarProdutoContratado(Long id){
        ProdutoContratado produtoContratado = buscarPorId(id);
        produtoContratado.setSituacao(false);
        produtoContratadoRepository.save(produtoContratado);
    }

    @Transactional
    public ResIncrementarSaldoDTO incrementar(Long agendamentoId) {
        ProdutoContratado produtoContratado = produtoContratadoRepository.findByAgendamentoId(agendamentoId);
        if (produtoContratado == null) {
            throw new ProdutoContratadoNaoExisteException();
        }

        if (!produtoElegivel(produtoContratado)) {
            return new ResIncrementarSaldoDTO(
                    false,
                    "O plano está inativo ou expirado. O saldo não será devolvido.",
                    produtoContratado.getSaldoAula()
            );
        }

        produtoContratado.setSaldoAula(produtoContratado.getSaldoAula() + 1);
        produtoContratadoRepository.save(produtoContratado);

        return new ResIncrementarSaldoDTO(
                true,
                "Saldo devolvido com sucesso.",
                produtoContratado.getSaldoAula()
        );
    }


    @Transactional
    public Long decrementar(Long alunoId, TipoAula tipoAula) {
        ProdutoContratado produtoContratado = produtoContratadoRepository
                .findFirstByAlunoIdAndTipoAulaComSaldoMaiorQueZero(
                       alunoId, tipoAula
                ).orElseThrow(UsuarioSemTipoAulaException::new);

        produtoContratado.setSaldoAula(produtoContratado.getSaldoAula() - 1);
        produtoContratadoRepository.save(produtoContratado);
        return produtoContratado.getId();
    }

    @Transactional
    public ProdutoContratado buscarPorId(Long id){
        ProdutoContratado produtoContratado = produtoContratadoRepository.findByIdWithLock(id);
        if (produtoContratado == null) {
            throw new ProdutoContratadoPorIdNaoExisteException(id);
        }
        return produtoContratado;
    }

    public List<ResProdutoContratadoDto> listarProdutosContratados(){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findAll();
        if (produtosContratados.isEmpty()) {
            throw new ProdutoContratadoNaoExisteException();
        }
        return produtoContratadoMapper.toListDto(produtosContratados);
    }

    public List<ResProdutoContratadoDto> listarPorSituacao(Boolean situacao){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository
                .findBySituacao(situacao);
        if (produtosContratados.isEmpty()) {
            throw new ProdutoContratadoPorSituacaoNaoExisteException(situacao);
        }
        return produtoContratadoMapper.toListDto(produtosContratados);
    }

    @Transactional
    public ResBuscarProdutoContratadoPorIdDto listarPorIdDto(Long id){
        ProdutoContratado produtoContratado = buscarPorId(id);
        return produtoContratadoMapper.toBuscarProdutoContratadoPorIdDto(produtoContratado);
    }

    public ProdutoContratado buscarPorIdAndAluno(Long id){
        return produtoContratadoRepository.findByIdAndAluno(id, jpaUserDetailsService.getCurrentAluno())
                .orElseThrow(() -> new ProdutoContratadoAlunoNaoTemEsseProdutoException(id));
    }


    public List<ResProdutoContratadoDto> listarPorAluno(Pageable pageable, String nomeProduto, LocalDate dataInicio, LocalDate dataFim){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findByAlunoIdWithFilters(
                jpaUserDetailsService.getCurrentAluno(),
                nomeProduto,
                dataInicio,
                dataFim,
                pageable);
        return produtoContratadoMapper.toListDto(produtosContratados);
    }

    public ResProdutoContratadoAtivoDto buscarProdutoContratadoAtivo(String email){
        return produtoContratadoMapper.toResProdutoContratadoAtivoDto(
                produtoContratadoRepository.buscarProdutoContratadoAtivo(email)
                        .orElseThrow(SemPlanoAtivoException::new));
    }

    public boolean temProdutoContratadoAtivo(Aluno aluno){
        return produtoContratadoRepository.temProdutoContratadoPacoteAtivo(aluno);
    }

    public Integer getTotalTipoAula(Aluno usuario, TipoAula tipoAula){
        List<ProdutoContratado> produtos = produtoContratadoRepository.buscarProdutoContratadoAtivoPorAlunoETipoAula(usuario, tipoAula);

        return produtos.stream()
                .map(ProdutoContratado::getSaldoAula)
                .reduce(0, Integer::sum);
    }

    public ResBuscarSaldoPorTipoAulaDto buscarTotalSaldoAulaPorTipoEspecifico(TipoAula tipoAula){
        Aluno usuario = jpaUserDetailsService.getCurrentAluno();

        Integer total = getTotalTipoAula(usuario, tipoAula);

        return new ResBuscarSaldoPorTipoAulaDto(tipoAula, total);
    }

    public ResTotalTipoSaldosDto getSaldoFromAllTipoAula(){
        Aluno usuario = jpaUserDetailsService.getCurrentAluno();

        return new ResTotalTipoSaldosDto(
                getTotalTipoAula(usuario, TipoAula.PRESENCIAL),
                getTotalTipoAula(usuario, TipoAula.RESIDENCIAL),
                getTotalTipoAula(usuario, TipoAula.FUNCIONAL)
        );
    }

    private boolean produtoElegivel(ProdutoContratado produtoContratado) {
        return produtoContratado.getSituacao() &&
                produtoContratado.getDataExpiracao().isAfter(LocalDate.now());
    }

    public List<ResListarGanhoMensalDto> listarGanhosMensais(Integer quantidadeMeses){
        return produtoContratadoRepository.listarGanhosPorMesDeCompra(quantidadeMeses)
                .stream()
                .map(this::converterParaResListarGanhoMensalDto)
                .toList();
    }

    private ResListarGanhoMensalDto converterParaResListarGanhoMensalDto(Object[] row) {
        return new ResListarGanhoMensalDto(
                ((Number) row[0]).intValue(),
                ((Number) row[1]).intValue(),
                ((Number) row[2]).doubleValue()
        );
    }


    public Integer contarProdutosVendidosUltimosDias(Integer dias){
        LocalDate dataInicio = LocalDate.now().minusDays(dias);
        return produtoContratadoRepository.totalPlanosVendidosUltimosDias(dataInicio, LocalDate.now());
    }

    public ResQuantidadePercentualAlunosExpiradosDto contagemEPercentualAlunosExpirados() {
        Integer quantidadeExpirados = produtoContratadoRepository.countAlunosComPlanosExpirados(TipoProduto.PACOTE);
        long totalAlunos = alunoRepository.count();

        int qtd = (quantidadeExpirados != null) ? quantidadeExpirados : 0;
        double percentual = 0.0;
        if (totalAlunos > 0) {
            percentual = ((double) qtd / (double) totalAlunos) * 100.0;
        }

        return new ResQuantidadePercentualAlunosExpiradosDto(qtd, percentual);
    }



}
