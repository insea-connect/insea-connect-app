package ma.insea.connect.keycloak.controller;

import lombok.extern.slf4j.Slf4j;
import ma.insea.connect.chatbot.service.ChatbotService;
import ma.insea.connect.keycloak.DTO.*;
import ma.insea.connect.keycloak.service.KeycloakApiService;
import ma.insea.connect.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/api")
public class KeycloakLoginController {

    // Retrieve the issuer URI from properties
    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuerUri;

    // Other required properties
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    UserService userService;

    @Autowired
    ChatbotService chatbotService;
    @Autowired
    KeycloakApiService keycloakApiService ;
    @PostMapping("/login")
    //Map<String, Object>
    public ResponseEntity<LoginResponseDTO > login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        try {
            LoginResponseDTO response = keycloakApiService.login(username, password);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Map<String, Object>> refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            Map<String, Object> responseBody = keycloakApiService.refreshToken(refreshTokenDTO.getRefreshToken());
            return ResponseEntity.ok(responseBody);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            Map<String, Object> responseBody = keycloakApiService.logout(refreshTokenDTO.getRefreshToken());
            return ResponseEntity.ok(responseBody);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getMessage()));
        }
    }
}
