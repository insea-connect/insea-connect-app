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
        group.setUsers(groupDTO.getUsers());
        //group.setId(groupDTO.getName() + groupDTO.getCreator() + System.currentTimeMillis());
        group.setIsOffecial(false);
        final List<String> admins = new ArrayList<String>();
        admins.add(groupDTO.getCreator());
        group.setAdmins(admins);
        group.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
        groupRepository.save(group);
        for (String user : groupDTO.getUsers()) {
            User user1 = UserRepository.findByEmail(user);
            user1.addGroup(group.getId());
            UserRepository.save(user1);
        }
        return group;
    }
    public List<Group> findallgroupsofemail(String email) {
        return groupRepository.findAllByUser(email);
    }
    public void deleteGroup(Long groupId, String email) {
        Group group = groupRepository.findById(groupId).get();
        if (group.getCreator().equals(email)) {
            groupRepository.delete(group);
            for (String user : group.getUsers()) {
                User user1 = UserRepository.findByEmail(user);
                user1.removeGroup(groupId);
                UserRepository.save(user1);
            }
            var chatId = chatMessageService.getChatRoomId(String.valueOf(groupId),String.valueOf(groupId), true);
            System.out.println("papa"+chatId);
            chatMessageService.deleteChatMessages(chatId);
        }
        throw new UnsupportedOperationException("Unimplemented method 'deleteGroup'");
    }
    public Group findById(Long groupId) {
        Group group=groupRepository.findById(groupId).get();
        return group;
    }
    public List<User> findUsers(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        List<User> users = new ArrayList<User>();
        for (String user : group.getUsers()) {
            users.add(UserRepository.findByEmail(user));
        }
        return users;

    }
    public void addGroupMembers(Long groupId, List<String> users) {
        Group group = groupRepository.findById(groupId).get();
        for (String user : users) {
            if (!group.getUsers().contains(user)){
            group.getUsers().add(user);
            User user1 = UserRepository.findByEmail(user);
            user1.addGroup(groupId);
            UserRepository.save(user1);
        }
        }
        groupRepository.save(group);
        
    }
    public ResponseEntity<String>   removeGroupMember(Long groupId, String memberId, String email) {
        Group group = groupRepository.findById(groupId).get();
        if (!group.getCreator().equals(email)) {
            return ResponseEntity.badRequest().body("Creator cannot be removed");
        }else{
        group.getUsers().remove(memberId);
        User user = UserRepository.findByEmail(memberId);
        user.removeGroup(groupId);
        UserRepository.save(user);
        groupRepository.save(group);
        return ResponseEntity.ok("Member removed successfully");
        }
    }
}
