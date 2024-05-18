package ma.insea.connect.user;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.chat.conversation.ConversationDTO;
import ma.insea.connect.chat.conversation.ConversationService;
import ma.insea.connect.chat.group.Group;
import ma.insea.connect.chat.group.GroupService;
import ma.insea.connect.keycloak.DTO.AddKeycloakDTO;
import ma.insea.connect.keycloak.controller.KeyCloakController;
import ma.insea.connect.keycloak.service.KeyCloakService;
import ma.insea.connect.user.DTO.AddUserDTO;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.LoggingPermission;

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
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/user/{myId}/groups")
    public ResponseEntity<List<Group>> getGroupsByEmail(@PathVariable Long myId) {
        List<Group> groups = groupService.findallgroupsofemail(myId);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        return ResponseEntity.ok(user);
    }

   @GetMapping("/users/me/groups")
    public ResponseEntity<List<Group>> getGroupsByEmail(@AuthenticationPrincipal Jwt jwt) {
       if (jwt == null) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
       }
       String email = jwt.getClaimAsString("email");
       User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(groupService.findallgroupsofemail(user.getId()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id).get());
    }
    @GetMapping("/users/me")
    public ResponseEntity<User> getUserById(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String email = jwt.getClaimAsString("email");
        return ResponseEntity.ok(userRepository.findByEmail(email));
    }

    @GetMapping("/users/me/conversations")
    public ResponseEntity<List<ConversationDTO>> getUserConversations(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String email = jwt.getClaimAsString("email");
        List<ConversationDTO> conversations = conversationService.findConversationsByEmail(email);
        return ResponseEntity.ok(conversations);
    }
}
