package ma.insea.connect.user;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.chat.conversation.ConversationDTO;
import ma.insea.connect.chat.conversation.ConversationService;
import ma.insea.connect.chat.group.Group;
import ma.insea.connect.chat.group.GroupService;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final ConversationService conversationService;

    @MessageMapping("/users.addUser")
    @SendTo("/user/public")
    public User addUser(
            @Payload User user
    ) {
        User user1=userRepository.findByEmail(user.getEmail());
        if(user1==null)
        {
            user.setStatus(Status.ONLINE);
            userService.saveUser(user);
        }
        else
        {
            user1.setStatus(Status.ONLINE);
            userService.saveUser(user1);
        }
        return user;
    }

    @MessageMapping("/users.disconnectUser")
    @SendTo("/users/public")
    public User disconnectUser(
            @Payload User user
    ) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/users/{myId}/groups")
    public ResponseEntity<List<Group>> getGroupsByEmail(@PathVariable Long myId) { 
        List<Group> groups = groupService.findallgroupsofemail(myId);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserByEmail(@PathVariable Long id) { 
        return ResponseEntity.ok(userRepository.findById(id).get());
    }

    @GetMapping("/users/{myId}/conversations")
    public ResponseEntity<List<ConversationDTO>> getUserConversations(@PathVariable Long myId) {
        List<ConversationDTO> conversations = conversationService.findConversationsByEmail(myId);
        return ResponseEntity.ok(conversations);
    }

}
