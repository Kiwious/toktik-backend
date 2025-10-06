package de.kiwious.toktik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "de.kiwious.toktik.repository")
@EntityScan(basePackages = "de.kiwious.toktik.model")
public class ToktikApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToktikApplication.class, args);
	}

}
