package ma.insea.connect.chat.common.chatMessage.repository;

import ma.insea.connect.chat.common.chatMessage.model.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {

    List<GroupMessage> findByGroupId(Long groupId);
}