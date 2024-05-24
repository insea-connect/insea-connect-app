package ma.insea.connect.chat.group.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MembershipKey implements Serializable {

    @Column(name = "User_id")
    Long userId;

    @Column(name = "group_id")
    Long groupId;

    public MembershipKey(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
    public MembershipKey() {
    }

}
