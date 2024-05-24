package ma.insea.connect.chat.group;

import lombok.AllArgsConstructor;
import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.chat.common.chatMessage.GroupMessageDTO;
import ma.insea.connect.user.UserDTO3;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class GroupController {

    private final GroupService groupService;
    private final ChatMessageService chatMessageService;

    @PostMapping("/groups")
    public GroupDTO addGroup(@RequestBody GroupDTO groupDTO) {
        return groupService.saveGroup(groupDTO);
    }
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable("groupId") Long groupId) {
            return ResponseEntity.ok(groupService.deleteGroup(groupId));//cases for response ent
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<GroupDTO3> getGroupInfo(@PathVariable("groupId") Long groupId) {
            
            return ResponseEntity.ok(groupService.getGroup(groupId));//cases for response ent
    }

    @GetMapping("/groups/{groupId}/messages")
    public ResponseEntity<List<GroupMessageDTO>> findGroupChatMessages(@PathVariable Long groupId) {
        
        return ResponseEntity
                .ok(chatMessageService.findGroupMessages(groupId));
    }

    @GetMapping("/groups/{groupId}/members")
    public ResponseEntity<List<UserDTO3>> findGroupUsers(@PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(groupService.findUsers(groupId));
    }
    
    @PostMapping("/groups/{groupId}/members")
    public ResponseEntity<String> addGroupMembers(@PathVariable("groupId") Long groupId, @RequestBody Map<String, List<Long>>users) {
        
        return ResponseEntity.ok(groupService.addGroupMembers(groupId, users.get("members")));
    }

    @DeleteMapping("/groups/{groupId}/members/{memberid}")
    public ResponseEntity<String> removeGroupMember(@PathVariable("groupId") Long groupId, @PathVariable("memberid") Long memberId) {
        return ResponseEntity.ok(groupService.removeGroupMember(groupId, memberId));
    }
    @PostMapping("/groups/{groupId}/admin")
    public ResponseEntity<?> addAdmin(@RequestBody Map<String, Long> userId, @PathVariable("groupId") Long groupId) {
        groupService.addAdmin(groupId, userId.get("userId"));
        return ResponseEntity.ok(null);    
    }
    @DeleteMapping("/groups/{groupId}/admin/{userId}")
    public ResponseEntity<?> removeAdmin(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {
        groupService.removeAdmin(groupId, userId);
        return ResponseEntity.ok(null);    
    }
    
}
