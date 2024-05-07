package ma.insea.connect.chat.conversation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ma.insea.connect.chat.common.chatMessage.ChatMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ConversatonController {
    private final ConversationService conversationService;

    @GetMapping("/conversation/{conversationId}/messages")
    public ResponseEntity<List<ChatMessage>> findConversationMessages(@PathVariable String conversationId
                                                 ) {
        return ResponseEntity
                .ok(conversationService.findConversationMessages(conversationId));
    }
    
}
