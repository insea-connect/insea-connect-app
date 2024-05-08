package ma.insea.connect.chat.common.chatMessage;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "chat_notification",schema = "testo")
public class ChatNotification {
    private Long id;
    private String senderId;
    private String recipientId;
    private String content;
}
