package ma.insea.connect.chat.common.chatMessage;

import java.util.Date;

import lombok.Data;
@Data
public class ChatMessageDTO3 {
    private Long senderId;
    private Long recipientId;
    private String content;
    private Date timestamp;
    
}
