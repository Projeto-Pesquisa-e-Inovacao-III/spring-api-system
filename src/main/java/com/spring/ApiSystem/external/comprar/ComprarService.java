package com.spring.ApiSystem.external.comprar;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicaoService;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.external.comprar.dto.request.ReqCheckoutDto;
import com.spring.ApiSystem.external.comprar.dto.response.ResCheckoutCreatedDto;
import com.spring.ApiSystem.external.comprar.exception.CompraDeProdutoExibicaoInexistente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class ComprarService {

    private static final Logger log = LoggerFactory.getLogger(ComprarService.class);
    private final JpaUserDetailsService userDetailsService;
    private final RestTemplate restTemplate;
    private final ProdutoExibicaoService produtoExibicaoService;
    private final ProdutoContratadoService produtoContratadoService;

    @Value("${pag.url}")
    private String url;

    public ComprarService(JpaUserDetailsService userDetailsService, RestTemplate restTemplate, ProdutoExibicaoService produtoExibicaoService, ProdutoContratadoService produtoContratadoService) {
        this.userDetailsService = userDetailsService;
        this.restTemplate = restTemplate;
        this.produtoExibicaoService = produtoExibicaoService;
        this.produtoContratadoService = produtoContratadoService;
    }


    public String comprar(Long produtoExibicaoId){

        log.info("Iniciando processo de compra | produtoExibicaoId: {} | url: {}", produtoExibicaoId, url);

        if(!produtoExibicaoService.produtoExibicaoAtivoExiste(produtoExibicaoId)){
            throw new CompraDeProdutoExibicaoInexistente(produtoExibicaoId);
        }

        Aluno aluno = userDetailsService.getCurrentAluno();
        ProdutoExibicao produtoExibicao = produtoExibicaoService.buscarPorId(produtoExibicaoId);

        produtoContratadoService.temProdutoContratadoTipoProdutoAtivo(
                produtoExibicaoId, aluno, TipoProduto.PACOTE
        );

        log.info("ProdutoExibicao encontrado e verificado para compra | produtoExibicaoId: {} | alunoId: {}", produtoExibicaoId, aluno.getId());

        Telefone telefone = aluno.getTelefones().stream().findFirst().orElse(null);

        ReqCheckoutDto requestBody = new ReqCheckoutDto(
                new ReqCheckoutDto.CustomerDto(
                        String.valueOf(aluno.getId()),
                        aluno.getNome(),
                        aluno.getEmail(),
                        new ReqCheckoutDto.TaxDocumentDto(aluno.getCpf().formatted(), "CPF"),
                        new ReqCheckoutDto.PhoneDto(
                                formatCountry(telefone),
                                telefone != null ? telefone.getDdd() : "",
                                telefone != null ? telefone.getNumero() : ""
                        )
                ),
                List.of(
                        new ReqCheckoutDto.ItemDto(
                                produtoExibicao.getId(),
                                UUID.randomUUID().toString(),
                                produtoExibicao.getTitulo(),
                                1,
                                toCents(produtoExibicao.getPreco())
                        )
                ),
                0,
                0
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReqCheckoutDto> requestEntity = new HttpEntity<>(requestBody, headers);

        log.info("Enviando requisição de checkout para gateway | produtoExibicaoId: {} | alunoId: {} | payload: {}", produtoExibicaoId, aluno.getId(), requestBody);

        ResCheckoutCreatedDto response = restTemplate
                .postForEntity(url + "/api/v1/checkouts", requestEntity, ResCheckoutCreatedDto.class)
                .getBody();

        log.info("Resposta recebida do gateway | produtoExibicaoId: {} | alunoId: {} | response: {}", produtoExibicaoId, aluno.getId(), response);

        return response != null ? response.payLink() : null;
    }

    private String formatCountry(Telefone telefone) {
        if (telefone == null || telefone.getPais() == null || telefone.getPais().isBlank()) {
            return "";
        }

        return telefone.getPais().startsWith("+") ? telefone.getPais() : "+" + telefone.getPais();
    }

    private Integer toCents(Double value) {
        return BigDecimal.valueOf(value)
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
    }
}
