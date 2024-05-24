package ma.insea.connect.chat.group.controller;

import lombok.AllArgsConstructor;
import ma.insea.connect.chat.common.chatMessage.service.ChatMessageService;
import ma.insea.connect.chat.common.chatMessage.DTO.GroupMessageDTO;
import ma.insea.connect.chat.group.DTO.GroupDTO;
import ma.insea.connect.chat.group.DTO.GroupDTO3;
import ma.insea.connect.chat.group.service.GroupService;
import ma.insea.connect.user.DTO.UserDTO2;

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
    @PostMapping("/groups/{groupid}/admin")
    public ResponseEntity<?> addAdmin(@RequestBody Map<String, Long> userId, @PathVariable("groupid") Long groupId) {
        groupService.addAdmin(groupId, userId.get("userId"));
        return ResponseEntity.ok(null);    
    }
    @DeleteMapping("/groups/{groupid}/admin")
    public ResponseEntity<?> removeAdmin(@RequestBody Map<String, Long> userId, @PathVariable("groupid") Long groupId) {
        groupService.removeAdmin(groupId, userId.get("userId"));
        return ResponseEntity.ok(null);    
    }
    
}
