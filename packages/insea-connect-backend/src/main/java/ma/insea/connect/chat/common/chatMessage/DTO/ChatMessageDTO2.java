package ma.insea.connect.chat.common.chatMessage.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO2 {
    private Long senderId;
    private String content;
    private Date timestamp;
    private String senderName;
    
}
