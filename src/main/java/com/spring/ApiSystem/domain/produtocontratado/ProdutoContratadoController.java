package com.spring.ApiSystem.domain.produtocontratado;

import com.spring.ApiSystem.domain.produtocontratado.dto.request.ReqCriarProdutoContratadoDto;
import com.spring.ApiSystem.domain.produtocontratado.dto.request.ReqCriarProdutoContratadoPagamentoDTO;
import com.spring.ApiSystem.domain.produtocontratado.dto.request.ReqProdutoContratadoDto;
import com.spring.ApiSystem.domain.produtocontratado.dto.response.*;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.produtocontratado.mapper.ProdutoContratadoMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/produtos-contratados")
public class ProdutoContratadoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoContratadoController.class);

    private final ProdutoContratadoService produtoContratadoService;
    private final ProdutoContratadoMapper produtoContratadoMapper;

    public ProdutoContratadoController(
            ProdutoContratadoService produtoContratadoService,
            ProdutoContratadoMapper produtoContratadoMapper
    ) {
        this.produtoContratadoService = produtoContratadoService;
        this.produtoContratadoMapper = produtoContratadoMapper;
    }

    @Operation(
            summary = "Cria um produto contratado (necessário login)",
            description = "Endpoint para criar um produto contratado com base no ID do produto de exibição e no ID do aluno logado"
    )
    @PostMapping
    public ResponseEntity<ResProdutoContratadoDto> criarProdutoContratado(
            @Valid @RequestBody ReqCriarProdutoContratadoDto reqCriarProdutoContratadoDto
    ) {
        ResProdutoContratadoDto resProdutoContratadoDto =
                produtoContratadoService.criarPordutoContratadoDoAlunoAtual(
                        reqCriarProdutoContratadoDto.idProdutoExibicao()
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(resProdutoContratadoDto);
    }

    @PostMapping("/pagamento")
    public ResponseEntity<Void> criarProdutoContratadoPagamento(
            @Valid @RequestBody ReqCriarProdutoContratadoPagamentoDTO reqCriarProdutoContratadoPagamentoDTO
    ) {
        Long idProdutoExibicao = reqCriarProdutoContratadoPagamentoDTO.itemId();
        Long idAluno = reqCriarProdutoContratadoPagamentoDTO.consumerId();

        logger.info("Iniciando criação de produto exibicao, recebido: {}", reqCriarProdutoContratadoPagamentoDTO);

        produtoContratadoService.criarProdutoContratadoPeloIdAluno(idProdutoExibicao, idAluno);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Lista dos produtos contratados com base na situacao",
            description = "Endpoint para listar todos os produtos contratados em sistema com base na situacao informada"
    )
    @GetMapping("/situacao/{situacao}")
    public ResponseEntity<List<ResProdutoContratadoDto>> listarProdutosContratadosPorSituacao(
            @PathVariable Boolean situacao
    ) {
        List<ResProdutoContratadoDto> produtosContratados = produtoContratadoService.listarPorSituacao(situacao);
        return ResponseEntity.ok(produtosContratados);
    }

    @Operation(
            summary = "Busca o produto contratado com base no ID (necessário login)",
            description = "Endpoint para listar o produto contratado com base no ID informado"
    )
    @GetMapping("/id/{id}")
    public ResponseEntity<ResProdutoContratadoDto> buscarProdutoContratadoPorId(@PathVariable Long id) {
        ProdutoContratado produtoContratado = produtoContratadoService.buscarPorIdAndAluno(id);
        return ResponseEntity.ok(produtoContratadoMapper.toDto(produtoContratado));
    }

    @GetMapping("/total-tipo/{tipoAula}")
    public ResponseEntity<ResBuscarSaldoPorTipoAulaDto> buscarTotalSaldoAulaPorTipoEspecifico(
            @PathVariable TipoAula tipoAula
    ) {
        return ResponseEntity.ok(produtoContratadoService.buscarTotalSaldoAulaPorTipoEspecifico(tipoAula));
    }

    @GetMapping("/total-tipo")
    public ResponseEntity<ResTotalTipoSaldosDto> buscarTotalSaldosPorTipoAula() {
        return ResponseEntity.ok(produtoContratadoService.getSaldoFromAllTipoAula());
    }

    @Operation(
            summary = "Lista todos os produtos contratados do usuário (necessário login)",
            description = "Endpoint para listar todos os produtos contratados em sistema que tiverem o idAluno correspondente"
    )
    @GetMapping
    public ResponseEntity<Page<ResProdutoContratadoDto>> listarProdutosContratadosPorIdAluno(
            @ModelAttribute ReqProdutoContratadoDto dto,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "dataCompra", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        Page<ResProdutoContratadoDto> produtosContratados =
                produtoContratadoService.listarPorAluno(pageable, dto);

        if(produtosContratados.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produtosContratados);
    }

    @GetMapping("/detalhado/{id}")
    public ResponseEntity<ResProdutoContratadoDetalhadoDTO> buscarProdutoContratadoDetalhe(
            @PathVariable Long id
    ) {
        ProdutoContratado produtoContratado = produtoContratadoService.buscarPorIdAndAluno(id);
        return ResponseEntity.ok(produtoContratadoMapper.toResProdutoContratadoDetalhadoDTO(produtoContratado));
    }

    @Operation(
            summary = "Busca o produto contratado ativo do usuário logado (necessário login)",
            description = "Endpoint para buscar o produto contratado ativo do usuário logado"
    )
    @GetMapping("/ativo")
    public ResponseEntity<ResProdutoContratadoAtivoDto> buscarProdutoContratadoAtivo(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(produtoContratadoService.buscarProdutoContratadoAtivo(userDetails.getUsername()));
    }

    @Operation(
            summary = "Lista os ganhos mensais dos últimos meses (necessário login)",
            description = "Endpoint para listar os ganhos mensais dos últimos meses com base na quantidade de meses informada"
    )
    @GetMapping("/ganhos-mes/{quantidadeMeses}")
    public ResponseEntity<List<ResListarGanhoMensalDto>> listarGanhosMensais(
            @PathVariable Integer quantidadeMeses
    ) {
        return ResponseEntity.ok(produtoContratadoService.listarGanhosMensais(quantidadeMeses));
    }

    @Operation(
            summary = "Contagem de planos vendidos nos últimos dias (necessário login)",
            description = "Endpoint para contar a quantidade de planos vendidos com base na quantidade de dias informada"
    )
    @GetMapping("/planos-vendidos/{quantidadeDias}")
    public ResponseEntity<Integer> contarPlanosVendidosUltimosDias(@PathVariable Integer quantidadeDias) {
        return ResponseEntity.ok(produtoContratadoService.contarProdutosVendidosUltimosDias(quantidadeDias));
    }

    @Operation(
            summary = "Contagem e percentual de alunos com planos expirados",
            description = "Endpoint para contar a quantidade e o percentual de alunos que possuem planos expirados"
    )
    @GetMapping("/quantidade-e-percentual-alunos-expirados")
    public ResponseEntity<ResQuantidadePercentualAlunosExpiradosDto> contagemEPercentualAlunosExpirados() {
        return ResponseEntity.ok(produtoContratadoService.contagemEPercentualAlunosExpirados());
    }
}