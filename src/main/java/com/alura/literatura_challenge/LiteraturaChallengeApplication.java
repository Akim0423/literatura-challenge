package com.alura.literatura_challenge;

import com.alura.literatura_challenge.principal.Principal;
import com.alura.literatura_challenge.repository.AutorRepository;
import com.alura.literatura_challenge.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaChallengeApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository repository;
	@Autowired
	private AutorRepository autorRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraturaChallengeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("\nIniciando la aplicacion...\n");
		Principal principal = new Principal(repository,autorRepository);
		principal.menu();
	}
}