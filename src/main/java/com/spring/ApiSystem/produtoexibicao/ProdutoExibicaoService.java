package com.spring.ApiSystem.produtoexibicao;

import com.spring.ApiSystem.produtoexibicao.dto.request.CadastroProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.request.EdicaoProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResListaProdutoExibicaoDto;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDto;
import com.spring.ApiSystem.produtoexibicao.enums.Status;
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

    public ResProdutoExibicaoDto criarProduto(CadastroProdutoExibicaoDTO produto){
        validarStatus(produto.status());
        ProdutoExibicao produtoEntity = produtoExibicaoMapper.toEntity(produto);
        produtoEntity.setDataCriacao(LocalDateTime.now());
        produtoExibicaoRepository.save(produtoEntity);
        return produtoExibicaoMapper.toResProdutoExibicaoDTO(produtoEntity);
    }

    public ResProdutoExibicaoDto editarProduto(Long id, EdicaoProdutoExibicaoDTO produto){
        desativarProduto(id);
        return criarProduto(produtoExibicaoMapper.toCadastroProdutoExibicaoDTO(produto));
    }

    public List<ResProdutoExibicaoDto> listarProdutosPorStatus(String status){
        validarStatus(status);
        return produtoExibicaoRepository.findByStatus(Status.valueOf(status.toUpperCase()));
    }

    public List<ResListaProdutoExibicaoDto> listarProdutos(){
        return produtoExibicaoMapper.toResListaProdutoExibicaoDTO(produtoExibicaoRepository.findAll());
    }

    public ProdutoExibicao buscarPorId(Long id){
        return produtoExibicaoRepository.findById(id)
                .orElseThrow(() -> new ProdutoExibicaoNaoEncontradoPorId(id));
    }

    public ResProdutoExibicaoDto resBuscarPorId(Long id){
        return produtoExibicaoMapper.toResProdutoExibicaoDTO(buscarPorId(id));
    }

    public void desativarProduto(Long id) {
        ProdutoExibicao produto = buscarPorId(id);
        produto.setStatus(Status.INATIVO);
        produtoExibicaoRepository.save(produto);
    }

    public void validarStatus(String status) {
        for (Status s : Status.values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + status);
    }
}
