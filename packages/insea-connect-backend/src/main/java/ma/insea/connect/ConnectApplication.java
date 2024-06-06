package ma.insea.connect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableMethodSecurity
public class ConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectApplication.class, args);
		
	}

}