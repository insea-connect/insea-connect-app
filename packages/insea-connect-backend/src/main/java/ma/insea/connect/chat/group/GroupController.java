package ma.insea.connect.chat.group;

import lombok.AllArgsConstructor;
import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.chat.common.chatMessage.GroupMessageDTO;
import ma.insea.connect.user.UserDTO2;

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

    // @MessageMapping("/groups")
    // @SendTo("/user/public")     //for realtime
    @PostMapping("/groups")       //for rest anotations
    public GroupDTO addGroup(@RequestBody GroupDTO groupDTO) {
        
        return groupService.saveGroup(groupDTO);
    }
    @DeleteMapping("/groups/{groupid}")
    public ResponseEntity<String> deleteGroup(@PathVariable("groupid") Long groupId) {
            return ResponseEntity.ok(groupService.deleteGroup(groupId));//cases for response ent
    }

    @GetMapping("/groups/{groupid}")
    public ResponseEntity<GroupDTO3> getGroupInfo(@PathVariable("groupid") Long groupId) {
            
            return ResponseEntity.ok(groupService.getGroup(groupId));//cases for response ent
    }

    @GetMapping("/groups/{groupId}/messages")
    public ResponseEntity<List<GroupMessageDTO>> findGroupChatMessages(@PathVariable Long groupId) {
        
        return ResponseEntity
                .ok(chatMessageService.findGroupMessages(groupId));
    }

    @GetMapping("/groups/{groupid}/members")
    public ResponseEntity<List<UserDTO2>> findGroupUsers(@PathVariable("groupid") Long groupId) {
        return ResponseEntity.ok(groupService.findUsers(groupId));
    }
    
    @PostMapping("/groups/{groupid}/members")
    public ResponseEntity<String> addGroupMembers(@PathVariable("groupid") Long groupId, @RequestBody Map<String, List<Long>>users) {
        
        return ResponseEntity.ok(groupService.addGroupMembers(groupId, users.get("members")));
    }

    @DeleteMapping("/groups/{groupid}/members/{memberid}")
    public ResponseEntity<String> removeGroupMember(@PathVariable("groupid") Long groupId, @PathVariable("memberid") Long memberId) {
        return ResponseEntity.ok(groupService.removeGroupMember(groupId, memberId));
    }
}
