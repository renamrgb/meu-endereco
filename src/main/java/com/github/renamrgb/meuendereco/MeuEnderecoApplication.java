package com.github.renamrgb.meuendereco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MeuEnderecoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeuEnderecoApplication.class, args);
	}

}
