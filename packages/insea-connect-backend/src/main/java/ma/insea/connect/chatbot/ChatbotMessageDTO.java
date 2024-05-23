package ma.insea.connect.chatbot;

import lombok.*;
import ma.insea.connect.chat.common.chatMessage.ChatMessageDTO;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ChatbotMessageDTO extends ChatMessageDTO {
    private String threadId;
}
