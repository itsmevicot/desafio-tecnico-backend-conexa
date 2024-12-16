package com.conexa.backend.scheduling;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class ConexaSchedulingApplication {

	public static void main(String[] args) {
		Dotenv dotenv;

		if (Files.exists(Paths.get("/app/.env"))) {
			dotenv = Dotenv.configure()
					.directory("/app")
					.filename(".env")
					.load();
		}
		else {
			dotenv = Dotenv.configure()
					.directory(".")
					.filename(".env")
					.load();
		}

		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(ConexaSchedulingApplication.class, args);
	}
}
