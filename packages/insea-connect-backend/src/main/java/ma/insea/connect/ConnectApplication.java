package ma.insea.connect;

import ma.insea.connect.keycloak.DTO.AddKeycloakDTO;
import ma.insea.connect.keycloak.controller.KeyCloakController;
import ma.insea.connect.user.DTO.AddUserDTO;
import ma.insea.connect.user.Role;
import ma.insea.connect.user.UserController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;
@SpringBootApplication
@EnableJpaAuditing
public class ConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectApplication.class, args);
	}

	/*@Profile("dev")
	@Bean
	public CommandLineRunner addAdmin(UserController userController, KeyCloakController keyCloakController) {
		return args -> {
			AddUserDTO user = AddUserDTO.builder()
					.username("admin3")
					.email("admin3")
					.firstName("admin3")
					.lastName("admin3")
					.role(Role.ADMIN)
					.password("admin3")
					.build();
			System.out.println("here it is "+AddKeycloakDTO.mapToAddKeycloakDTO(user).toString());
			userController.addUser1(user);


		};
	}*/
}