package ma.insea.connect.chat.common.chatMessage;



import lombok.Data;
@Data
public class GroupMessageDTO {
    private Long senderId;
    private Long groupId;
    private String content;

}