package ma.insea.connect.chatbot.DTO.conversationDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ChatbotApiResponseDTO {
     @JsonProperty("response")
     private String message;
}
