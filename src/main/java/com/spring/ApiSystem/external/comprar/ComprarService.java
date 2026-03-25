package com.spring.ApiSystem.external.comprar;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicaoService;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.external.comprar.dto.request.CheckoutRequestDto;
import com.spring.ApiSystem.external.comprar.dto.response.LinkDto;
import com.spring.ApiSystem.external.comprar.exception.CompraDeProdutoExibicaoInexistente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ComprarService {

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
        if(!produtoExibicaoService.produtoExibicaoAtivoExiste(produtoExibicaoId)){
            throw new CompraDeProdutoExibicaoInexistente(produtoExibicaoId);
        }

        Aluno aluno = userDetailsService.getCurrentAluno();

        produtoContratadoService.temProdutoContratadoTipoProdutoAtivo(
                produtoExibicaoId, aluno, TipoProduto.PACOTE
        );

        ProdutoExibicao produtoExibicao = produtoExibicaoService.buscarPorId(produtoExibicaoId);
        CheckoutRequestDto requestBody = montarCheckoutRequest(aluno, produtoExibicao);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CheckoutRequestDto> requestEntity = new HttpEntity<>(requestBody, headers);
        LinkDto response = restTemplate
                .postForEntity(url+"/api/v1/checkouts", requestEntity, LinkDto.class)
                .getBody();

        return response == null ? null : response.payLink();
    }

    private CheckoutRequestDto montarCheckoutRequest(Aluno aluno, ProdutoExibicao produtoExibicao) {
        CheckoutRequestDto.CustomerDto customer = new CheckoutRequestDto.CustomerDto(
                String.valueOf(aluno.getId()),
                aluno.getNome(),
                aluno.getEmail(),
                new CheckoutRequestDto.TaxDocumentDto(aluno.getCpf().getValue(), "CPF"),
                montarPhone(aluno)
        );

        CheckoutRequestDto.ItemDto item = new CheckoutRequestDto.ItemDto(
                String.valueOf(produtoExibicao.getId()),
                produtoExibicao.getTitulo(),
                getDescricaoItem(produtoExibicao),
                1,
                calcularUnitAmount(produtoExibicao.getPreco())
        );

        return new CheckoutRequestDto(
                customer,
                List.of(item),
                0,
                0
        );
    }

    private CheckoutRequestDto.PhoneDto montarPhone(Aluno aluno) {
        Telefone telefone = aluno.getTelefones() == null
                ? null
                : aluno.getTelefones().stream().findFirst().orElse(null);

        if (telefone == null) {
            return new CheckoutRequestDto.PhoneDto("+55", "", "");
        }

        String country = telefone.getPais();
        if (country == null || country.isBlank()) {
            country = "+55";
        } else if (!country.startsWith("+")) {
            country = "+" + country;
        }

        String area = telefone.getDdd() == null ? "" : telefone.getDdd();
        String number = telefone.getNumero() == null ? "" : telefone.getNumero();

        return new CheckoutRequestDto.PhoneDto(country, area, number);
    }

    private String getDescricaoItem(ProdutoExibicao produtoExibicao) {
        if (produtoExibicao.getDescricao() != null && !produtoExibicao.getDescricao().isBlank()) {
            return produtoExibicao.getDescricao();
        }

        if (produtoExibicao.getSubtitulo() != null && !produtoExibicao.getSubtitulo().isBlank()) {
            return produtoExibicao.getSubtitulo();
        }

        return produtoExibicao.getTitulo();
    }

    private Integer calcularUnitAmount(Double preco) {
        return BigDecimal.valueOf(preco)
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
    }
}
