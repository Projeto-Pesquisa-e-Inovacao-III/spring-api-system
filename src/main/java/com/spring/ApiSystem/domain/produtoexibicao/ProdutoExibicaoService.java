package com.spring.ApiSystem.domain.produtoexibicao;

import com.spring.ApiSystem.domain.produtoexibicao.dto.request.ReqCadastroProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.dto.request.ReqEdicaoProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.dto.response.ResProdutoExibicaoDto;
import com.spring.ApiSystem.domain.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.exception.ProdutoExibicaoNaoEncontradoPorId;
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

    @Transactional
    public ResProdutoExibicaoDto criarProduto(ReqCadastroProdutoExibicaoDto produto){
        ProdutoExibicao produtoEntity = produtoExibicaoMapper.toEntity(produto);
        produtoEntity.setDataCriacao(LocalDateTime.now());
        produtoExibicaoRepository.save(produtoEntity);

        return produtoExibicaoMapper.toResProdutoExibicaoDTO(produtoEntity);
    }

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
        return produtoExibicaoRepository.existsProdutoExibicaoAtivoById(id);
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
