package ma.insea.connect.chatbot.service;


import lombok.extern.slf4j.Slf4j;
import ma.insea.connect.chat.common.chatMessage.DTO.ChatMessageDTO;
import ma.insea.connect.chat.common.chatMessage.service.ChatMessageService;
import ma.insea.connect.chat.common.chatMessage.DTO.GroupMessageDTO;
import ma.insea.connect.chatbot.DTO.conversationDTO.ChatbotMessageRequestDTO;
import ma.insea.connect.chatbot.DTO.conversationDTO.ChatbotApiResponseDTO;
import ma.insea.connect.chatbot.DTO.conversationDTO.ChatbotApiRequestDTO;
import ma.insea.connect.chatbot.DTO.conversationDTO.ChatbotMessageResponseDTO;
import ma.insea.connect.chatbot.DTO.CreateThreadDTO;
import ma.insea.connect.chatbot.DTO.groupDTO.ChatbotGroupMessageRequestDTO;
import ma.insea.connect.chatbot.DTO.groupDTO.ChatbotGroupMessageResponseDTO;
import ma.insea.connect.exceptions.chatbot.ChatbotServerException;
import ma.insea.connect.exceptions.chatbot.MessageSaveException;
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
    private ChatMessageService chatMessageService;

    @Value("${chatbotServer}")
    private String chatbotServer;

    private final long chatbotId = 1L;
    private final RestTemplate restTemplate = new RestTemplate();

    public ChatbotMessageResponseDTO addInteractionInConversation(ChatbotMessageRequestDTO chatbotMessageRequestDTO, String responseContent) {
        try {
            Date date = new Date();
            ChatMessageDTO userRequest = new ChatMessageDTO();
            userRequest.setContent(chatbotMessageRequestDTO.getContent());
            userRequest.setTimestamp(chatbotMessageRequestDTO.getTimestamp());
            userRequest.setSenderId(chatbotMessageRequestDTO.getSenderId());
            userRequest.setRecipientId(chatbotMessageRequestDTO.getRecipientId());

            ChatMessageDTO botResponse = new ChatMessageDTO();
            botResponse.setRecipientId(chatbotMessageRequestDTO.getSenderId());
            botResponse.setSenderId(chatbotMessageRequestDTO.getRecipientId());
            botResponse.setContent(responseContent);
            botResponse.setTimestamp(date);

            chatMessageService.saveusermessage(userRequest);
            chatMessageService.saveusermessage(botResponse);

            ChatbotMessageResponseDTO chatbotMessageResponseDTO = new ChatbotMessageResponseDTO();
            chatbotMessageResponseDTO.setMessage(botResponse.getContent());
            return chatbotMessageResponseDTO;
        } catch (Exception e) {
            throw new MessageSaveException("Failed to save user or bot message");
        }
    }

    public ChatbotGroupMessageResponseDTO addInteractionToGroup(ChatbotGroupMessageRequestDTO chatbotGroupMessageRequestDTO, String responseContent) {
        try {
            Date date = new Date();
            GroupMessageDTO userRequest = new GroupMessageDTO();
            userRequest.setContent(chatbotGroupMessageRequestDTO.getContent());
            userRequest.setTimestamp(chatbotGroupMessageRequestDTO.getTimestamp());
            userRequest.setSenderId(chatbotGroupMessageRequestDTO.getSenderId());
            userRequest.setGroupId(chatbotGroupMessageRequestDTO.getGroupId());

            GroupMessageDTO botResponse = new GroupMessageDTO();
            botResponse.setGroupId(chatbotGroupMessageRequestDTO.getGroupId());
            botResponse.setSenderId(chatbotId);
            botResponse.setContent(responseContent);
            botResponse.setTimestamp(date);

            chatMessageService.savegroupmessage(userRequest);
            chatMessageService.savegroupmessage(botResponse);

            ChatbotGroupMessageResponseDTO chatbotGroupMessageResponseDTO = new ChatbotGroupMessageResponseDTO();
            chatbotGroupMessageResponseDTO.setMessage(botResponse.getContent());
            return chatbotGroupMessageResponseDTO;
        } catch (Exception e) {
            throw new MessageSaveException("Failed to save group message");
        }
    }

    public ResponseEntity<CreateThreadDTO> getThreadId() {
        String url = chatbotServer + "/start_conversation";
        HttpEntity<String> request = new HttpEntity<>(new String());
        try {
            ResponseEntity<CreateThreadDTO> response = restTemplate.postForEntity(url, request, CreateThreadDTO.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                throw new ChatbotServerException("Failed to start conversation");
            }
        } catch (Exception e) {
            throw new ChatbotServerException("Error communicating with chatbot server");
        }
    }

    public String getThreadIdString() {
        String url = chatbotServer + "/start_conversation";
        HttpEntity<String> request = new HttpEntity<>(new String());
        try {
            ResponseEntity<CreateThreadDTO> response = restTemplate.postForEntity(url, request, CreateThreadDTO.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                CreateThreadDTO createThreadDTO = response.getBody();
                if (createThreadDTO != null && createThreadDTO.getThread_id() != null) {
                    return createThreadDTO.getThread_id();
                } else {
                    throw new ChatbotServerException("Invalid thread response from chatbot server");
                }
            } else {
                throw new ChatbotServerException("Failed to start conversation");
            }
        } catch (Exception e) {
            throw new ChatbotServerException("Error communicating with chatbot server");
        }
    }

    public ChatbotApiResponseDTO redirect(ChatbotApiRequestDTO requestDTO) {
        String url = chatbotServer + "/process_request";
        HttpEntity<ChatbotApiRequestDTO> requestEntity = new HttpEntity<>(requestDTO);
        try {
            ResponseEntity<ChatbotApiResponseDTO> responseEntity = restTemplate.postForEntity(url, requestEntity, ChatbotApiResponseDTO.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                throw new ChatbotServerException("Failed to get response from chatbot server");
            }
        } catch (Exception e) {
            throw new ChatbotServerException("Error communicating with chatbot server");
        }
    }

    public ChatbotMessageResponseDTO sendToBotConversation(ChatbotMessageRequestDTO chatbotMessageRequestDTO) {
        ChatbotApiRequestDTO chatbotApiRequestDTO = new ChatbotApiRequestDTO(chatbotMessageRequestDTO.getThreadId(), chatbotMessageRequestDTO.getContent());
        ChatbotApiResponseDTO responseFromApi = redirect(chatbotApiRequestDTO);
        return addInteractionInConversation(chatbotMessageRequestDTO, responseFromApi.getMessage());
    }

    public ChatbotGroupMessageResponseDTO sendToBotGroup(ChatbotGroupMessageRequestDTO chatbotGroupMessageRequestDTO) {
        ChatbotApiRequestDTO chatbotApiRequestDTO = new ChatbotApiRequestDTO(chatbotGroupMessageRequestDTO.getThreadId(), chatbotGroupMessageRequestDTO.getContent());
        ChatbotApiResponseDTO responseFromApi = redirect(chatbotApiRequestDTO);
        return addInteractionToGroup(chatbotGroupMessageRequestDTO, responseFromApi.getMessage());
    }
}
