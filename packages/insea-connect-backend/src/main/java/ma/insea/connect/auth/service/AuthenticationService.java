package ma.insea.connect.auth.service;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.account.model.User;
import ma.insea.connect.account.repository.UserRepository;
import ma.insea.connect.auth.dto.RegistrationResponse;
import ma.insea.connect.auth.dto.UserLoginDTO;
import ma.insea.connect.auth.dto.UserRegistrationRequestDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ma.insea.connect.account.model.Role;
@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;


    public RegistrationResponse register(UserRegistrationRequestDTO request) {
        User user = User.builder()
                .username(request.username())
                .lastName(request.lastName())
                .firstName(request.firstName())
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        user.setRole(Role.STUDENT);
        user = repository.save(user);

        String token = jwtService.generateToken(user);

        return new RegistrationResponse(token);
    }

    public RegistrationResponse authenticate (UserLoginDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = repository.findByUsername(request.username()).orElseThrow();
        String token = jwtService.generateToken(user);

        return new RegistrationResponse(token);
    }
}
