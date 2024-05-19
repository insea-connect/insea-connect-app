package ma.insea.connect.chat.conversation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.insea.connect.user.User;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {

    List<Conversation> findAllByMember1OrMember2(User email, User email2);

}