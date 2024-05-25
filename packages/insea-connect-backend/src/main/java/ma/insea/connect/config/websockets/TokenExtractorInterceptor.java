package ma.insea.connect.config.websockets;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
public class TokenExtractorInterceptor implements HandshakeInterceptor {

    private final RestTemplate restTemplate;

    public TokenExtractorInterceptor() {
        this.restTemplate = new RestTemplate();
        // Add a logging interceptor for debugging requests and responses
        this.restTemplate.getInterceptors().add((ClientHttpRequestInterceptor) (request, body, execution) -> {

            return execution.execute(request, body);
        });
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Extract the query parameter "token" from the request URI
        String token = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams().getFirst("token");

        if (token != null && !token.isEmpty() && validateTokenWithKeycloak(token)) {
            attributes.put("token", token);
            return true;
        }

        response.setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // Nothing to do after the handshake
    }

    private boolean validateTokenWithKeycloak(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", token);
        map.add("client_id", "INSEA-CONNECT-API");
        map.add("client_secret", "**********"); // Replace with your actual client secret

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8088/realms/INSEA-CONNECT/protocol/openid-connect/token/introspect",
                    HttpMethod.POST,
                    request,
                    String.class);


            // Parse the response body to determine if the token is active
            return response.getBody() != null && response.getBody().contains("\"active\":true");
        } catch (Exception e) {
            log.error("Error validating token with Keycloak: {}", e.getMessage(), e);
            return false;
        }
    }
}
