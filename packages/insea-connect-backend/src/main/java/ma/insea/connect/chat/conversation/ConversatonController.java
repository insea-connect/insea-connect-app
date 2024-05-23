package ma.insea.connect.chat.conversation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.insea.connect.chat.common.chatMessage.DTO.ChatMessageDTO2;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ConversatonController {
    private final ConversationService conversationService;

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageDTO2>> findConversationMessages(@PathVariable String conversationId
                                                 ) {
        return ResponseEntity
                .ok(conversationService.findConversationMessages(conversationId));
    }

    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<ConversationDTO2> getConversationInfo(@PathVariable String conversationId
                                                 ) {
        return ResponseEntity
                .ok(conversationService.getConversation(conversationId));
    }
    
}
