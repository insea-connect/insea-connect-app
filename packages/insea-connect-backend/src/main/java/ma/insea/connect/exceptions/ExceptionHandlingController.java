package ma.insea.connect.exceptions;

import ma.insea.connect.exceptions.keycloak.KeycloakCommunicationException;
import ma.insea.connect.exceptions.keycloak.TokenRequestException;
import ma.insea.connect.exceptions.user.DatabaseOperationException;
import ma.insea.connect.exceptions.user.UserNotFoundException;
import ma.insea.connect.exceptions.user.UserPersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(TokenRequestException.class)
    public ResponseEntity<Map<String, String>> handleTokenRequestException(TokenRequestException ex) {
        return ResponseEntity.status(ex.getStatus()).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(KeycloakCommunicationException.class)
    public ResponseEntity<Map<String, String>> handleKeycloakCommunicationException(KeycloakCommunicationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(UserPersistenceException.class)
    public ResponseEntity<String> handleUserPersistenceException(UserPersistenceException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Persistence Error: " + ex.getMessage());
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<String> handleDatabaseOperationException(DatabaseOperationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database Operation Error: " + ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found: " + ex.getMessage());
    }
}