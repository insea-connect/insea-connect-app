package ma.insea.connect.chat.conversation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {

    List<Conversation> findAllByMember1IdOrMember2Id(String email, String email2);

}

