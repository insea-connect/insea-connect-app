package ma.insea.connect.chat.common.chatMessage;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import ma.insea.connect.chat.conversation.Conversation;
import ma.insea.connect.chat.conversation.ConversationRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;

    public ChatMessage saveusermessage(ChatMessage chatMessage) {
        var chatId = getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        Conversation conversation = new Conversation();
        conversation.setChatId(chatId);
        conversation.setMember1Id(chatMessage.getSenderId());
        conversation.setMember2Id(chatMessage.getRecipientId());
        conversationRepository.save(conversation);
        return chatMessage;
    }
    public ChatMessage savegroupmessage(ChatMessage chatMessage) {
        var chatId = getChatRoomId(chatMessage.getRecipientId(), chatMessage.getRecipientId(), true);

        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        return chatMessage;
    }
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = getChatRoomId(senderId, recipientId, true);
        return repository.findByChatId(chatId);
    }

    public void deleteChatMessages(String chatId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAll();
        for (ChatMessage chatMessage : chatMessages) {
            if (chatMessage.getChatId().equals(chatId)) {
                chatMessageRepository.delete(chatMessage);
            }
        }
    }

    public String getChatRoomId(
        String senderId,
        String recipientId,
        boolean createNewRoomIfNotExists
) {
    var first = senderId.compareTo(recipientId) < 0 ? senderId : recipientId;
    var second = senderId.compareTo(recipientId) < 0 ? recipientId : senderId;
    var chatId = String.format("%s_%s",first, second);
            return chatId;
}
    public ChatMessage findLastMessage(String chatId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatId(chatId);
        if (chatMessages.size() > 0) {
            return chatMessages.get(chatMessages.size() - 1);
        }
        return null;
    }
    
}
