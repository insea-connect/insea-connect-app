package ma.insea.connect;

import ma.insea.connect.account.model.Role;
import ma.insea.connect.account.model.User;
import ma.insea.connect.account.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class ConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectApplication.class, args);
	}

	@Profile("dev")
	@Bean
	public CommandLineRunner addAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			User user = User.builder()
					.username("admin")
					.role(Role.STUDENT)
					.passwordHash(passwordEncoder.encode("admin"))
					.build();
			userRepository.save(user);
		};
	}
}
