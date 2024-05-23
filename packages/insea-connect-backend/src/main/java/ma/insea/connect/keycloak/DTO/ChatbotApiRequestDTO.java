package ma.insea.connect.keycloak.DTO;


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
