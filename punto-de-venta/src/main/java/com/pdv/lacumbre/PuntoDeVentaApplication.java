package com.pdv.lacumbre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PuntoDeVentaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PuntoDeVentaApplication.class, args);
	}

}
