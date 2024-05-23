package ma.insea.connect.chatbot.DTO.groupDTO;

import lombok.*;
import ma.insea.connect.chat.common.chatMessage.DTO.GroupMessageDTO;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ChatbotGroupMessageRequestDTO extends GroupMessageDTO {

    private String threadId;
}
