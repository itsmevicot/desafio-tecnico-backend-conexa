package com.conexa.backend.scheduling;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class ConexaSchedulingApplicationTests {

	static {
		Dotenv dotenv;

		if (Files.exists(Paths.get("/app/.env"))) {
			dotenv = Dotenv.configure()
					.directory("/app")
					.filename(".env")
					.load();
		} else {
			dotenv = Dotenv.configure()
					.directory(".")
					.filename(".env")
					.load();
		}

		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
	}

	@Test
	void contextLoads() {
		System.out.println("Environment Variable (SPRING_APPLICATION_NAME): "
				+ System.getProperty("SPRING_APPLICATION_NAME"));
	}
}
