package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.produtocontratado.dto.response.BuscarProdutoContratadoPorId;
import com.spring.ApiSystem.produtocontratado.exception.ProdutoContratadoNaoExisteException;
import org.springframework.stereotype.Service;

@Service
public class ProdutoContratadoService {
    private final ProdutoContratadoRepository produtoContratadoRepository;

    public ProdutoContratadoService(ProdutoContratadoRepository produtoContratadoRepository) {
        this.produtoContratadoRepository = produtoContratadoRepository;
    }

    public BuscarProdutoContratadoPorId buscarPorIdProdutoContratado(Integer id) {
        var produtoContratado = buscarPorId(id);

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

    public ProdutoContratado buscarPorId(Integer id) {

        return produtoContratadoRepository
                .findById(id)
                .orElseThrow(ProdutoContratadoNaoExisteException::new);
    }
}
