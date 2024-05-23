package ma.insea.connect.chat.common.chatMessage.DTO;



import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageDTO {
    private Long senderId;
    private String content;
    private Long groupId;
    private String senderName;
    private Date timestamp;

}