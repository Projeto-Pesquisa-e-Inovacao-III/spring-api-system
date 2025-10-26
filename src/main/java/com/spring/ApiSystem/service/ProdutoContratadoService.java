package com.spring.ApiSystem.service;

import com.spring.ApiSystem.dto.ProdutoContratado.response.BuscarProdutoContratadoPorId;
import com.spring.ApiSystem.exception.ProdutoNaoExisteExcpetion;
import com.spring.ApiSystem.model.ProdutoContratado;
import com.spring.ApiSystem.repository.ProdutoContratadoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoContratadoService {
    private final ProdutoContratadoRepository produtoContratadoRepository;

    public ProdutoContratadoService(ProdutoContratadoRepository produtoContratadoRepository) {
        this.produtoContratadoRepository = produtoContratadoRepository;
    }

    public BuscarProdutoContratadoPorId buscarPorIdProdutoContratado(Integer id) {
        var produtoContratado = produtoContratadoRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Produto Contratado não encontrado"));

        return new BuscarProdutoContratadoPorId(
                produtoContratado.getId(),
                produtoContratado.getAtivo(),
                produtoContratado.getDataCompra(),
                produtoContratado.getDataExpiracao(),
                produtoContratado.getSaldoAula(),
                produtoContratado.getAluno().getId(),
                produtoContratado.getProdutoExibicao().getId()
        );
    }

    public ProdutoContratado findById(Integer id) {

        return produtoContratadoRepository
                .findById(id)
                .orElseThrow(ProdutoNaoExisteExcpetion::new);
    }
}
