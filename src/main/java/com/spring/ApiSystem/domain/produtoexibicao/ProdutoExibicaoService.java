package com.spring.ApiSystem.domain.produtoexibicao;

import com.spring.ApiSystem.domain.produtoexibicao.dto.request.ReqCadastroProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.dto.request.ReqEdicaoProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.dto.response.ResProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.dto.response.ResProdutoExibicaoLimitAndSizeDto;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import com.spring.ApiSystem.domain.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.exception.ProdutoExibicaoNaoEncontradoPorId;
import com.spring.ApiSystem.domain.produtoexibicao.exception.ProdutoExibicaoOutOfLimitsException;
import com.spring.ApiSystem.domain.produtoexibicao.mapper.ProdutoExibicaoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoExibicaoService {
    private final ProdutoExibicaoRepository produtoExibicaoRepository;
    private final ProdutoExibicaoMapper produtoExibicaoMapper;

    public ProdutoExibicaoService(ProdutoExibicaoRepository produtoExibicaoRepository,
                                  ProdutoExibicaoMapper produtoExibicaoMapper) {
        this.produtoExibicaoRepository = produtoExibicaoRepository;
        this.produtoExibicaoMapper = produtoExibicaoMapper;
    }

    private final Integer LIMIT_PRODUTO_PACOTE = 6;
    private final Integer LIMIT_PRODUTO_ADICIONAL = 6;

    public List<ResProdutoExibicaoLimitAndSizeDto> checkLimitByTipoProduto(){
        return List.of(
                new ResProdutoExibicaoLimitAndSizeDto(
                        TipoProduto.PACOTE,
                        LIMIT_PRODUTO_PACOTE,
                        produtoExibicaoRepository.countByStatusAndTipoProduto(
                                ProdutoExibicaoStatus.ATIVO,
                                TipoProduto.PACOTE
                        )
                ),
                new ResProdutoExibicaoLimitAndSizeDto(
                        TipoProduto.ADICIONAL,
                        LIMIT_PRODUTO_ADICIONAL,
                        produtoExibicaoRepository.countByStatusAndTipoProduto(
                                ProdutoExibicaoStatus.ATIVO,
                                TipoProduto.ADICIONAL
                        )
                )
        );
    }

    public void defineLimitByStatus(Integer limit, ProdutoExibicaoStatus status, TipoProduto tipoProduto){
        Integer totalProdutosAtivos = produtoExibicaoRepository.countByStatusAndTipoProduto(
                status,
                tipoProduto
        );

        if(totalProdutosAtivos >= limit){
            throw new ProdutoExibicaoOutOfLimitsException(limit, tipoProduto, status);
        }
    }

    @Transactional
    public ResProdutoExibicaoDto criarProduto(ReqCadastroProdutoExibicaoDto produto){
        if(produto.tipoProduto().equals(TipoProduto.PACOTE)){
            defineLimitByStatus(LIMIT_PRODUTO_PACOTE, ProdutoExibicaoStatus.ATIVO, produto.tipoProduto());
        } else if (produto.tipoProduto().equals(TipoProduto.ADICIONAL)) {
            defineLimitByStatus(LIMIT_PRODUTO_ADICIONAL, ProdutoExibicaoStatus.ATIVO, produto.tipoProduto());
        }

        ProdutoExibicao produtoEntity = produtoExibicaoMapper.toEntity(produto);
        produtoEntity.setDataCriacao(LocalDateTime.now());
        produtoExibicaoRepository.save(produtoEntity);

        return produtoExibicaoMapper.toResProdutoExibicaoDTO(produtoEntity);

    }

    @Transactional
    public ResProdutoExibicaoDto editarProduto(Long id, ReqEdicaoProdutoExibicaoDto produto){
        produto =  produto.withTipoProduto(buscarPorId(id).getTipoProduto());
        desativarProduto(id);
        return criarProduto(produtoExibicaoMapper.toCadastroProdutoExibicaoDTO(produto));
    }

    public List<ResProdutoExibicaoDto> listarProdutosPorStatus(String status){
        return produtoExibicaoMapper.toResProdutoExibicaoDTOList(
                produtoExibicaoRepository.findByStatus(ProdutoExibicaoStatus.valueOf(status.toUpperCase()))
        );
    }

    public List<ResProdutoExibicaoDto> listarProdutosAtivos(){
        return listarProdutosPorStatus("ATIVO");
    }

    public List<ResProdutoExibicaoDto> listarProdutos(){
        return produtoExibicaoMapper.toResProdutoExibicaoDTOList(produtoExibicaoRepository.findAll());
    }

    public ProdutoExibicao buscarPorId(Long id){
        return produtoExibicaoRepository.findById(id)
                .orElseThrow(() -> new ProdutoExibicaoNaoEncontradoPorId(id));
    }

    public Boolean produtoExibicaoAtivoExiste(Long id){
        return produtoExibicaoRepository.existsByIdAndStatus(id, ProdutoExibicaoStatus.ATIVO);
    }

    public ResProdutoExibicaoDto resBuscarPorId(Long id){
        return produtoExibicaoMapper.toResProdutoExibicaoDTO(buscarPorId(id));
    }

    public void desativarProduto(Long id) {
        ProdutoExibicao produto = buscarPorId(id);
        produto.setStatus(ProdutoExibicaoStatus.INATIVO);
        produtoExibicaoRepository.save(produto);
    }

}
