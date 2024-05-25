package ma.insea.connect.chat.common.chatMessage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.insea.connect.user.User;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class GroupMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private Long groupId;
    @Column(name = "content", length = 10000)
    private String content;
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name="sender_id", nullable=false)
    @JsonIgnore
    private User sender;   
}
