package ma.insea.connect.chat.common.chatMessage;

import java.util.Date;

import lombok.AllArgsConstructor;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private Long senderId;
    private Long recipientId;
    private String content;
    private Date timestamp;
    
}
