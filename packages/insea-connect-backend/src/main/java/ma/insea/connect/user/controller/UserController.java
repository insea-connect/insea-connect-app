package ma.insea.connect.user.controller;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.chat.conversation.Conversation;
import ma.insea.connect.chat.conversation.ConversationDTO;
import ma.insea.connect.chat.conversation.ConversationService;
import ma.insea.connect.chat.group.DTO.GroupDTO2;
import ma.insea.connect.chat.group.service.GroupService;
import ma.insea.connect.exceptions.user.UserNotFoundException;
import ma.insea.connect.keycloak.DTO.AddKeycloakDTO;
import ma.insea.connect.keycloak.controller.KeyCloakController;
import ma.insea.connect.keycloak.service.KeyCloakService;
import ma.insea.connect.user.DTO.AddUserDTO;
import ma.insea.connect.user.DTO.UserDTO;
import ma.insea.connect.user.DTO.UserInfoResponseDTO;
import ma.insea.connect.user.model.Status;
import ma.insea.connect.user.model.User;
import ma.insea.connect.user.repository.UserRepository;
import ma.insea.connect.user.service.UserService;
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
import java.util.Optional;

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



    @MessageMapping("/users.addUser")
    @SendTo("/user/public")
    public ResponseEntity<AddUserDTO> addUser(@Payload AddUserDTO addUserDTO) {
        userService.addUser(addUserDTO);
        return ResponseEntity.ok(addUserDTO);
    }

    @PostMapping("/user/addUser")
    public ResponseEntity<AddUserDTO> addUser1(@RequestBody AddUserDTO addUserDTO) {
        userService.addUser(addUserDTO);
        return ResponseEntity.ok(addUserDTO);
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
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/users/me")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String email = jwt.getClaimAsString("email");
        User user = userService.getUserInfo(email);
        UserInfoResponseDTO userInfoResponseDTO = UserInfoResponseDTO.mapToUserInfoDTO(user);
        return ResponseEntity.ok(userInfoResponseDTO);
    }

    @GetMapping("/users/me/conversations")
    public ResponseEntity<List<ConversationDTO>> getUserConversations() {
        List<ConversationDTO> conversations = conversationService.findConversationsByEmail();
        return ResponseEntity.ok(conversations);
    }

    @PostMapping("/user/me/conversations")
    public ResponseEntity<Conversation> createConversation(@RequestBody Map<String,Long> recipientId) {
        return ResponseEntity.ok(conversationService.createConversation(recipientId.get("recipientId")));

    }
}
