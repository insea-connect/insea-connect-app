package ma.insea.connect.keycloak.service;

import ma.insea.connect.chatbot.service.ChatbotService;
import ma.insea.connect.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import ma.insea.connect.chatbot.service.ChatbotService;
import ma.insea.connect.keycloak.DTO.*;
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


@Service
public class KeycloakApiService {
    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuerUri;
    String tokenUrl = issuerUri + "/protocol/openid-connect/token";
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    UserService userService;

    @Autowired
    ChatbotService chatbotService;

    public LoginResponseDTO login(String username, String password) {
        String tokenUrl = issuerUri + "/protocol/openid-connect/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", username);
        formData.add("password", password);
        formData.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, requestEntity, Map.class);

            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.extractTokenInfo(response.getBody().toString());
            loginResponseDTO.setUser(LoginResponseUserDTO.mapToLoginUserResponseDTO(userService.findByUsername(username)));
            loginResponseDTO.setThreadId(chatbotService.getThreadIdString());

            return loginResponseDTO;
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), "Error requesting token from Keycloak: " + e.getMessage(), e);
        }
        catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e);
        }
    }


    public Map<String, Object> refreshToken(String refreshToken) {
        String tokenUrl = issuerUri + "/protocol/openid-connect/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);
        formData.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, requestEntity, Map.class);
            return response.getBody();
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), "Error requesting token from Keycloak: " + e.getMessage(), e);
        }
        catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> logout(String refreshToken) {
        String tokenUrl = issuerUri + "/protocol/openid-connect/logout";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, requestEntity, Map.class);
            return response.getBody();
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), "Error requesting logout from Keycloak: " + e.getMessage(), e);
        }
        catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e);
        }
    }


}
