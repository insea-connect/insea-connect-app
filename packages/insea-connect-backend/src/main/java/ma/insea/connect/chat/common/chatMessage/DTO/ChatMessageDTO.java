package ma.insea.connect.chat.common.chatMessage.DTO;

import java.util.Date;

import jakarta.persistence.Column;
import lombok.Data;
@Data
public class ChatMessageDTO {
    private Long senderId;
    private Long recipientId;
    private String content;
    private Date timestamp;
    
}
