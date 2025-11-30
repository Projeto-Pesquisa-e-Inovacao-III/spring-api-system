package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.aluno.AlunoRepository;
import com.spring.ApiSystem.aluno.AlunoService;
import com.spring.ApiSystem.produtocontratado.dto.response.*;
import com.spring.ApiSystem.produtocontratado.exception.*;
import com.spring.ApiSystem.produtocontratado.mapper.ProdutoContratadoMapper;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicaoService;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
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

    public ProdutoContratadoService(ProdutoContratadoRepository produtoContratadoRepository,
                                    ProdutoContratadoMapper produtoContratadoMapper,
                                    ProdutoExibicaoService produtoExibicaoService,
                                    AlunoService alunoService,
                                    AlunoRepository alunoRepository) {
        this.produtoContratadoRepository = produtoContratadoRepository;
        this.produtoExibicaoService = produtoExibicaoService;
        this.alunoService = alunoService;
        this.produtoContratadoMapper = produtoContratadoMapper;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public ResProdutoContratadoDto criarProdutoContratado(Long idProdutoExibicao, String email){
        Aluno aluno = alunoService.buscarPorEmail(email);
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
        return produtoContratadoMapper.toDto(produtoContratado);
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
                .findFirstByAlunoIdAndTipoAulaWithSaldoGreaterThanOne(
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

    public ResProdutoContratadoDto buscarPorIdAlunoEmail(Long id, String email){
        ProdutoContratado produtoContratado = produtoContratadoRepository.findByIdAndAlunoEmail(id, email)
                .orElseThrow(() -> new ProdutoContratadoAlunoNaoTemEsseProdutoException(id));
        return produtoContratadoMapper.toDto(produtoContratado);
    }


    public List<ResProdutoContratadoDto> listarPorAluno(String email, Pageable pageable){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findByAlunoEmail(email, pageable);
        if (produtosContratados.isEmpty()) {
            throw new ProdutoContratadoPorAlunoNaoExisteException();
        }
        return produtoContratadoMapper.toListDto(produtosContratados);
    }

    public ResProdutoContratadoAtivoDto buscarProdutoContratadoAtivo(String email){
        return produtoContratadoMapper.toResProdutoContratadoAtivoDto(
                produtoContratadoRepository.buscarProdutoContratadoAtivo(email)
                        .orElseThrow(SemPlanoAtivoException::new));
    }

    public ResSaldoDto buscarTotalSaldoAulaPorTipo(TipoAula tipoAula){
        return new ResSaldoDto(
                tipoAula, produtoContratadoRepository.totalSaldoAtivoPorTipo(tipoAula));
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

    public ResQuantidadePercentualAlunosExpiradosDto contarEPercentualAlunosExpirados() {
        Integer quantidadeExpirados = produtoContratadoRepository.countAlunosComPlanosExpirados(LocalDate.now());
        long totalAlunos = alunoRepository.count();

        double percentual = 0.0;
        if (totalAlunos > 0) {
            percentual = (quantidadeExpirados.doubleValue() / (double) totalAlunos) * 100.0;
        }

        return new ResQuantidadePercentualAlunosExpiradosDto(quantidadeExpirados, percentual);
    }

}
