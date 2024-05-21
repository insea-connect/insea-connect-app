package ma.insea.connect.chat.common.chatMessage;

import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupNotification {
    private Long groupId;
    private Long senderId;
    private String content;
    private Date timestamp;
    private Boolean isGroup;
}
