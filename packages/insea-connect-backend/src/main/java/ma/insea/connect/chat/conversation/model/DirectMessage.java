package ma.insea.connect.chat.conversation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ma.insea.connect.account.model.User;
import ma.insea.connect.chat.common.model.Message;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DirectMessage extends Message {

    @ManyToOne
    private Conversation conversation;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;
}
