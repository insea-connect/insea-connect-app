package ma.insea.connect.exceptions.chat;

public class ChatUserNotFoundException extends RuntimeException {
    public ChatUserNotFoundException(String message) {
        super(message);
    }
}