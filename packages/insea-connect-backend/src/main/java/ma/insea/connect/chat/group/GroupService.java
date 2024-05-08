package ma.insea.connect.chat.group;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;


@Service
@RequiredArgsConstructor
public class GroupService {
    private final UserRepository UserRepository;
    private final GroupRepository groupRepository;
    private final ChatMessageService chatMessageService;

    public Group saveGroup(GroupDTO groupDTO) {
        Group group = new Group();
        
        group.setName(groupDTO.getName());
        group.setCreator(groupDTO.getCreator());
        group.setUsers(groupDTO.getMembers());
        group.setIsOffecial(false);
        group.setDescription(groupDTO.getDescription());
        final List<Long> admins = new ArrayList<Long>();
        admins.add(groupDTO.getCreator());
        group.setAdmins(admins);
        group.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
        groupRepository.save(group);

        for (Long user : groupDTO.getMembers()) {
            User user1 = UserRepository.findById(user).get();
            user1.addGroup(group.getId());
            UserRepository.save(user1);
        }
        
        return group;
    }
    public List<Group> findallgroupsofemail(Long myId) {
        return groupRepository.findAllByUser(myId);
    }
    public void deleteGroup(Long groupId, Long myId) {
        Group group = groupRepository.findById(groupId).get();
        if (group.getCreator().equals(myId)) {
            groupRepository.delete(group);
            for (Long user : group.getUsers()) {
                User user1 = UserRepository.findById(user).get();
                user1.removeGroup(groupId);
                UserRepository.save(user1);
            }
            var chatId = chatMessageService.getChatRoomId(String.valueOf(groupId),String.valueOf(groupId), true);
            chatMessageService.deleteChatMessages(chatId);
        }
    }
    public Group findById(Long groupId) {
        Group group=groupRepository.findById(groupId).get();
        return group;
    }
    public List<User> findUsers(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        List<User> users = new ArrayList<User>();
        for (Long user : group.getUsers()) {
            users.add(UserRepository.findById(user).get());
        }
        return users;

    }
    public void addGroupMembers(Long groupId, List<Long> users) {
        //test if the connected user is an admin
        Group group = groupRepository.findById(groupId).get();
        for (Long user : users) {
            if (!group.getUsers().contains(user)){
            group.getUsers().add(user);
            User user1 = UserRepository.findById(user).get();
            user1.addGroup(groupId);
            UserRepository.save(user1);
        }
        }
        groupRepository.save(group);
        
    }
    public ResponseEntity<String>   removeGroupMember(Long groupId, Long memberId, Long me) {
        Group group = groupRepository.findById(groupId).get();
        //test if admin
        if (!group.getAdmins().contains(me)) {
            return ResponseEntity.badRequest().body("Only Admins can remove members");
        }else{
        group.getUsers().remove(memberId);
        User user = UserRepository.findById(memberId).get();
        user.removeGroup(groupId);
        UserRepository.save(user);
        groupRepository.save(group);
        return ResponseEntity.ok("Member removed successfully");
        }
    }
}
