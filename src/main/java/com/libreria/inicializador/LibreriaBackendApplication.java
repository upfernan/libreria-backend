package com.libreria.inicializador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@ComponentScan(basePackages = { "com.libreria.controlador" })
public class LibreriaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibreriaBackendApplication.class, args);
	}

}
