package ma.insea.connect.chat.common.chatMessage;



import java.util.Date;

import lombok.Data;
@Data
public class GroupMessageDTO2 {
    private Long groupId;
    private Long senderId;
    private String content;
}