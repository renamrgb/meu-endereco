package com.github.renamrgb.meuendereco.cep.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CepResponse {

    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String uf;
}
