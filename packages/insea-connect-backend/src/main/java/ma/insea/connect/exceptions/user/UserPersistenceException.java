package ma.insea.connect.exceptions.user;

public class UserPersistenceException extends RuntimeException {
    public UserPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}