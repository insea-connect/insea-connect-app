package ma.insea.connect.chat.common.chatMessage;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.user.UserRepository;
import ma.insea.connect.user.User;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final UserRepository userRepository;

    @MessageMapping("/chat.sendmessage")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.saveusermessage(chatMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }
    @MessageMapping("/chat.sendgroupmessage")
    @SendTo("/user/public")
    public ChatMessage processGroupMessage(@Payload ChatMessage chatMessage) {
        chatMessageService.savegroupmessage(chatMessage);   
        return chatMessage;
    }


    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findUserChatMessages(@PathVariable String senderId,
                                                 @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
    
    
}
