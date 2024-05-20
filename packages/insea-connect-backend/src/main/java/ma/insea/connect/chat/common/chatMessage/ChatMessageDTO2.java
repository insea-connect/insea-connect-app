package ma.insea.connect.chat.common.chatMessage;

import java.util.Date;

import lombok.Data;
@Data
public class ChatMessageDTO2 {
    private Long senderId;
    private String content;
    private Date timestamp;
    private String senderName;
    
}
