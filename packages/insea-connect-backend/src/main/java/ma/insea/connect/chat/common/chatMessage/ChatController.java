package ma.insea.connect.chat.common.chatMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/sendmessage")
    public ChatMessage processMessage(@RequestBody ChatMessageDTO chatMessage) {
        return chatMessageService.saveusermessage(chatMessage);
    }
    @MessageMapping("/sendgroupmessage")
    public GroupMessage processGroupMessage(@RequestBody GroupMessageDTO groupMessage) {
        return chatMessageService.savegroupmessage(groupMessage);
         
    }
}