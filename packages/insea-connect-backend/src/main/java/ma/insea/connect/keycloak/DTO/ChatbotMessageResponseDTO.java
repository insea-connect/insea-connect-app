package ma.insea.connect.keycloak.DTO;

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
