package com.pdv.lacumbre; // Tu paquete

import com.pdv.lacumbre.model.entity.UsuarioEntity;
import com.pdv.lacumbre.model.enums.Rol;
import com.pdv.lacumbre.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class PuntoDeVentaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PuntoDeVentaApplication.class, args);
	}

}