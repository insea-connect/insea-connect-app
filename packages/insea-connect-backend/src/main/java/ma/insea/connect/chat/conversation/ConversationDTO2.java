package ma.insea.connect.chat.conversation;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import ma.insea.connect.chat.common.chatMessage.ChatMessageDTO;
import ma.insea.connect.user.Status;
import ma.insea.connect.user.UserDTO2;

@Data
@AllArgsConstructor
public class ConversationDTO2 {
    UserDTO2 member1;
    UserDTO2 member2;


}
