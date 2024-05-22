package ma.insea.connect.chat.common.chatMessage;



import java.util.Date;

import lombok.Data;
@Data
public class GroupMessageDTO {
    private Long senderId;
    private String content;
    private Long groupId;
    private String senderName;
    private Date timestamp;

}