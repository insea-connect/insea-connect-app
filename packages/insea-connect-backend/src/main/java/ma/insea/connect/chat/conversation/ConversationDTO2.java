package ma.insea.connect.chat.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;
import ma.insea.connect.user.DTO.UserDTO2;

@Data
@AllArgsConstructor
public class ConversationDTO2 {
    UserDTO2 member1;
    UserDTO2 member2;


}
