package ma.insea.connect.chat.common.chatMessage;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
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
    @MessageMapping("/conversation/typing")
    public ResponseEntity<TypingDTO> chatTyping(@RequestBody TypingDTO body) {
        return ResponseEntity.ok(chatMessageService.chatTyping(body));
    }
    @MessageMapping("/group/Typing")
    public ResponseEntity<GroupTypingDTO> grouptyping(@RequestBody GroupTypingDTO body) {
        return ResponseEntity.ok(chatMessageService.groupTyping(body));
    }
}