package ma.insea.connect.chatbot.DTO.conversationDTO;

import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ChatbotMessageResponseDTO {
    private Date date = new Date();
    private String message;
}
