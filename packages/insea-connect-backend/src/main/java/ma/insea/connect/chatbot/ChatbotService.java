package ma.insea.connect.chatbot;


import lombok.extern.slf4j.Slf4j;
import ma.insea.connect.chat.common.chatMessage.ChatMessageDTO;
import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.keycloak.DTO.ChatbotApiResponseDTO;
import ma.insea.connect.keycloak.DTO.ChatbotApiRequestDTO;
import ma.insea.connect.keycloak.DTO.ChatbotMessageResponseDTO;
import ma.insea.connect.keycloak.DTO.CreateThreadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Slf4j
@Service
public class ChatbotService {
    @Autowired
    ChatMessageService chatMessageService;
    @Value("${chatbotServer}")
    private String chatbotServer;
    private final RestTemplate restTemplate = new RestTemplate();

    public ChatbotMessageResponseDTO addInteraction(ChatbotMessageDTO chatbotMessageDTO, String responseContent){

            Date date = new Date();
            ChatMessageDTO userRequest = new ChatMessageDTO();
            userRequest.setContent(chatbotMessageDTO.getContent());
            userRequest.setTimestamp(chatbotMessageDTO.getTimestamp());
            userRequest.setSenderId(chatbotMessageDTO.getSenderId());
            userRequest.setRecipientId(chatbotMessageDTO.getRecipientId());
            ChatMessageDTO botResponse = new ChatMessageDTO();
            botResponse.setRecipientId(chatbotMessageDTO.getSenderId());
            botResponse.setSenderId(chatbotMessageDTO.getRecipientId());
            botResponse.setContent(responseContent);
            botResponse.setTimestamp(date);
            chatMessageService.saveusermessage(userRequest);
            chatMessageService.saveusermessage(botResponse);
            ChatbotMessageResponseDTO chatbotMessageResponseDTO = new ChatbotMessageResponseDTO();
            chatbotMessageResponseDTO.setMessage(botResponse.getContent());
            return chatbotMessageResponseDTO;
        }






    public ResponseEntity<CreateThreadDTO> getThreadId() {
        String url = chatbotServer + "/start_conversation";
        HttpEntity<String> request = new HttpEntity<>(new String());  // Prepare request. Update this if you need to send a body.
        ResponseEntity<CreateThreadDTO> response = restTemplate.postForEntity(url, request, CreateThreadDTO.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Assuming the response body is properly mapped to CreateThreadDTO
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }


    public ChatbotApiResponseDTO redirect(ChatbotApiRequestDTO requestDTO) {
        String url = chatbotServer + "/process_request";

        // Prepare the request entity with headers and body
        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatbotApiRequestDTO> requestEntity = new HttpEntity<>(requestDTO);

        // Send the request and get the response
        ResponseEntity<ChatbotApiResponseDTO> responseEntity = restTemplate.postForEntity(url, requestEntity, ChatbotApiResponseDTO.class);
        log.warn("after mapping "+responseEntity.getBody().toString());
        // Return the response body if the request is successful
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            // Handle the error scenario appropriately
            throw new RuntimeException("Failed to get response from chatbot server");
        }
    }


    public ChatbotMessageResponseDTO sendToBot(ChatbotMessageDTO chatbotMessageDTO){

        ChatbotApiRequestDTO  chatbotApiRequestDTO= new ChatbotApiRequestDTO(chatbotMessageDTO.getThreadId(), chatbotMessageDTO.getContent());
        ChatbotApiResponseDTO responseFromApi = redirect(chatbotApiRequestDTO);
        ChatbotMessageResponseDTO chatbotMessageResponseDTO = addInteraction(chatbotMessageDTO, responseFromApi.getMessage());
        return chatbotMessageResponseDTO;

    }
}
