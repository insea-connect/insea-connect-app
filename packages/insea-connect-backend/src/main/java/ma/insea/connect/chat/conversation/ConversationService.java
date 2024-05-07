package ma.insea.connect.chat.conversation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;
import ma.insea.connect.chat.common.chatMessage.ChatMessage;
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
        List<Conversation> conversations = conversationRepository.findAllByMember1IdOrMember2Id(email,email);
        System.out.println("hey2"+conversations);

        List<ConversationDTO> conversationDTOs = new ArrayList<>();
        for(Conversation conversation:conversations)
        {
            ChatMessage chatMessage=chatMessageService.findLastMessage(conversation.getChatId());
            String recepient=conversation.getMember1Id().equals(email)?conversation.getMember2Id():conversation.getMember1Id();
            User user=userRepository.findByEmail(recepient);

            ConversationDTO conversationDTO=new ConversationDTO();

            conversationDTO.setChatId(conversation.getChatId());
            conversationDTO.setEmail(recepient);
            conversationDTO.setUsername(user.getUsername());
            conversationDTO.setLastLogin(user.getLastLogin());
            conversationDTO.setStatus(user.getStatus());
            conversationDTO.setLastMessage(chatMessage);
            System.out.println("hey3"+conversationDTO);

            
            conversationDTOs.add(conversationDTO);
        }
        return conversationDTOs;
    

    }

    public List<ChatMessage> findConversationMessages(String conversationId) {
        return chatMessageRepository.findByChatId(conversationId);
        }
    
}
