package ma.insea.connect.chat.group.DTO;


import lombok.Data;
import ma.insea.connect.chat.common.chatMessage.DTO.GroupMessageDTO;
@Data
public class GroupDTO2 {
    private Long id;
    private String name;
    private GroupMessageDTO lastMessage;
}
