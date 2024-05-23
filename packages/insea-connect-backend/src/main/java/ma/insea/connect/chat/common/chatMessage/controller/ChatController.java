package ma.insea.connect.chat.common.chatMessage.controller;

import lombok.RequiredArgsConstructor;

import ma.insea.connect.chat.common.chatMessage.DTO.ChatMessageDTO;
import ma.insea.connect.chat.common.chatMessage.DTO.GroupMessageDTO;
import ma.insea.connect.chat.common.chatMessage.model.ChatMessage;
import ma.insea.connect.chat.common.chatMessage.model.GroupMessage;
import ma.insea.connect.chat.common.chatMessage.service.ChatMessageService;
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