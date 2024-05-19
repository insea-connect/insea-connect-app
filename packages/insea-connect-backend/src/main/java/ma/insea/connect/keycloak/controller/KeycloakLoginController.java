package ma.insea.connect.keycloak.controller;

import ma.insea.connect.keycloak.DTO.LoginRequestDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.logging.Logger;

@RestController
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

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
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



            // Return the response containing the token
            return ResponseEntity.ok(response.getBody());
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
    public ResponseEntity<Map<String, Object>> refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) {

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

            // Return the response containing the token
            return ResponseEntity.ok(response.getBody());
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
