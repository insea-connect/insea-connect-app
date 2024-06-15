package ma.insea.connect.keycloak.controller;

import lombok.extern.slf4j.Slf4j;
import ma.insea.connect.chatbot.service.ChatbotService;
import ma.insea.connect.keycloak.DTO.*;
import ma.insea.connect.user.DTO.UserInfoResponseDTO;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;
import ma.insea.connect.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    private UserRepository userRepository;


    @PostMapping("/login")
    //Map<String, Object>
    public ResponseEntity<LoginResponseDTO > login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        // Construct the token URL dynamically
        String tokenUrl = issuerUri + "/protocol/openid-connect/token";

        // Set headers to form-url-encoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Prepare form data
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", username);
        formData.add("password", password);
        formData.add("grant_type", "password");

        // Prepare the HTTP request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            // Make the request to the Keycloak token endpoint
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, requestEntity, Map.class);

            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.extractTokenInfo(response.getBody().toString());
            loginResponseDTO.setUser(LoginResponseUserDTO.mapToLoginUserResponseDTO(userService.findByUsername(username)));
            // Return the response containing the token
            loginResponseDTO.setThread_id( chatbotService.getThreadIdString());

            return ResponseEntity.ok(loginResponseDTO);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle client and server errors specifically
            throw new ResponseStatusException(e.getStatusCode(), "Error requesting token from Keycloak: " + e.getMessage(), e);
        }
        catch (RestClientException e) {
            // Catch other REST client errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponseDTO >refresh(@RequestBody RefreshTokenDTO refreshTokenDTO ,@AuthenticationPrincipal Jwt jwt) {

        // Construct the token URL dynamically
        String tokenUrl = issuerUri + "/protocol/openid-connect/token";

        // Set headers to form-url-encoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Prepare form data
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshTokenDTO.getRefreshToken());
        System.out.println(refreshTokenDTO.getRefreshToken());
        formData.add("grant_type", "refresh_token");

        // Prepare the HTTP request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {

            // Make the request to the Keycloak token endpoint
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, requestEntity, Map.class);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            String email = jwt.getClaimAsString("email");
            loginResponseDTO.setUser(LoginResponseUserDTO.mapToLoginUserResponseDTO(userService.findByEmail(email)));
            loginResponseDTO.extractTokenInfo(response.getBody().toString());
            // Return the response containing the token
            return ResponseEntity.ok(loginResponseDTO);
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle client and server errors specifically
            throw new ResponseStatusException(e.getStatusCode(), "Error requesting token from Keycloak: " + e.getMessage(), e);
        }
        catch (RestClientException e) {
            // Catch other REST client errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody RefreshTokenDTO refreshTokenDTO) {

        // Construct the token URL dynamically
        String tokenUrl = issuerUri + "/protocol/openid-connect/logout";

        // Set headers to form-url-encoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Prepare form data
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshTokenDTO.getRefreshToken());
        // Prepare the HTTP request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            // Make the request to the Keycloak token endpoint
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, requestEntity, Map.class);

            // Return the response containing the token
            return ResponseEntity.ok(response.getBody());
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle client and server errors specifically
            throw new ResponseStatusException(e.getStatusCode(), "Error requesting logging out from Keycloak: " + e.getMessage(), e);
        }
        catch (RestClientException e) {
            // Catch other REST client errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e);
        }
    }
}
