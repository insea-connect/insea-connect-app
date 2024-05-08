// package ma.insea.connect;

// import ma.insea.connect.chat.common.chatMessage.ChatController;
// import ma.insea.connect.chat.common.chatMessage.ChatMessage;
// import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
//     import static org.mockito.ArgumentMatchers.isNull;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;

// @SpringBootTest
// public class ChatControllerTest {

//     @MockBean
//     private SimpMessagingTemplate simpMessagingTemplate;

//     @MockBean
//     private ChatMessageService chatMessageService;


// @Test
// public void testSendMessage() {
//     ChatController chatController = new ChatController(simpMessagingTemplate, chatMessageService);

//     ChatMessage chatMessage = new ChatMessage();
//     chatMessage.setSenderId("testUser");
//     chatMessage.setContent("Hello, World!");

//     Mockito.when(chatMessageService.saveusermessage(any(ChatMessage.class))).thenReturn(chatMessage);

//     chatController.processMessage(chatMessage);

//     verify(simpMessagingTemplate, times(1)).convertAndSendToUser(isNull(), any(String.class), any(Object.class));
// }
// }