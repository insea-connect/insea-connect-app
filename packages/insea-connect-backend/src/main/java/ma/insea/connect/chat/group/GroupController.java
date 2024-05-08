package ma.insea.connect.chat.group;

import lombok.AllArgsConstructor;
import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.chat.common.chatMessage.GroupMessage;
import ma.insea.connect.user.User;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final ChatMessageService chatMessageService;

    // @MessageMapping("/groups")
    // @SendTo("/user/public")     //for realtime
    @PostMapping("/groups")       //for rest anotations
    public GroupDTO addGroup(@RequestBody GroupDTO groupDTO) {
        groupService.saveGroup(groupDTO);
        return groupDTO;
    }
    @DeleteMapping("/groups/{groupid}")
    public ResponseEntity<String> deleteGroup(@PathVariable("groupid") Long groupId, @RequestBody Map<String, Long> user) {
            groupService.deleteGroup(groupId,user.get("id"));
            return ResponseEntity.ok("Group deleted successfully");//cases for response ent
    
    }

    @GetMapping("/groups/{groupId}/messages")
    public ResponseEntity<List<GroupMessage>> findGroupChatMessages(@PathVariable Long groupId) {
        
        return ResponseEntity
                .ok(chatMessageService.findGroupMessages(groupId));
    }

    @GetMapping("/groups/{groupid}/members")
    public ResponseEntity<List<User>> findGroupUsers(@PathVariable("groupid") Long groupId) {
        List<User> users = groupService.findUsers(groupId);
        return ResponseEntity.ok(users);
    }
    
    @PostMapping("/groups/{groupid}/members")
    public ResponseEntity<String> addGroupMembers(@PathVariable("groupid") Long groupId, @RequestBody Map<String, List<Long>>users) {
        groupService.addGroupMembers(groupId, users.get("users"));
        return ResponseEntity.ok("Group members added successfully");
    }

    @DeleteMapping("/groups/{groupid}/members/{memberid}")
    public ResponseEntity<String> removeGroupMember(@PathVariable("groupid") Long groupId, @PathVariable("memberid") Long memberId, @RequestBody Map<String, Long> user) {
        return groupService.removeGroupMember(groupId, memberId, user.get("name"));
    }
}
