package ma.insea.connect.chat.common.chatMessage.repository;

import ma.insea.connect.chat.common.chatMessage.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatId(String chatId);
}