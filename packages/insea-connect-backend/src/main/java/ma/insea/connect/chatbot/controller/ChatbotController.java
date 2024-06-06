package ma.insea.connect.chatbot.controller;


import lombok.extern.slf4j.Slf4j;
import ma.insea.connect.chatbot.DTO.groupDTO.ChatbotGroupMessageRequestDTO;
import ma.insea.connect.chatbot.DTO.groupDTO.ChatbotGroupMessageResponseDTO;
import ma.insea.connect.chatbot.service.ChatbotService;
import ma.insea.connect.chatbot.DTO.conversationDTO.ChatbotMessageRequestDTO;
import ma.insea.connect.chatbot.DTO.conversationDTO.ChatbotMessageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@RestController
@RequestMapping("api/v1/chatbot")
public class ChatbotController {

    @Value("${chatbotServer}")
    private String chatbotServer;

    private final RestTemplate restTemplate = new RestTemplate() ;
    @Autowired
    private ChatbotService chatbotService;

    private String cleanResponseMessage(String message) {
        String regex = "【\\d+:\\d+†source】";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        log.info("Formatted text without crosses "+matcher.replaceAll("") );
        return matcher.replaceAll("");
    }


    @PostMapping("/sendMessage/conversation/stream")
    public SseEmitter sendToBotConversationStream(@RequestBody ChatbotMessageRequestDTO requestDTO) {
        SseEmitter emitter = new SseEmitter();

        new Thread(() -> {
            try {
                // Get the full response from the chatbot service
                ChatbotMessageResponseDTO responseDTO = chatbotService.sendToBotConversation(requestDTO);

                // Clean the response message
                String cleanedMessage = cleanResponseMessage(responseDTO.getMessage());

                // Stream the response word by word
                String[] words = cleanedMessage.split(" ");
                for (String word : words) {
                    emitter.send(SseEmitter.event().data(word));
                    Thread.sleep(150); // Adjust delay as needed
                }
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            } catch (Exception e) {
                emitter.completeWithError(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e));
            }
        }).start();

        return emitter;
    }
    @PostMapping("/sendMessage/conversation")
    public ResponseEntity<ChatbotMessageResponseDTO> sendToBotConversation(@RequestBody ChatbotMessageRequestDTO requestDTO) {
        try {
            ChatbotMessageResponseDTO chatbotMessageResponseDTO= chatbotService.sendToBotConversation(requestDTO);
            chatbotMessageResponseDTO.setMessage(cleanResponseMessage(chatbotMessageResponseDTO.getMessage()));
            log.info("the formated text included in the message body "+ chatbotMessageResponseDTO.getMessage());
            log.info("the text it should have is  "+cleanResponseMessage(chatbotMessageResponseDTO.getMessage()));
            return ResponseEntity.ok(chatbotMessageResponseDTO);
        }

        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e);
        }



    }


    @PostMapping("/sendMessage/group")
    public ResponseEntity<ChatbotGroupMessageResponseDTO> sendToBotGroup(@RequestBody ChatbotGroupMessageRequestDTO requestDTO) {
        try {
            ChatbotGroupMessageResponseDTO chatbotGroupMessageResponseDTO= chatbotService.sendToBotGroup(requestDTO);
            return ResponseEntity.ok(chatbotGroupMessageResponseDTO);
        }

        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e);
        }



    }

}