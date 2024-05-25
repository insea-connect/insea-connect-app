package ma.insea.connect.chatbot.DTO.groupDTO;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ChatbotGroupMessageResponseDTO {
    private Date date = new Date();
    private String message;
}
