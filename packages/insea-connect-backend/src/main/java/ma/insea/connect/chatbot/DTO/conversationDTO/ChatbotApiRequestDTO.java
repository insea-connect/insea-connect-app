package ma.insea.connect.chatbot.DTO.conversationDTO;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class ChatbotApiRequestDTO {
    private String thread_id;
    private String message;
}
