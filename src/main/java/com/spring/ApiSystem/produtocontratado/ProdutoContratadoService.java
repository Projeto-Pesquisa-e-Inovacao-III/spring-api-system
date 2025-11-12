package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.aluno.AlunoService;
import com.spring.ApiSystem.produtocontratado.dto.request.EditarProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.request.ReqOperacaoSaldoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResBuscarProdutoContratadoPorIdDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResIncrementarSaldoDTO;
import com.spring.ApiSystem.produtocontratado.exception.*;
import com.spring.ApiSystem.produtocontratado.mapper.ProdutoContratadoMapper;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public ProdutoContratadoService(ProdutoContratadoRepository produtoContratadoRepository,
                                    ProdutoContratadoMapper produtoContratadoMapper,
                                    ProdutoExibicaoService produtoExibicaoService,
                                    AlunoService alunoService) {
        this.produtoContratadoRepository = produtoContratadoRepository;
        this.produtoExibicaoService = produtoExibicaoService;
        this.alunoService = alunoService;
        this.produtoContratadoMapper = produtoContratadoMapper;
    }

    public ResProdutoContratadoDto criarProdutoContratado(Long idProdutoExibicao, Long idAluno){
        Aluno aluno = alunoService.buscarPorId(idAluno);
        ProdutoExibicao produtoExibicao = produtoExibicaoService.buscarPorId(idProdutoExibicao);

        ProdutoContratado produtoContratado = new ProdutoContratado(
                null,
                true,
                LocalDate.now(),
                LocalDate.now().plusMonths(produtoExibicao.getDuracaoMes()),
                Integer.parseInt(produtoExibicao.getQuantidadeAula()),
                aluno,
                produtoExibicao
        );

        produtoContratadoRepository.save(produtoContratado);
        return produtoContratadoMapper.toDto(produtoContratado);
    }

    @Transactional
    public ResProdutoContratadoDto atualizarProdutoContratado(EditarProdutoContratadoDto editarProdutoContratadoDto){
        ProdutoContratado produtoContratado = listarPorId(editarProdutoContratadoDto.id());

        atualizarAluno(produtoContratado, editarProdutoContratadoDto.alunoId());
        atualizarProdutoExibicao(produtoContratado, editarProdutoContratadoDto.produtoExibicaoId());

        produtoContratadoMapper.partialUpdate(editarProdutoContratadoDto, produtoContratado);
        produtoContratadoRepository.save(produtoContratado);

        return produtoContratadoMapper.toDto(produtoContratado);
    }

    @Transactional
    public void desativarProdutoContratado(Long id){
        ProdutoContratado produtoContratado = listarPorId(id);
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
    public Long decrementar(ReqOperacaoSaldoDto reqOperacaoSaldoDto) {
        ProdutoContratado produtoContratado = produtoContratadoRepository
                .findFirstByAlunoIdAndTipoAulaWithSaldoGreaterThanOne(
                        reqOperacaoSaldoDto.alunoId(),
                        reqOperacaoSaldoDto.tipoAula()
                ).orElseThrow(ProdutoContratadoNaoExisteException::new);

        produtoContratado.setSaldoAula(produtoContratado.getSaldoAula() - 1);
        produtoContratadoRepository.save(produtoContratado);
        return produtoContratado.getId();
    }

    public List<ResProdutoContratadoDto> listarProdutosContratados(){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findAll();
        if (produtosContratados.isEmpty()) {
            throw new ProdutoContratadoNaoExisteException();
        }
        return produtoContratadoMapper.toListDto(produtosContratados);
    }

    public List<ResProdutoContratadoDto> listarPorSituacao(Boolean situacao){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findBySituacao(situacao);
        if (produtosContratados.isEmpty()) {
            throw new ProdutoContratadoPorSituacaoNaoExisteException(situacao);
        }
        return produtoContratadoMapper.toListDto(produtosContratados);
    }

    @Transactional
    public ResBuscarProdutoContratadoPorIdDto listarPorIdDto(Long id){
        ProdutoContratado produtoContratado = listarPorId(id);
        return produtoContratadoMapper.toBuscarProdutoContratadoPorIdDto(produtoContratado);
    }

    @Transactional
    public ProdutoContratado listarPorId(Long id){
        ProdutoContratado produtoContratado = produtoContratadoRepository.findByIdWithLock(id);
        if (produtoContratado == null) {
            throw new ProdutoContratadoPorIdNaoExisteException(id);
        }
        return produtoContratado;
    }

    public List<ResProdutoContratadoDto> listarPorAluno(Long idAluno){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findByAlunoId(idAluno);
        if (produtosContratados.isEmpty()) {
            throw new ProdutoContratadoPorAlunoNaoExisteException(idAluno);
        }
        return produtoContratadoMapper.toListDto(produtosContratados);
    }

    private boolean produtoElegivel(ProdutoContratado produtoContratado) {
        return produtoContratado.getSituacao() &&
                produtoContratado.getDataExpiracao().isAfter(LocalDate.now());
    }

    private void validarProdutoAtivo(ProdutoContratado produtoContratado) {
        if (!produtoContratado.getSituacao() ||
                produtoContratado.getDataExpiracao().isBefore(LocalDate.now())) {
            throw new PlanoInativoException();
        }
    }

    private void atualizarAluno(ProdutoContratado produto, Long novoAlunoId) {
        if (!produto.getAluno().getId().equals(novoAlunoId)) {
            produto.setAluno(alunoService.buscarPorId(novoAlunoId));
        }
    }

    private void atualizarProdutoExibicao(ProdutoContratado produto, Long novoProdutoExibicaoId) {
        if (!produto.getProdutoExibicao().getId().equals(novoProdutoExibicaoId)) {
            produto.setProdutoExibicao(produtoExibicaoService.buscarPorId(novoProdutoExibicaoId));
        }
    }
}
