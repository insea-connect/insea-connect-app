package ma.insea.connect.chat.conversation;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// import java.security.Timestamp;
import java.util.Date;

import ma.insea.connect.chat.common.chatMessage.DTO.ChatMessageDTO;
import ma.insea.connect.user.model.Status;
import jakarta.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDTO {
    @Id
    private String chatId;
    private Long recipientId;
    private ChatMessageDTO lastMessage;  
    private Status status; 
    private Date lastLogin;
    private String username;
}
