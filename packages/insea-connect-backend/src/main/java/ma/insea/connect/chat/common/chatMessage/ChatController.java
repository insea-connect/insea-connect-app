package ma.insea.connect.chat.common.chatMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @PostMapping("/sendmessage")
    public ChatMessage processMessage(@RequestBody ChatMessageDTO chatMessage) {
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
        return savedMsg;
    }
    @PostMapping("/sendgroupmessage")
    // @SendTo("/user/public")
    public GroupMessage processGroupMessage(@RequestBody GroupMessageDTO2 groupMessage) {
        return chatMessageService.savegroupmessage(groupMessage);//add send only to ur grp
        
    }
}