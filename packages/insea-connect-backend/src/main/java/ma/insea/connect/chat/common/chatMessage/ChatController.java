package ma.insea.connect.chat.common.chatMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
// @RequestMapping("/api/v1")
public class ChatController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/sendmessage")
    public ChatMessage processMessage(@RequestBody ChatMessageDTO chatMessage) {
        return chatMessageService.saveusermessage(chatMessage);
    }
    @MessageMapping("/sendgroupmessage")
    // @SendTo("/user/public")
    public GroupMessage processGroupMessage(@Payload GroupMessageDTO groupMessage) {
        return chatMessageService.savegroupmessage(groupMessage);
         
    }
}