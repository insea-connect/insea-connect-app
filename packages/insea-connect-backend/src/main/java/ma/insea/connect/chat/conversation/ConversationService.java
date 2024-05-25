package ma.insea.connect.chat.conversation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Collections;

import org.springframework.stereotype.Service;

import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserDTO2;
import ma.insea.connect.user.UserRepository;
import ma.insea.connect.utils.Functions;
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
    private final Functions functions;

    public List<ConversationDTO> findConversationsByEmail() {
        User connectedUser = functions.getConnectedUser();
        String email=connectedUser.getEmail();

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
        Collections.reverse(conversationDTOs);
        conversationDTOs.sort(Comparator.comparing(
        conversationDTO -> ((ConversationDTO) conversationDTO).getLastMessage() != null ? ((ConversationDTO) conversationDTO).getLastMessage().getTimestamp() : new Date(0)).reversed());
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
        User connectedUser = functions.getConnectedUser();
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

        Conversation conversation = conversationRepository.findByChatId(conversationId);
        User user1=conversation.getMember1();
        User user2=conversation.getMember2();

        if (connectedUser.equals(user1)||connectedUser.equals(user2)) {
            return chatMessageDTOs;
        }
        return null;
    }

    public ConversationDTO2 getConversation(String conversationId) {
        User connectedUser = functions.getConnectedUser();
        Conversation conversation = conversationRepository.findByChatId(conversationId);
        User user1=conversation.getMember1();
        User user2=conversation.getMember2();

        UserDTO2 userDTO1=new UserDTO2(user1.getId(), user1.getUsername(), user1.getEmail());
        UserDTO2 userDTO2=new UserDTO2(user2.getId(), user2.getUsername(), user2.getEmail());
        if (connectedUser.equals(user1)||connectedUser.equals(user2)) {
            return new ConversationDTO2(userDTO1, userDTO2);
        }
        return null;





        
    }

    public Conversation createConversation(Long long1) {
        User connectedUser = functions.getConnectedUser();
        User user2=userRepository.findById(long1).get();
        String chatId=chatMessageService.getChatRoomId(connectedUser.getId().toString(), user2.getId().toString(), true);
        Conversation conversation = new Conversation();
        conversation.setChatId(chatId);
        conversation.setMember1(connectedUser);
        conversation.setMember2(user2);
        conversationRepository.save(conversation);
        return conversation;
    }
    
}
