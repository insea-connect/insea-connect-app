package ma.insea.connect.chat.common.chatMessage;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import ma.insea.connect.chat.conversation.Conversation;
import ma.insea.connect.chat.conversation.ConversationRepository;
import ma.insea.connect.chat.group.Membership;
import ma.insea.connect.chat.group.MembershipRepository;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;
import ma.insea.connect.utils.Functions;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final Functions functions;
    private final MembershipRepository membershipRepository;

    public ChatMessage saveusermessage(ChatMessageDTO chatMessage) {
        ChatMessage chatMessage1 = new ChatMessage();
        var chatId = getChatRoomId(Long.toString(chatMessage.getSenderId()),Long.toString(chatMessage.getRecipientId()), true);
        chatMessage1.setChatId(chatId);

        User recipient = userRepository.findById(chatMessage.getRecipientId()).get();
        User sender = userRepository.findById(chatMessage.getSenderId()).get();
        // User sender = functions.getConnectedUser();

        chatMessage1.setSender(sender);
        chatMessage1.setRecipient(recipient);
        chatMessage1.setContent(chatMessage.getContent());
        chatMessage1.setTimestamp(new java.util.Date(System.currentTimeMillis()));

        chatMessageRepository.save(chatMessage1);

        Conversation conversation = new Conversation();
        conversation.setChatId(chatId);
        conversation.setMember1(chatMessage1.getSender());
        conversation.setMember2(chatMessage1.getRecipient());
        conversationRepository.save(conversation);
        return chatMessage1;
    }
    public GroupMessage savegroupmessage(GroupMessageDTO2 groupMessageDTO) {
        User sender = userRepository.findById(groupMessageDTO.getSenderId()).get();
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setSender(sender);
        groupMessage.setGroupId(groupMessageDTO.getGroupId());
        groupMessage.setContent(groupMessageDTO.getContent());
        groupMessage.setTimestamp(new java.util.Date(System.currentTimeMillis()));
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
    public ChatMessageDTO2 findLastMessage(String chatId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatId(chatId);
        if (chatMessages.size() > 0) {
            ChatMessage c=chatMessages.get(chatMessages.size() - 1);
            ChatMessageDTO2 chatMessageDTO=new ChatMessageDTO2();
            chatMessageDTO.setContent(c.getContent());
            chatMessageDTO.setTimestamp(c.getTimestamp());
            chatMessageDTO.setSenderId(c.getSender().getId());
            chatMessageDTO.setSenderName(c.getSender().getUsername());
            // chatMessageDTO.setRecipientId(c.getRecipient().getId());
            return chatMessageDTO;
        }
        return null;
    }
    public List<GroupMessageDTO> findGroupMessages(Long groupId) {
        List<GroupMessage> groupMessages = groupMessageRepository.findByGroupId(groupId);
        List<GroupMessageDTO> groupMessages2 = new ArrayList<GroupMessageDTO>();
        for (GroupMessage groupMessage : groupMessages) {
            GroupMessageDTO groupMessageDTO = new GroupMessageDTO();
            groupMessageDTO.setContent(groupMessage.getContent());
            groupMessageDTO.setTimestamp(groupMessage.getTimestamp());
            groupMessageDTO.setSenderId(groupMessage.getSender().getId());
            groupMessageDTO.setSenderName(groupMessage.getSender().getUsername());
            groupMessages2.add(groupMessageDTO);
        }
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        if (membership != null) {
            return groupMessages2;
        }
        return null;
    }
    public GroupMessageDTO findLastGroupMessage(Long groupId) {
        List<GroupMessage> groupMessages = groupMessageRepository.findByGroupId(groupId);
        if (groupMessages.size() > 0) {
            GroupMessage  groupMessage2=groupMessages.get(groupMessages.size() - 1);
            GroupMessageDTO groupMessageDTO=new GroupMessageDTO();
            groupMessageDTO.setContent(groupMessage2.getContent());
            groupMessageDTO.setTimestamp(groupMessage2.getTimestamp());
            groupMessageDTO.setSenderId(groupMessage2.getSender().getId());
            groupMessageDTO.setSenderName(groupMessage2.getSender().getUsername());
            return groupMessageDTO;
        }
        return null;}
    
}
