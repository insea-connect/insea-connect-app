package ma.insea.connect.chat.group;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository  extends JpaRepository<Membership,MembershipKey> {

    List<Membership> findByUserId(Long myId);

    void deleteByGroupId(Long groupId);

    void deleteAllByGroupId(Long groupId);

    List<Membership> findAllByGroupId(Long groupId);

    void deleteByGroupIdAndUserId(Long groupId, Long memberId);

    Membership findByUserIdAndGroupId(Long id, Long groupId);

    List<Membership> findByGroupIdAndIsAdmin(Long groupId, boolean b);
}