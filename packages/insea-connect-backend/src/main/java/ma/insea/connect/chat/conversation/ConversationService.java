package ma.insea.connect.chat.conversation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;
import ma.insea.connect.chat.common.chatMessage.ChatMessage;
import ma.insea.connect.chat.common.chatMessage.ChatMessageDTO;
import ma.insea.connect.chat.common.chatMessage.ChatMessageDTO2;
import ma.insea.connect.chat.common.chatMessage.ChatMessageRepository;


import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ChatMessageService chatMessageService;
    private final ChatMessageRepository chatMessageRepository;

    public List<ConversationDTO> findConversationsByEmail(String email) {
        User user2=userRepository.findByEmail(email);
        System.out.println("useer" + user2);
        List<Conversation> conversations = conversationRepository.findAllByMember1OrMember2(user2,user2);
        

        List<ConversationDTO> conversationDTOs = new ArrayList<>();
        for(Conversation conversation:conversations)
        {
            ChatMessageDTO chatMessage=chatMessageService.findLastMessage(conversation.getChatId());

            String member1=conversation.getMember1().getEmail();
            String member2=conversation.getMember2().getEmail();
            String recepientId=member1.equals(email)?member2:member1;
            User user=userRepository.findByEmail(recepientId);



            ConversationDTO conversationDTO=new ConversationDTO();

            conversationDTO.setChatId(conversation.getChatId());
            conversationDTO.setUsername(user.getUsername());
            conversationDTO.setLastLogin(user.getLastLogin());
            conversationDTO.setStatus(user.getStatus());
            conversationDTO.setLastMessage(chatMessage);

            
            conversationDTOs.add(conversationDTO);
        }
        return conversationDTOs;
    

    }

    public List<ConversationDTO> findConversationsByID(Long myId) {
        User user2=userRepository.findById(myId).get();
        List<Conversation> conversations = conversationRepository.findAllByMember1OrMember2(user2,user2);


        List<ConversationDTO> conversationDTOs = new ArrayList<>();
        for(Conversation conversation:conversations)
        {
            ChatMessageDTO chatMessage=chatMessageService.findLastMessage(conversation.getChatId());

            Long member1=conversation.getMember1().getId();
            Long member2=conversation.getMember2().getId();
            Long recepientId=member1.equals(myId)?member2:member1;
            User user=userRepository.findById(recepientId).get();

            ConversationDTO conversationDTO=new ConversationDTO();

            conversationDTO.setChatId(conversation.getChatId());
            conversationDTO.setRecipientId(recepientId);
            conversationDTO.setUsername(user.getUsername());
            conversationDTO.setLastLogin(user.getLastLogin());
            conversationDTO.setStatus(user.getStatus());
            conversationDTO.setLastMessage(chatMessage);


            conversationDTOs.add(conversationDTO);
        }
        return conversationDTOs;


    }

    public List<ChatMessageDTO2> findConversationMessages(String conversationId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatId(conversationId);
        List<ChatMessageDTO2> chatMessageDTOs = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            ChatMessageDTO2 chatMessageDTO = new ChatMessageDTO2();
            chatMessageDTO.setContent(chatMessage.getContent());
            chatMessageDTO.setTimestamp(chatMessage.getTimestamp());
            chatMessageDTO.setSenderId(chatMessage.getSender().getId());
            chatMessageDTO.setSenderName(chatMessage.getSender().getUsername());
            chatMessageDTOs.add(chatMessageDTO);
        }
        return chatMessageDTOs;}
    
}
