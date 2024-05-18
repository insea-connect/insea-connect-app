package ma.insea.connect.user;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.chat.conversation.ConversationDTO;
import ma.insea.connect.chat.conversation.ConversationService;
import ma.insea.connect.chat.group.Group;
import ma.insea.connect.chat.group.GroupService;
import ma.insea.connect.keycloak.DTO.UserDTO;
import ma.insea.connect.keycloak.service.KeyCloakService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final KeyCloakService keyCloakService;
    private final UserService userService;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final ConversationService conversationService;

    @MessageMapping("/users.addUser")
    @SendTo("/user/public")
    public User addUser(@Payload User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null) {
            user.setStatus(Status.ONLINE);
            userService.saveUser(user);
            UserDTO userDTO = UserDTO.mapToUserDTO(user);
            keyCloakService.addUser(userDTO);
        } else {
            existingUser.setStatus(Status.ONLINE);
            UserDTO userDTO = UserDTO.mapToUserDTO(existingUser);
            keyCloakService.addUser(userDTO);
            userService.saveUser(existingUser);
        }
        return user;
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
    public ResponseEntity<List<Group>> getGroupsByEmail() {
        KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext = principal.getKeycloakSecurityContext();
        String email = keycloakSecurityContext.getToken().getEmail();
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

    @GetMapping("/users/me/conversations")
    public ResponseEntity<List<ConversationDTO>> getUserConversations() {
        KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext = principal.getKeycloakSecurityContext();
        String email = keycloakSecurityContext.getToken().getEmail();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<ConversationDTO> conversations = conversationService.findConversationsByEmail(user.getId());
        return ResponseEntity.ok(conversations);
    }
}
