package com.github.renamrgb.meuendereco.cep.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renamrgb.meuendereco.cep.client.ViaCepClient;
import com.github.renamrgb.meuendereco.cep.dto.CepResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.apache.logging.log4j.util.Strings.*;

@Slf4j
@Service
public class CepService {

    private final ViaCepClient viaCepClient;
    private final ObjectMapper objectMapper;
    private final CacheServiceWrapper cacheServiceWrapper;

    public CepService(ViaCepClient viaCepClient, ObjectMapper objectMapper, CacheServiceWrapper cacheServiceWrapper) {
        this.viaCepClient = viaCepClient;
        this.objectMapper = objectMapper;
        this.cacheServiceWrapper = cacheServiceWrapper;
    }

    public Mono<CepResponse> findByCep(String cep) {
        return cacheServiceWrapper
                .exists(cep)
                .flatMap(exists -> {
                    if (exists) {
                        return cacheServiceWrapper.get(cep);
                    } else {
                        return viaCepClient
                                .findByCep(cep)
                                .flatMap(response -> cacheServiceWrapper.save(cep, response));
                    }
                })
                .flatMap(this::handleResponse);
    }

    private Mono<CepResponse> handleResponse(String response) {
        if (!isEmpty(response)) {
            try {
                return Mono.just(objectMapper.readValue(response, CepResponse.class));
            } catch (Exception e) {
                log.error("#CepService #handleResponse Erro ao tentar converter resposta.", e);
            }
        }
        return Mono.empty();
    }
}
