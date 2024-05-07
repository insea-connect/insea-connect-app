package ma.insea.connect.chat.group;

import lombok.AllArgsConstructor;
import ma.insea.connect.chat.common.chatMessage.ChatMessage;
import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;



import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final ChatMessageService chatMessageService;

    // @MessageMapping("/groups")
    // @SendTo("/user/public")     //for realtime
    @PostMapping("/groups/")       //for rest anotations
    public GroupDTO addGroup(@RequestBody GroupDTO groupDTO) {
        System.out.println("hayo"+groupDTO);
        groupService.saveGroup(groupDTO);
        return groupDTO;
    }
    @DeleteMapping("/groups/{groupid}")
    public ResponseEntity<String> deleteGroup(@PathVariable("groupid") Long groupId, @RequestBody Map<String, String> user) {
            groupService.deleteGroup(groupId,user.get("name"));
            return ResponseEntity.ok("Group deleted successfully");
    
    }

    @GetMapping("/groups/{groupId}/messages")
    public ResponseEntity<List<ChatMessage>> findGroupChatMessages(@PathVariable String groupId) {
        
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(groupId, groupId));
    }

    @GetMapping("/groups/{groupid}/users")
    public ResponseEntity<List<User>> findGroupUsers(@PathVariable("groupid") Long groupId) {
        List<User> users = groupService.findUsers(groupId);
        return ResponseEntity.ok(users);
    }
    
    @PostMapping("/groups/{groupid}/members")
    public ResponseEntity<String> addGroupMembers(@PathVariable("groupid") Long groupId, @RequestBody Map<String, List<String>>users) {
        groupService.addGroupMembers(groupId, users.get("users"));
        return ResponseEntity.ok("Group members added successfully");
    }

    @DeleteMapping("/groups/{groupid}/members/{memberid}")
    public ResponseEntity<String> removeGroupMember(@PathVariable("groupid") Long groupId, @PathVariable("memberid") String memberId, @RequestBody Map<String, String> user) {
        return groupService.removeGroupMember(groupId, memberId, user.get("name"));
    }
}
