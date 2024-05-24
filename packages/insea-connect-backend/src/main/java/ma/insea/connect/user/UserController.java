package ma.insea.connect.user;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.chat.conversation.Conversation;
import ma.insea.connect.chat.conversation.ConversationDTO;
import ma.insea.connect.chat.conversation.ConversationRepository;
import ma.insea.connect.chat.conversation.ConversationService;
import ma.insea.connect.chat.group.GroupDTO2;
import ma.insea.connect.chat.group.GroupService;
import ma.insea.connect.keycloak.DTO.AddKeycloakDTO;
import ma.insea.connect.keycloak.controller.KeyCloakController;
import ma.insea.connect.keycloak.service.KeyCloakService;
import ma.insea.connect.user.DTO.AddUserDTO;
import ma.insea.connect.user.DTO.UserInfoResponseDTO;
import ma.insea.connect.utils.Functions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final KeyCloakService keyCloakService;
    private final UserService userService;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final ConversationService conversationService;
    private  final KeyCloakController keyCloakController;
    private final Functions functions;
    private final ChatMessageService chatMessageService;
    private final ConversationRepository conversationRepository;

    @MessageMapping("/users.addUser")
    @SendTo("/user/public")
    public AddUserDTO addUser(@Payload AddUserDTO addUserDTO) {
        User existingUser = userRepository.findByEmail(addUserDTO.getEmail());
        if (existingUser == null) {

            addUserDTO.setStatus(Status.ONLINE);
            User user = AddUserDTO.mapToUser(addUserDTO);
            userService.saveUser(user);
            AddKeycloakDTO addKeycloakDTO = AddKeycloakDTO.mapToAddKeycloakDTO(addUserDTO);
            keyCloakService.addUser(addKeycloakDTO);

        } else {
            existingUser.setStatus(Status.ONLINE);
            AddKeycloakDTO addKeycloakDTO = AddKeycloakDTO.mapToAddKeycloakDTO(addUserDTO);
            keyCloakService.addUser(addKeycloakDTO);
            userService.saveUser(existingUser);
        }
        return addUserDTO;
    }

    @PostMapping("/user/addUser")
    public AddKeycloakDTO addUser1(@RequestBody AddUserDTO addUserDTO) {
        keyCloakController.addUser(AddKeycloakDTO.mapToAddKeycloakDTO(addUserDTO));

        User existingUser = userRepository.findByEmail(addUserDTO.getEmail());
        if (existingUser == null) {

            addUserDTO.setStatus(Status.ONLINE);
            User user = AddUserDTO.mapToUser(addUserDTO);
            userService.saveUser(user);


        } else {
            existingUser.setStatus(Status.ONLINE);
            userService.saveUser(existingUser);
        }
        return AddKeycloakDTO.mapToAddKeycloakDTO(addUserDTO);
    }


    @MessageMapping("/users.disconnectUser")
    @SendTo("/users/public")
    public User disconnectUser(@Payload User user) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }


   @GetMapping("/users/me/groups")
    public ResponseEntity<List<GroupDTO2>> getGroupsByEmail() {
        return ResponseEntity.ok(groupService.findallgroupsofemail());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id).get());
    }
    @GetMapping("/users/me")
    public ResponseEntity<UserInfoResponseDTO> getUserById(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String email = jwt.getClaimAsString("email");
        UserInfoResponseDTO userInfoResponseDTO = UserInfoResponseDTO.mapToUserInfoDTO(userRepository.findByEmail(email)) ;
        return ResponseEntity.ok(userInfoResponseDTO);
    }

    @GetMapping("/users/me/conversations")
    public ResponseEntity<List<ConversationDTO>> getUserConversations() {
        return ResponseEntity.ok(conversationService.findConversationsByEmail());
    }

    @PostMapping("/users/me/conversations")
    public ResponseEntity<CreateConversationDTO> createConversation(@RequestBody Map<String,Long> body) {
        User connectedUser = functions.getConnectedUser();
        String chatId=chatMessageService.getChatRoomId(Long.toString(connectedUser.getId()),Long.toString(body.get("recipientId")),true);
        if(conversationRepository.existsById(chatId))
        {return ResponseEntity.ok(new CreateConversationDTO(chatId));
        }
        else{
            conversationService.createConversation(body.get("recipientId"));
            return ResponseEntity.created(null).build();
        }

    }
    @GetMapping("users/{userId}/status")
    public ResponseEntity<OnlineDTO> getUserStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserStatus(userId));
    }
    @PutMapping("users/me/heartbeat")
    public ResponseEntity<Void> updateUserHeartbeat(@Payload Map<String,Status> status) {
        userService.updateUserLastSeen(status.get("status"));
        return ResponseEntity.ok().build();
    }
}
