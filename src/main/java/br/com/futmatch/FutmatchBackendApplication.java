package br.com.futmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FutmatchBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FutmatchBackendApplication.class, args);
	}

} 