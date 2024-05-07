package ma.insea.connect.chat.common.chatMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.insea.connect.user.User;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private Long id;
    private String senderId;
    private String recipientId;
    private String content;
}
