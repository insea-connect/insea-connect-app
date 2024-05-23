package ma.insea.connect.chatbot.DTO;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class CreateThreadDTO {
    private String run_id;
    private String thread_id ;
}
