package ma.insea.connect.chatbot.controller;


import ma.insea.connect.chatbot.service.ChatbotService;
import ma.insea.connect.chatbot.DTO.ChatbotMessageRequestDTO;
import ma.insea.connect.chatbot.DTO.ChatbotMessageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("api/v1/chatbot")
public class ChatbotController {

    @Value("${chatbotServer}")
    private String chatbotServer;

    private final RestTemplate restTemplate = new RestTemplate() ;
    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/sendMessage")
    public ResponseEntity<ChatbotMessageResponseDTO> redirect(@RequestBody ChatbotMessageRequestDTO requestDTO) {
        try {
            ChatbotMessageResponseDTO chatbotMessageResponseDTO= chatbotService.sendToBot(requestDTO);
            return ResponseEntity.ok(chatbotMessageResponseDTO);
        }

        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage(), e);
        }



    }

}