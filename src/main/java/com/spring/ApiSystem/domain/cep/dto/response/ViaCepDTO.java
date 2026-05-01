package com.spring.ApiSystem.domain.cep.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepDTO(
        @JsonProperty("cep") String id,
        @JsonProperty("logradouro") String logradouro,
        @JsonProperty("bairro") String bairro,
        @JsonProperty("localidade") String localidade,
        @JsonProperty("uf") String uf,
        @JsonProperty("erro") Boolean erro
) {
}
