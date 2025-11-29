package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.produtocontratado.dto.request.ReqCriarProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.request.ReqCriarProdutoContratadoPagamentoDTO;
import com.spring.ApiSystem.produtocontratado.dto.response.ResListarGanhoMensalDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResProdutoContratadoAtivoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResProdutoContratadoDto;
import com.spring.ApiSystem.produtocontratado.dto.response.ResSaldoDto;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.usuario.security.JpaUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/produtos-contratados")
public class ProdutoContratadoController {
    private static final Logger logger = LoggerFactory.getLogger(ProdutoContratadoController.class);
    private final ProdutoContratadoService produtoContratadoService;

    public ProdutoContratadoController(ProdutoContratadoService produtoContratadoService) {
        this.produtoContratadoService = produtoContratadoService;
    }

    @Operation(summary = "Cria um produto contratado (necessário login)",
              description = "Endpoint para criar um produto contratado com base no ID do produto" +
                      "de exibição e no ID do aluno logado")
    @PostMapping
    public ResponseEntity<ResProdutoContratadoDto> criarProdutoContratado(@Valid @RequestBody ReqCriarProdutoContratadoDto reqCriarProdutoContratadoDto){
        ResProdutoContratadoDto resProdutoContratadoDto = produtoContratadoService.criarPordutoContratadoDoAlunoAtual(
                reqCriarProdutoContratadoDto.idProdutoExibicao()
        );

        return ResponseEntity.ok(resProdutoContratadoDto);
    }

    @PostMapping("/pagamento")
    public ResponseEntity<?> criarProdutoContratadoPagamento(@Valid @RequestBody ReqCriarProdutoContratadoPagamentoDTO reqCriarProdutoContratadoPagamentoDTO){
        Long idProdutoExibicao = reqCriarProdutoContratadoPagamentoDTO.itemId();
        Long idAluno = reqCriarProdutoContratadoPagamentoDTO.consumerId();
        logger.info("Iniciando criação de produto exibicao, recebido: {}", reqCriarProdutoContratadoPagamentoDTO);
        ResProdutoContratadoDto resProdutoContratadoDto = produtoContratadoService.criarProdutoContratadoPeloIdAluno(idProdutoExibicao, idAluno);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "Lista dos produtos contratados com base na situacao",
            description = "Endpoint para listar todos os produtos contratados em sistema com base" +
                    "na situacao informada")
    @GetMapping("/situacao/{situacao}")
    public ResponseEntity<List<ResProdutoContratadoDto>>
    listarProdutosContratadosPorSituacao(@PathVariable Boolean situacao){
        List<ResProdutoContratadoDto> produtosContratados = produtoContratadoService.listarPorSituacao(situacao);
        return ResponseEntity.ok(produtosContratados);
    }

    @Operation(summary = "Busca o produto contratado com base no ID (necessário login)",
            description = "Endpoint para listar o produto contratado com base no ID informado")
    @GetMapping("/id/{id}")
    public ResponseEntity<ResProdutoContratadoDto>
    buscarProdutoContratadoPorId(@PathVariable Long id){
        ResProdutoContratadoDto produtoContratado = produtoContratadoService
                .buscarPorIdAndAluno(id);
        if(produtoContratado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produtoContratado);
    }

    @GetMapping("/total-tipo/{tipoAula}")
    public ResponseEntity<ResSaldoDto> buscarTotalSaldoAulaPorTipo(@PathVariable TipoAula tipoAula){
        return ResponseEntity.ok(produtoContratadoService.buscarTotalSaldoAulaPorTipo(tipoAula));
    }

    @Operation(summary = "Lista todos os produtos contratados do usuário (necessário login)",
            description = "Endpoint para listar todos os produtos contratados em sistema que" +
                    "tiverem o idAluno correspondente")
    @GetMapping("/aluno")
    public ResponseEntity<List<ResProdutoContratadoDto>>
    listarProdutosContratadosPorIdAluno(@RequestParam(required = false) String nomeProduto,
                                        @RequestParam(required = false) LocalDateTime dataInicio,
                                        @RequestParam(required = false) LocalDateTime dataFim,
                                        @ParameterObject @PageableDefault(sort = "dataCompra", direction = Sort.Direction.DESC)
                                        Pageable pageable){
        List<ResProdutoContratadoDto> produtosContratados = produtoContratadoService
                .listarPorAluno(pageable, nomeProduto, dataInicio, dataFim);
        return ResponseEntity.ok(produtosContratados);
    }

    @Operation(summary = "Busca o produto contratado ativo do usuário logado (necessário login)",
            description = "Endpoint para buscar o produto contratado ativo do usuário logado")
    @GetMapping("/ativo")
    public ResponseEntity<ResProdutoContratadoAtivoDto>
    buscarProdutoContratadoAtivo(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(produtoContratadoService.buscarProdutoContratadoAtivo(userDetails.getUsername()));
    }

    @Operation(summary = "Lista os ganhos mensais dos últimos meses (necessário login)",
            description = "Endpoint para listar os ganhos mensais dos últimos meses com base na quantidade de meses informada, " +
                    "ou seja, se for informado 3, irá retornar os ganhos dos últimos 3 meses")
    @GetMapping("/ganhos-mes/{quantidadeMeses}")
    public ResponseEntity<List<ResListarGanhoMensalDto>> listarGanhosMensais(@PathVariable Integer quantidadeMeses){
        return ResponseEntity.ok(produtoContratadoService.listarGanhosMensais(quantidadeMeses));
    }

    @Operation(summary = "Contagem de planos vendidos nos últimos dias (necessário login)",
            description = "Endpoint para contar a quantidade de planos vendidos com base na quantidade de dias informada, " +
                    "ou seja, se for informado 7, irá retornar a quantidade de planos vendidos nos últimos 7 dias")
    @GetMapping("/planos-vendidos/{quantidadeDias}")
    public ResponseEntity<Integer> contarPlanosVendidosUltimosDias(@PathVariable Integer quantidadeDias){
        return ResponseEntity.ok(produtoContratadoService.contarProdutosVendidosUltimosDias(quantidadeDias));
    }
}
