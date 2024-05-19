package ma.insea.connect.chat.common.chatMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendmessage")
    public void processMessage(@Payload ChatMessageDTO chatMessage) {
        ChatMessage savedMsg = chatMessageService.saveusermessage(chatMessage);

        messagingTemplate.convertAndSendToUser(
                Long.toString(chatMessage.getRecipientId()), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSender().getEmail(),
                        savedMsg.getRecipient().getEmail(),
                        savedMsg.getContent()
                )
        );
    }
    @MessageMapping("/chat.sendgroupmessage")
    @SendTo("/user/public")
    public GroupMessage processGroupMessage(@Payload GroupMessageDTO groupMessage) {
        GroupMessage groupMessage2=chatMessageService.savegroupmessage(groupMessage);
        return groupMessage2;
    }


    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findUserChatMessages(@PathVariable String senderId,
                                                                  @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }



}