package ma.insea.connect.chat.common.chatMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


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
}