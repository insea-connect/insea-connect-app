package ma.insea.connect.exceptions.keycloak;

public class KeycloakCommunicationException extends RuntimeException {
    public KeycloakCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
