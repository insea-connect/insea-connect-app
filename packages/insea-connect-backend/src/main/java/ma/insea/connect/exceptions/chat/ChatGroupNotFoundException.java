package ma.insea.connect.exceptions.chat;

public class ChatGroupNotFoundException extends RuntimeException {
    public ChatGroupNotFoundException(String message) {
        super(message);
    }
}
