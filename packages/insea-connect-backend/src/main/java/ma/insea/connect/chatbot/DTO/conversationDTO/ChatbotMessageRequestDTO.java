package ma.insea.connect.chatbot.DTO.conversationDTO;

import lombok.*;
import ma.insea.connect.chat.common.chatMessage.DTO.ChatMessageDTO;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ChatbotMessageRequestDTO extends ChatMessageDTO {
    private String threadId;
}
