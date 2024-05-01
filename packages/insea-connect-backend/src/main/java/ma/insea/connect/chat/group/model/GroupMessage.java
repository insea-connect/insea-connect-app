package ma.insea.connect.chat.group.model;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ma.insea.connect.account.model.User;
import ma.insea.connect.chat.common.model.Message;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class GroupMessage extends Message {

    @ManyToOne
    private Group group;

    @ManyToOne
    private User sender;


}
