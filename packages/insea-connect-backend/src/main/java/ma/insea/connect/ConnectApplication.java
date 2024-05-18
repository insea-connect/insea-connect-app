package ma.insea.connect;

import ma.insea.connect.user.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
// import org.springframework.security.crypto.password.PasswordEncoder;

import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;

@SpringBootApplication
@EnableJpaAuditing
public class ConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectApplication.class, args);
	}


}
