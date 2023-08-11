package Dcoding.Celebrem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CelebremApplication {

	public static void main(String[] args) {
		SpringApplication.run(CelebremApplication.class, args);
	}

}
