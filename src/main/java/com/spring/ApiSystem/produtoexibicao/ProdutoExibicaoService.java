package com.spring.ApiSystem.produtoexibicao;

import com.spring.ApiSystem.produtoexibicao.dto.request.ReqCadastroProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.request.ReqEdicaoProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.produtoexibicao.exception.ProdutoExibicaoNaoEncontradoPorId;
import com.spring.ApiSystem.produtoexibicao.mapper.ProdutoExibicaoMapper;
import org.springframework.stereotype.Service;
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

    public ResProdutoExibicaoDTO criarProduto(ReqCadastroProdutoExibicaoDTO produto){
        ProdutoExibicao produtoEntity = produtoExibicaoMapper.toEntity(produto);
        produtoEntity.setDataCriacao(LocalDateTime.now());
        produtoExibicaoRepository.save(produtoEntity);
        return produtoExibicaoMapper.toResProdutoExibicaoDTO(produtoEntity);
    }

    public ResProdutoExibicaoDTO editarProduto(Long id, ReqEdicaoProdutoExibicaoDTO produto){
        desativarProduto(id);
        return criarProduto(produtoExibicaoMapper.toCadastroProdutoExibicaoDTO(produto));
    }

    public List<ResProdutoExibicaoDTO> listarProdutosPorStatus(String status){
        return produtoExibicaoRepository.findByStatus(ProdutoExibicaoStatus.valueOf(status.toUpperCase()));
    }

    public List<ResProdutoExibicaoDTO> listarProdutos(){
        List<ResProdutoExibicaoDTO> resProdutoExibicaoDTO = produtoExibicaoMapper.toResProdutoExibicaoDTOList(produtoExibicaoRepository.findAll());
        return  resProdutoExibicaoDTO;
    }

    public ProdutoExibicao buscarPorId(Long id){
        return produtoExibicaoRepository.findById(id)
                .orElseThrow(() -> new ProdutoExibicaoNaoEncontradoPorId(id));
    }

    public ResProdutoExibicaoDTO resBuscarPorId(Long id){
        return produtoExibicaoMapper.toResProdutoExibicaoDTO(buscarPorId(id));
    }

    public void desativarProduto(Long id) {
        ProdutoExibicao produto = buscarPorId(id);
        produto.setStatus(ProdutoExibicaoStatus.INATIVO);
        produtoExibicaoRepository.save(produto);
    }

}
