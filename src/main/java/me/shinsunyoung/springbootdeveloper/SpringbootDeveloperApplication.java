package me.shinsunyoung.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //created_at,updated_at 자동업데이트
@SpringBootApplication
public class SpringbootDeveloperApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDeveloperApplication.class, args);
	}

}
