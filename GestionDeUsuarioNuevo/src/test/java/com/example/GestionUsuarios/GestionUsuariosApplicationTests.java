package com.example.GestionUsuarios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class GestionUsuariosApplicationTests {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	static class NoopDatabaseLoaderConfig {
		@Bean(name = "initDatabase")
		CommandLineRunner initDatabase() {
			return args -> {
			};
		}
	}

}
