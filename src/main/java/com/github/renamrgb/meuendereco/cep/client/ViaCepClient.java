package com.github.renamrgb.meuendereco.cep.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ViaCepClient {

    private final WebClient webClient;

    @Value("${app-config.client.viacep}")
    private String viaCepUri;

    public ViaCepClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> findByCep(String cep) {
        log.info("#ViaCepClient #findByCep Buscando dados na api para o cep {}", cep);
        return webClient
                .get()
                .uri(buildUri(cep))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(throwable -> {
                    log.error("#ViaCepClient #findByCep Erro ao chamar a api para o cep {}", cep, throwable);
                    return Mono.empty();
                })
                .doOnNext(response -> log.info("#ViaCepClient #findByCep Retorno dos dados da api para o cep {} {}", cep, response));
    }

    public String buildUri(String cep) {
        return String.format(viaCepUri, cep);
    }

}
