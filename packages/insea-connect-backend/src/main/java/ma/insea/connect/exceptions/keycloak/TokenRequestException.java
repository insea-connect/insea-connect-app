package ma.insea.connect.exceptions.keycloak;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class TokenRequestException extends RuntimeException {
    private final HttpStatusCode status;

    public TokenRequestException(String message, Throwable cause, HttpStatusCode status) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}

