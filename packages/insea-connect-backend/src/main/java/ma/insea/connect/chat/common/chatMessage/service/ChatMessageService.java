package ma.insea.connect.chat.common.chatMessage.service;

import lombok.AllArgsConstructor;

import ma.insea.connect.chat.common.chatMessage.DTO.ChatMessageDTO;
import ma.insea.connect.chat.common.chatMessage.DTO.ChatMessageDTO2;
import ma.insea.connect.chat.common.chatMessage.DTO.GroupMessageDTO;
import ma.insea.connect.chat.common.chatMessage.model.ChatMessage;
import ma.insea.connect.chat.common.chatMessage.model.GroupMessage;
import ma.insea.connect.chat.common.chatMessage.repository.ChatMessageRepository;
import ma.insea.connect.chat.common.chatMessage.repository.GroupMessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import ma.insea.connect.chat.group.model.Membership;
import ma.insea.connect.chat.group.repository.MembershipRepository;
import ma.insea.connect.user.model.User;
import ma.insea.connect.user.repository.UserRepository;
import ma.insea.connect.utils.Functions;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final Functions functions;
    private final MembershipRepository membershipRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessage saveusermessage(ChatMessageDTO chatMessage) {

        ChatMessage chatMessage1 = new ChatMessage();
        var chatId = getChatRoomId(Long.toString(chatMessage.getSenderId()),Long.toString(chatMessage.getRecipientId()), true);
        chatMessage1.setChatId(chatId);

        User recipient = userRepository.findById(chatMessage.getRecipientId()).get();
        User sender = userRepository.findById(chatMessage.getSenderId()).get();

        chatMessage1.setSender(sender);
        chatMessage1.setRecipient(recipient);
        chatMessage1.setContent(chatMessage.getContent());
        chatMessage1.setTimestamp(new java.sql.Date(System.currentTimeMillis()));

        chatMessageRepository.save(chatMessage1);

        messagingTemplate.convertAndSendToUser(
                chatId, "/queue/messages",
                new ChatMessageDTO2(
                        chatMessage1.getSender().getId(),
                        chatMessage1.getContent(),
                        new java.util.Date(System.currentTimeMillis()),
                        chatMessage1.getSender().getUsername())
        );
        return chatMessage1;

    }
    public GroupMessage savegroupmessage(GroupMessageDTO groupMessageDTO) {
        User sender = userRepository.findById(groupMessageDTO.getSenderId()).get();
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setSender(sender);
        groupMessage.setContent(groupMessageDTO.getContent());
        groupMessage.setGroupId(groupMessageDTO.getGroupId());
        groupMessage.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
        groupMessageRepository.save(groupMessage);

        messagingTemplate.convertAndSendToUser(
            Long.toString(groupMessageDTO.getGroupId()), "/queue/messages",
            new GroupMessageDTO(
                    groupMessage.getSender().getId(),
                    groupMessage.getContent(),
                    groupMessage.getGroupId(),
                    groupMessage.getSender().getUsername(),
                    new Date(System.currentTimeMillis())
            )
        );
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
    public ChatMessageDTO findLastMessage(String chatId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatId(chatId);
        if (chatMessages.size() > 0) {
            ChatMessage c=chatMessages.get(chatMessages.size() - 1);
            ChatMessageDTO chatMessageDTO=new ChatMessageDTO();
            chatMessageDTO.setContent(c.getContent());
            chatMessageDTO.setTimestamp(c.getTimestamp());
            chatMessageDTO.setSenderId(c.getSender().getId());
            chatMessageDTO.setRecipientId(c.getRecipient().getId());
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
            groupMessageDTO.setGroupId(groupMessage.getGroupId());
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
