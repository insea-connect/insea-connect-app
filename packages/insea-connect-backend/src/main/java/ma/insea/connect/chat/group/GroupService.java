package ma.insea.connect.chat.group;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.chat.common.chatMessage.GroupMessageDTO;
import ma.insea.connect.exception.UnauthorizedException;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserDTO3;
import ma.insea.connect.user.UserRepository;
import ma.insea.connect.utils.Functions;


@Service
@RequiredArgsConstructor
public class GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final ChatMessageService chatMessageService;
    private final Functions functions;

    public GroupDTO saveGroup(GroupDTO groupDTO) {
        User connectedUser = functions.getConnectedUser();        
        Group group = new Group();
        
        group.setName(groupDTO.getName());
        group.setCreator(connectedUser);
        group.setIsOfficial(false);
        group.setDescription(groupDTO.getDescription());
        group.setCreatedDate(new java.util.Date(System.currentTimeMillis()));
        groupRepository.save(group);
        List<Long> mem = groupDTO.getMembers();
        mem.add(connectedUser.getId());
        group.setMemberships(new ArrayList<Membership>());
        for (Long user : mem) {
            Membership m = new Membership();
            m.setId(new MembershipKey(user, group.getId()));
            m.setGroup(group);
            m.setUser(userRepository.findById(user).get());
            m.setIsAdmin(false);
            m.setJoiningDate(new java.util.Date(System.currentTimeMillis()));
            group.addMembership(m);
        }
        groupRepository.save(group);
        Membership m=membershipRepository.findById(new MembershipKey(group.getCreator().getId(), group.getId())).get();
        m.setIsAdmin(true);
        membershipRepository.save(m);
        groupDTO.setId(group.getId());
        return groupDTO;
    }
    public List<GroupDTO2> findallgroupsofemail() {
        User connectedUser = functions.getConnectedUser();
        List<Membership> memberships = membershipRepository.findByUserId(connectedUser.getId());
        List<Group> groups = new ArrayList<Group>();
        for (Membership membership : memberships) {
            groups.add(membership.getGroup());
        }
        List<GroupDTO2> groupDTOs = new ArrayList<GroupDTO2>();
        for (Group group : groups) {
            GroupDTO2 groupDTO = new GroupDTO2();
            groupDTO.setId(group.getId());
            groupDTO.setName(group.getName());
            GroupMessageDTO chatMessage=chatMessageService.findLastGroupMessage(group.getId());
            groupDTO.setLastMessage(chatMessage);
            groupDTOs.add(groupDTO);
        }
        Collections.reverse(groupDTOs);
        groupDTOs.sort(Comparator.comparing(
        groupDTO2 -> ((GroupDTO2) groupDTO2).getLastMessage() != null ? ((GroupDTO2) groupDTO2).getLastMessage().getTimestamp() : new Date(0)).reversed());
        
    
        return groupDTOs;
    }
    public String deleteGroup(Long groupId) {
        User connectedUser = functions.getConnectedUser();

        Group group = groupRepository.findById(groupId).get();
        if (connectedUser == group.getCreator()){
            groupRepository.delete(group);  
            return "Group deleted successfully";  
        }
        return "You are not allowed to delete this group";
    }
    public Group findById(Long groupId) {
        Group group=groupRepository.findById(groupId).get();
        return group;
    }
    public List<UserDTO3> findUsers(Long groupId) {
        List<Membership> membership = membershipRepository.findAllByGroupId(groupId);
        List<UserDTO3> groupMembersDTOs = new ArrayList<UserDTO3>();
        for (Membership m : membership) {
            User user = m.getUser();
            UserDTO3 userDTO = new UserDTO3(user.getId(), user.getUsername(), user.getEmail(), m.getIsAdmin(), m.getUser().getId() == m.getGroup().getCreator().getId());
            groupMembersDTOs.add(userDTO);
        }
        User connectedUser = functions.getConnectedUser();
        Membership membership2 = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        if (membership2 == null) {
            return null;
        }
        return groupMembersDTOs;

    }
    public Boolean addGroupMembers(Long groupId, List<Long> users) {
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        
        if(membership == null || !membership.getIsAdmin()) {
            return false;
        }else{
        for (Long user : users) {
            Membership m = new Membership();
            m.setId(new MembershipKey(user, groupId));
            m.setGroup(groupRepository.findById(groupId).get());
            m.setUser(userRepository.findById(user).get());
            m.setIsAdmin(false);
            m.setJoiningDate(new java.util.Date(System.currentTimeMillis()));
            membershipRepository.save(m);
        }
        return true;
    }
        
    }
    @Transactional
    public Boolean removeGroupMember(Long groupId, Long memberId) {
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        
        if(membership == null || !membership.getIsAdmin()) {
            return false;
        }else{
        membershipRepository.deleteByGroupIdAndUserId(groupId, memberId);
        return true;}
    }
    public GroupDTO3 getGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        if(membership == null) {
            return null;
        }
        return new GroupDTO3(group.getId(), group.getImagrUrl(), group.getName(), group.getDescription(), group.getIsOfficial(), group.getCreatedDate());
        
    }
    public void addAdmin(Long groupId, Long long1) {
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        Membership membership2 = membershipRepository.findByUserIdAndGroupId(long1, groupId);
        if (membership2 == null) {
            throw new UnauthorizedException("User is not a member of this group");
        }
        else if(membership == null || !membership.getIsAdmin()) {
            throw new UnauthorizedException("You are not allowed to add admins to this group");
        }else{
            Membership m=membershipRepository.findById(new MembershipKey(long1, groupId)).get();
            m.setIsAdmin(true);
            membershipRepository.save(m);
        }
    }
    public void removeAdmin(Long groupId, Long long1) {
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        Membership membership2 = membershipRepository.findByUserIdAndGroupId(long1, groupId);
        if (membership2 == null) {
            throw new UnauthorizedException("User is not a member of this group");
        }
        else if(membership == null || !membership.getIsAdmin()) {
            throw new UnauthorizedException("You are not allowed to remove admins from this group");
        }else{
            Membership m=membershipRepository.findById(new MembershipKey(long1, groupId)).get();
            m.setIsAdmin(false);
            membershipRepository.save(m);
        }
    }
}
