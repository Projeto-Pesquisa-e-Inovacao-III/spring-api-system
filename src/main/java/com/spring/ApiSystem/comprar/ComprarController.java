package com.spring.ApiSystem.comprar;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comprar")
public class ComprarController {

    private final ComprarService comprarService;

    public ComprarController(ComprarService comprarService) {
        this.comprarService = comprarService;
    }


    @PostMapping("/{produtoExibicaoId}")
    public ResponseEntity<?> pagar(@PathVariable Long produtoExibicaoId){
        return new ResponseEntity<>(comprarService.comprar(produtoExibicaoId), HttpStatus.OK);
    }
}
