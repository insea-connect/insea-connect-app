package ma.insea.connect.keycloak.controller;

import lombok.extern.slf4j.Slf4j;
import ma.insea.connect.keycloak.DTO.*;
import ma.insea.connect.user.UserService;
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

    @PostMapping("/login")
    //Map<String, Object>
    public ResponseEntity<LoginResponseDTO > login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();
        String json1 = "{\"access_token\": \"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJDd0ZKeTVrYm55TnRvaVQ3U2UyUjdnRFk3Z0hVTzBRR2lvUTRiUWhjSEtZIn0.eyJleHAiOjE3MTYzMDM5MzgsImlhdCI6MTcxNjMwMjEzOCwianRpIjoiODczMTg4ZjAtYTg2NC00ZTYzLWE1ZTMtZmFjZDYwZGE3OWQ3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg4L3JlYWxtcy9JTlNFQS1DT05OR0lDIiwiYXVkIjoicmVhbG0tbWFuYWdlbWVudCIsInN1YiI6ImNlOTQ4NmMwLTYwZjctNGY3MS04ZDc3LWEzOGQwZDExYjgxYyIsInR5cCI6IkJlYXJlciIsImF6cCI6IklOU0VBLUNPTk5FQ1QtQVBJIiwic2Vzc2lvbl9zdGF0ZSI6IjBiYzMyNDYyLWQ3NDktNDE0Yy1iOWM2LTMxODBkYzJhMDk2YiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsibWFuYWdlbWVudCIsIkFETUlOIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJtYW5hZ2UtdXNlcnMiLCJ2aWV3LXVzZXJzIiwicXVlcnktZ3JvdXBzIiwicXVlcnktdXNlcnMiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiIwYmMzMjQ2Mi1kNzQ5LTQxNGMtYjljNi0zMTgwZGMyYTA5NmIiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJhbmFzIEVsIEFyZGkiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbmFzIiwiZ2l2ZW5fbmFtZSI6ImFuYXMiLCJmYW1pbHlfbmFtZSI6IkVsIEFyZGkiLCJlbWFpbCI6ImFuYXNAZ21haWwuY29tIn0.dw49XwGhHXYhPASOJmF_suq5zSp8XKdRAq2XXpkdtAyEX2swDxfcqY7DSuL4cPhwefwp6CZaPvszd99HO49_OOpOvLWySAgbRIA8n-xupxr7LYyJS6fqGBWM9m_Gkld0fdacuBKmhn3PxlaCO7gE1_GtAGDEbmhR1XKetgLq1FAbsBkLID-7hrNwU0qqUbLfSDEIuYdx7h2pUFFBYOk6CLP3-00vUXYcDbsk88n1vmfZHQNEBc1zEIx8-76rMaZNMx393u4L3X71p7LM4ThF7FU8Hq3ZYfTjtqT5wyZQ_fIctU0AwkikvRg0YOeU84MkjvA6buGVpnqnWpUtPyQxGw\",\"expires_in\": 1800,\"refresh_expires_in\": 1800,\"refresh_token\": \"eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIyODY3MmFiZS1hODhiLTQ5M2YtOTY1NC05YWQ1YzVkODMwZjgifQ.eyJleHAiOjE3MTYzMDM5MzgsImlhdCI6MTcxNjMwMjEzOCwianRpIjoiOTAwZjA1ZDEtNTVmOS00NWQzLWExYjctMWQ0MGM4MTY0OWM4IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg4L3JlYWxtcy9JTlNFQS1DT05OR0lDIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg4L3JlYWxtcy9JTlNFQS1DT05OR0lDIiwic3ViIjoiY2U5NDg2YzAtNjBmNy00ZjcxLThkNzctYTM4ZDBkMTFiODFjIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6IklOU0VBLUNPTk5FQ1QtQVBJIiwic2Vzc2lvbl9zdGF0ZSI6IjBiYzMyNDYyLWQ3NDktNDE0Yy1iOWM2LTMxODBkYzJhMDk2YiIsInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6IjBiYzMyNDYyLWQ3NDktNDE0Yy1iOWM2LTMxODBkYzJhMDk2YiJ9.pjFB5pHf8jpjZk1EtP3UoUzg9Mlijx4LmJovEuS394s2nmaspyrQpjPuUnQIFppy1GiSZKeDmZpuyFS9c6-34g\",\"token_type\": \"Bearer\",\"not-before-policy\": 0,\"session_state\": \"0bc32462-d749-414c-b9c6-3180dc2a096b\",\"scope\": \"profile email\"}";

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
