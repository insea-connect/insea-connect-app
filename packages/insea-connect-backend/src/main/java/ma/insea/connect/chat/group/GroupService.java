package ma.insea.connect.chat.group;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ma.insea.connect.chat.common.chatMessage.ChatMessageDTO;
import ma.insea.connect.chat.common.chatMessage.ChatMessageDTO2;
import ma.insea.connect.chat.common.chatMessage.ChatMessageService;
import ma.insea.connect.chat.common.chatMessage.GroupMessageDTO;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserDTO2;
import ma.insea.connect.user.UserRepository;


@Service
@RequiredArgsConstructor
public class GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final ChatMessageService chatMessageService;

    public Group saveGroup(GroupDTO groupDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User connectedUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        
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
        return group;
    }
    public List<GroupDTO2> findallgroupsofemail(Long myId) {
        List<Membership> memberships = membershipRepository.findByUserId(myId);
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
        return groupDTOs;
    }
    public void deleteGroup(Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);

        Group group = groupRepository.findById(groupId).get();
        if (user == group.getCreator()){
            groupRepository.delete(group);    
        }
    }
    public Group findById(Long groupId) {
        Group group=groupRepository.findById(groupId).get();
        return group;
    }
    public List<UserDTO2> findUsers(Long groupId) {
        List<Membership> membership = membershipRepository.findAllByGroupId(groupId);
        List<UserDTO2> groupMembersDTOs = new ArrayList<UserDTO2>();
        for (Membership m : membership) {
            User user = m.getUser();
            UserDTO2 userDTO = new UserDTO2(user.getId(), user.getUsername(), user.getEmail());
            groupMembersDTOs.add(userDTO);
        }


        
        return groupMembersDTOs;

    }
    public String addGroupMembers(Long groupId, List<Long> users) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User connectedUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        
        if(membership == null || !membership.getIsAdmin()) {
            return "You are not allowed to add members to this group";
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
        return "Group members added successfully";
    }
        
    }
    @Transactional
    public String removeGroupMember(Long groupId, Long memberId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User connectedUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        
        if(membership == null || !membership.getIsAdmin()) {
            return "You are not allowed to add members to this group";
        }else{
        membershipRepository.deleteByGroupIdAndUserId(groupId, memberId);
        return "Group member removed successfully";}
    }
    public GroupDTO3 getGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        User creator = group.getCreator();
        UserDTO2 creatorDTO = new UserDTO2(creator.getId(), creator.getUsername(), creator.getEmail());
        List <UserDTO2> admins= new ArrayList<UserDTO2>();
        List <Membership> adminsmem=membershipRepository.findByGroupIdAndIsAdmin(groupId, true);
        for (Membership admin : adminsmem) {
            UserDTO2 adminDTO = new UserDTO2(admin.getUser().getId(), admin.getUser().getUsername(), admin.getUser().getEmail());
            admins.add(adminDTO);
        }
        return new GroupDTO3(group.getId(), group.getImagrUrl(), group.getName(), group.getDescription(), group.getIsOfficial(), group.getCreatedDate(), creatorDTO, admins);
        
    }
    
}
