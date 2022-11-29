package com.github.renamrgb.meuendereco.cep.controller;

import com.github.renamrgb.meuendereco.cep.dto.CepResponse;
import com.github.renamrgb.meuendereco.cep.service.CepService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/cep")
public class CepController {

    private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @GetMapping("{cep}")
    public Mono<CepResponse> findByCep(@PathVariable String cep) {
        return cepService.findByCep(cep);
    }

}
