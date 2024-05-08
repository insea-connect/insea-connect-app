package ma.insea.connect.chat.common.chatMessage;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import ma.insea.connect.chat.conversation.Conversation;
import ma.insea.connect.chat.conversation.ConversationRepository;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final GroupMessageRepository groupMessageRepository;

    public ChatMessage saveusermessage(ChatMessageDTO chatMessage) {
        ChatMessage chatMessage1 = new ChatMessage();
        var chatId = getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage1.setChatId(chatId);

        User recipient = userRepository.findByEmail(chatMessage.getRecipientId());
        User sender = userRepository.findByEmail(chatMessage.getSenderId());

        chatMessage1.setSender(sender);
        chatMessage1.setRecipient(recipient);
        chatMessage1.setContent(chatMessage.getContent());
        chatMessage1.setTimestamp(new Date());

        chatMessageRepository.save(chatMessage1);

        Conversation conversation = new Conversation();
        conversation.setChatId(chatId);
        conversation.setMember1(chatMessage1.getSender());
        conversation.setMember2(chatMessage1.getRecipient());
        conversationRepository.save(conversation);
        return chatMessage1;
    }
    public GroupMessage savegroupmessage(GroupMessageDTO groupMessageDTO) {
        User sender = userRepository.findById(groupMessageDTO.getSenderId()).get();
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setGroupId(groupMessageDTO.getGroupId());
        groupMessage.setSender(sender);
        groupMessage.setContent(groupMessageDTO.getContent());
        groupMessage.setTimestamp(new Date());
        groupMessageRepository.save(groupMessage);
        return groupMessage;
    }
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = getChatRoomId(senderId, recipientId, true);
        return chatMessageRepository.findByChatId(chatId);
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
    public List<GroupMessage> findGroupMessages(Long groupId) {
        return groupMessageRepository.findByGroupId(groupId);
    }
    
}
