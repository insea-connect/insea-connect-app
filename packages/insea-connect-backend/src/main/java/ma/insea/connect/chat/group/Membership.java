package ma.insea.connect.chat.group;

import java.util.Date;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.insea.connect.user.model.User;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Membership {

    @EmbeddedId
    MembershipKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    Group group;

    Date joiningDate;
    Boolean isAdmin;
}