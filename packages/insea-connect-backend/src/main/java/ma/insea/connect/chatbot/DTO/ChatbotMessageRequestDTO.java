package ma.insea.connect.chatbot.DTO;

import lombok.*;
import ma.insea.connect.chat.common.chatMessage.ChatMessageDTO;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ChatbotMessageRequestDTO extends ChatMessageDTO {
    private String threadId;
}
