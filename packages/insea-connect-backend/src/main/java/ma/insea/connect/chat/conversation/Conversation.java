package ma.insea.connect.chat.conversation;

import lombok.Data;
import lombok.NoArgsConstructor;
import ma.insea.connect.user.User;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_conversation",schema = "testo")
public class Conversation {
    @Id
    private String chatId;
    private String member1Id;
    private String member2Id;

    
}
