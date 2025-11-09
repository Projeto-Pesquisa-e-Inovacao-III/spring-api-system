package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.aluno.AlunoService;
import com.spring.ApiSystem.produtocontratado.dto.request.EditarProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.request.ReqOperacaoSaldoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.BuscarProdutoContratadoPorIdDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResOperacaoSaldoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.exception.*;
import com.spring.ApiSystem.produtocontratado.mapper.ProdutoContratadoMapper;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
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

    public ProdutoContratadoDto criarProdutoContratado(Long idProdutoExibicao, Long idAluno){
        Aluno aluno = alunoService.buscarPorId(idAluno);
        ProdutoExibicao produtoExibicao = produtoExibicaoService.buscarPorId(idProdutoExibicao);

        ProdutoContratado produtoContratado = new ProdutoContratado(
          null, true, LocalDate.now(),
           LocalDate.now().plusMonths(produtoExibicao.getDuracaoMes()),
           Integer.parseInt(produtoExibicao.getQuantidadeAula()), aluno, produtoExibicao
        );

        produtoContratadoRepository.save(produtoContratado);

        return produtoContratadoMapper.toDto(produtoContratado);
    }

    public List<ProdutoContratadoDto> listarProdutosContratados(){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findAll();
        if (produtosContratados.isEmpty()) {
            throw new ProdutoContratadoNaoExisteException();
        }
        return produtoContratadoMapper.toListDto(produtosContratados);
    }

    public List<ProdutoContratadoDto> listarPorSituacao(Boolean situacao){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findBySituacao(situacao);
        if (produtosContratados.isEmpty()) {
            throw new ProdutoContratadoPorSituacaoNaoExisteException(situacao);
        }
        return produtoContratadoMapper.toListDto(produtosContratados);
    }

    @Transactional
    public BuscarProdutoContratadoPorIdDto listarPorIdDto(Long id){
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

    public List<ProdutoContratadoDto> listarPorAluno(Long idAluno){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findByAlunoId(idAluno);
        if (produtosContratados.isEmpty()) {
            throw new ProdutoContratadoPorAlunoNaoExisteException(idAluno);
        }
        return produtoContratadoMapper.toListDto(produtosContratados);
    }


    @Transactional
    public ProdutoContratadoDto atualizarProdutoContratado(EditarProdutoContratadoDto editarProdutoContratadoDto){
        ProdutoContratado produtoContratado = listarPorId(editarProdutoContratadoDto.id());

        if(produtoContratado != null) {
            if(!produtoContratado.getAluno().getId().equals(editarProdutoContratadoDto.alunoId())){
                produtoContratado.setAluno(alunoService.buscarPorId(editarProdutoContratadoDto.alunoId()));
            }

            if(!produtoContratado.getProdutoExibicao().getId().equals(editarProdutoContratadoDto.produtoExibicaoId())){
                produtoContratado.setProdutoExibicao(produtoExibicaoService.buscarPorId(editarProdutoContratadoDto.produtoExibicaoId()));
            }

            produtoContratadoMapper.partialUpdate(editarProdutoContratadoDto, produtoContratado);
            produtoContratadoRepository.save(produtoContratado);
        }

        return produtoContratadoMapper.toDto(produtoContratado);
    }

    @Transactional
    public void desativarProdutoContratado(Long id){
        ProdutoContratado produtoContratado = listarPorId(id);
        produtoContratado.setSituacao(false);
        produtoContratadoRepository.save(produtoContratado);
    }

    @Transactional
    public void incrementar(ReqOperacaoSaldoDto reqOperacaoSaldoDto){
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository
                .findByAlunoIdAndProdutoExibicaoTipoAula(
                    reqOperacaoSaldoDto.alunoId(),
                    reqOperacaoSaldoDto.tipoAula()
                );

        for (ProdutoContratado produtoContratado : produtosContratados) {
            if(temSaldo(produtoContratado.getId())){
                produtoContratado.setSaldoAula(produtoContratado.getSaldoAula() + 1);
                produtoContratadoRepository.save(produtoContratado);
                return;
            }
        }

        throw new PlanoInativoException();
    }

    @Transactional
    public void decrementar(ReqOperacaoSaldoDto reqOperacaoSaldoDto) {
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository
                .findByAlunoIdAndProdutoExibicaoTipoAula(
                        reqOperacaoSaldoDto.alunoId(),
                        reqOperacaoSaldoDto.tipoAula()
                );

        for (ProdutoContratado produtoContratado : produtosContratados) {
            if(temSaldo(produtoContratado.getId())){
                produtoContratado.setSaldoAula(produtoContratado.getSaldoAula() - 1);
                produtoContratadoRepository.save(produtoContratado);
                return;
            }
        }

        throw new PlanoInativoException();
    }

    public Boolean temSaldo(Long id){
        ProdutoContratado produtoContratado = listarPorId(id);
        return produtoContratado.getSituacao().equals(true) && produtoContratado.getSaldoAula() > 0;
    }

    private Logger logger = LoggerFactory.getLogger(ProdutoContratadoService.class);

    // Executa todo dia à meia-noite
    @Scheduled(cron = "0 0 0 * * *")
    public void verificarExpiracaoProdutosContratados(){
        logger.info("Iniciando verificação de expiração de produtos contratados");
        List<ProdutoContratado> produtosContratados = produtoContratadoRepository.findBySituacao(true);
        int contador = 0;
        for(ProdutoContratado produtoContratado : produtosContratados){
            if(produtoContratado.getDataExpiracao().isBefore(LocalDate.now()) &&
               produtoContratado.getSituacao().equals(true)){
                produtoContratado.setSituacao(false);
                produtoContratadoRepository.save(produtoContratado);
                contador++;
            }
        }
        logger.info("Verificação concluída. De " + produtosContratados.size() +
                    " produtos contratados, " + contador + " foram desativados.");
    }
}
