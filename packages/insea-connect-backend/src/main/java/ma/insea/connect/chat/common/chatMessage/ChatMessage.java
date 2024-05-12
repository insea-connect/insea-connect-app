package ma.insea.connect.chat.common.chatMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.insea.connect.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String chatId;
    private String content;
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name="sender_id", nullable=false)
    @JsonIgnore
    private User sender;
    @ManyToOne
    @JoinColumn(name="recipient_id", nullable=false)
    @JsonIgnore
    private User recipient;
    
}
