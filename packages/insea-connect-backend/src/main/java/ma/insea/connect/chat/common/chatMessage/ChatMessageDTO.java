package ma.insea.connect.chat.common.chatMessage;

import java.util.Date;

import lombok.Data;
@Data
public class ChatMessageDTO {
    private String senderId;
    private String recipientId;
    private String content;
    private Date timestamp;//to datete
    
}
