package com.spring.ApiSystem.produtoexibicao;

import com.spring.ApiSystem.produtoexibicao.dto.request.CadastroProdutoExibicaoDTO;
import com.spring.ApiSystem.produtoexibicao.dto.response.ResProdutoExibicaoDTO;
import com.spring.ApiSystem.enums.Status;
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

    public ResProdutoExibicaoDTO criarProduto(CadastroProdutoExibicaoDTO produto){
        validarStatus(produto.status());
        ProdutoExibicao produtoEntity = produtoExibicaoMapper.toEntity(produto);
        produtoEntity.setDataCriacao(LocalDateTime.now());
        produtoExibicaoRepository.save(produtoEntity);
        return produtoExibicaoMapper.toResProdutoExibicaoDTO(produtoEntity);
    }

    public List<ResProdutoExibicaoDTO> listarProdutosPorStatus(String status){
        validarStatus(status);
        return produtoExibicaoRepository.findByStatus(Status.valueOf(status.toUpperCase()));
    }

    public List<ResProdutoExibicaoDTO> listarProdutos(){
        return produtoExibicaoRepository.findAllBy();
    }

    public void desativarProduto(Long id) {
        ProdutoExibicao produto = produtoExibicaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        produto.setStatus(Status.INATIVO);
        produto.setDataAtualizacao(LocalDateTime.now());
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
