package ma.insea.connect.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String keycloakVerifyTokenEndpoint = "http://keycloak-server/auth/realms/INSEA-CONNECT/protocol/openid-connect/token/introspect";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Extract the token from query parameters or headers
        String token = request.getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || token.isEmpty()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        // Send the token to Keycloak's verification endpoint
        Map<String, String> body = Map.of("token", token);
        ResponseEntity<String> verifyResponse = restTemplate.postForEntity(keycloakVerifyTokenEndpoint, body, String.class);

        if (!verifyResponse.getStatusCode().is2xxSuccessful()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        // Optionally add more checks on the verifyResponse body content
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // Handle post-handshake actions here
    }
}