package ma.insea.connect.chat.group;


import lombok.Data;
import ma.insea.connect.chat.common.chatMessage.GroupMessageDTO;
@Data
public class GroupDTO2 {
    private Long id;
    private String name;
    private GroupMessageDTO lastMessage;
}
