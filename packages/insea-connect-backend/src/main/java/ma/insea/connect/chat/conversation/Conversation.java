package ma.insea.connect.chat.conversation;

import lombok.Data;
import lombok.NoArgsConstructor;
import ma.insea.connect.user.User;
import lombok.AllArgsConstructor;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    @Id
    private String chatId;

    @ManyToOne
    @JoinColumn(name="member1_id", nullable=true)
    @JsonIgnore
    private User member1;

    @ManyToOne
    @JoinColumn(name="member2_id", nullable=true)
    @JsonIgnore
    private User member2;

    
}
