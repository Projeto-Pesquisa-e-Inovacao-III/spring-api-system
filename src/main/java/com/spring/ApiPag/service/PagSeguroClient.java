package com.spring.ApiPag.service;

import com.spring.ApiPag.dto.CheckoutDto.CheckoutDto;
import com.spring.ApiPag.entity.Checkout;
import com.spring.ApiPag.repository.CheckoutRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PagSeguroClient {

    private final CheckoutRepository checkoutRepository;
    private final RestTemplate restTemplate;
    private String url = "https://sandbox.api.pagseguro.com/checkouts";
    private Boolean debugMode = true;

    @Value("${pagseguro.token}")
    private String pagseguroToken;

    public PagSeguroClient(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
        this.restTemplate = new RestTemplate();
        if(debugMode){
            this.restTemplate.getInterceptors().add(new LoggingRequestInterceptor());
        }

    }

    public Checkout createCheckout(CheckoutDto checkoutDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + pagseguroToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(checkoutDto, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            Checkout checkout = objectMapper.readValue(response.getBody(), Checkout.class);
            return checkoutRepository.save(checkout);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Checkout getCheckoutFromAPI(String checkoutId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + pagseguroToken);
            HttpEntity<Object> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url + "/" + checkoutId, HttpMethod.GET, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(response.getBody());
            return objectMapper.readValue(response.getBody(), Checkout.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Checkout activateCheckout(String checkoutId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + pagseguroToken);
            HttpEntity<Object> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url + "/" + checkoutId + "/activate", entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(response.getBody());
            return objectMapper.readValue(response.getBody(), Checkout.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Checkout deactivateCheckout(String checkoutId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + pagseguroToken);
            HttpEntity<Object> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url + "/" + checkoutId + "/inactivate", entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(response.getBody());
            return objectMapper.readValue(response.getBody(), Checkout.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
