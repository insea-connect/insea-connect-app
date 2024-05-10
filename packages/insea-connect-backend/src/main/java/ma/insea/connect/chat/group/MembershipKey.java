package ma.insea.connect.chat.group;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MembershipKey implements Serializable {

    @Column(name = "User_id")
    Long userId;

    @Column(name = "group_id")
    Long groupId;

    protected MembershipKey(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
    protected MembershipKey() {
    }

}
