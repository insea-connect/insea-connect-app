package ma.insea.connect.chat.group.service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ma.insea.connect.chat.group.DTO.GroupDTO;
import ma.insea.connect.chat.group.DTO.GroupDTO2;
import ma.insea.connect.chat.group.DTO.GroupDTO3;
import ma.insea.connect.chat.group.model.Group;
import ma.insea.connect.chat.group.model.Membership;
import ma.insea.connect.chat.group.model.MembershipKey;
import ma.insea.connect.chat.group.repository.MembershipRepository;
import ma.insea.connect.chat.group.repository.GroupRepository;
import ma.insea.connect.exceptions.UnauthorizedException;
import ma.insea.connect.exceptions.groups.GroupNotFoundException;
import ma.insea.connect.exceptions.groups.MembershipNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ma.insea.connect.chat.common.chatMessage.service.ChatMessageService;
import ma.insea.connect.chat.common.chatMessage.DTO.GroupMessageDTO;
import ma.insea.connect.user.model.User;
import ma.insea.connect.user.DTO.UserDTO2;
import ma.insea.connect.user.repository.UserRepository;
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
        group.setCreatedDate(new Date(System.currentTimeMillis()));
        groupRepository.save(group);

        List<Long> mem = groupDTO.getMembers();
        mem.add(connectedUser.getId());
        group.setMemberships(new ArrayList<>());
        for (Long user : mem) {
            Membership m = new Membership();
            m.setId(new MembershipKey(user, group.getId()));
            m.setGroup(group);
            m.setUser(userRepository.findById(user).orElseThrow(() -> new MembershipNotFoundException("User not found")));
            m.setIsAdmin(false);
            m.setJoiningDate(new Date(System.currentTimeMillis()));
            group.addMembership(m);
        }
        groupRepository.save(group);

        Membership m = membershipRepository.findById(new MembershipKey(group.getCreator().getId(), group.getId()))
                .orElseThrow(() -> new MembershipNotFoundException("Membership not found"));
        m.setIsAdmin(true);
        membershipRepository.save(m);
        groupDTO.setId(group.getId());
        return groupDTO;
    }


    public List<GroupDTO2> findallgroupsofemail() {
        User connectedUser = functions.getConnectedUser();
        List<Membership> memberships = membershipRepository.findByUserId(connectedUser.getId());
        List<Group> groups = new ArrayList<>();
        for (Membership membership : memberships) {
            groups.add(membership.getGroup());
        }
        List<GroupDTO2> groupDTOs = new ArrayList<>();
        for (Group group : groups) {
            GroupDTO2 groupDTO = new GroupDTO2();
            groupDTO.setId(group.getId());
            groupDTO.setName(group.getName());
            GroupMessageDTO chatMessage = chatMessageService.findLastGroupMessage(group.getId());
            groupDTO.setLastMessage(chatMessage);
            groupDTOs.add(groupDTO);
        }
        return groupDTOs;
    }
    public String deleteGroup(Long groupId) {
        User connectedUser = functions.getConnectedUser();
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found"));
        if (connectedUser.equals(group.getCreator())) {
            groupRepository.delete(group);
            return "Group deleted successfully";
        }
        throw new UnauthorizedException("You are not allowed to delete this group");
    }
    public Group findById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found"));
    }

    public List<UserDTO2> findUsers(Long groupId) {
        List<Membership> memberships = membershipRepository.findAllByGroupId(groupId);
        List<UserDTO2> groupMembersDTOs = new ArrayList<>();
        for (Membership m : memberships) {
            User user = m.getUser();
            UserDTO2 userDTO = new UserDTO2(user.getId(), user.getUsername(), user.getEmail());
            groupMembersDTOs.add(userDTO);
        }
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        if (membership == null) {
            throw new UnauthorizedException("You are not a member of this group");
        }
        return groupMembersDTOs;
    }


    public String addGroupMembers(Long groupId, List<Long> users) {
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);

        if (membership == null || !membership.getIsAdmin()) {
            throw new UnauthorizedException("You are not allowed to add members to this group");
        } else {
            for (Long user : users) {
                Membership m = new Membership();
                m.setId(new MembershipKey(user, groupId));
                m.setGroup(groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found")));
                m.setUser(userRepository.findById(user).orElseThrow(() -> new MembershipNotFoundException("User not found")));
                m.setIsAdmin(false);
                m.setJoiningDate(new Date(System.currentTimeMillis()));
                membershipRepository.save(m);
            }
            return "Group members added successfully";
        }
    }


    @Transactional
    public String removeGroupMember(Long groupId, Long memberId) {
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);

        if (membership == null || !membership.getIsAdmin()) {
            throw new UnauthorizedException("You are not allowed to remove members from this group");
        } else {
            membershipRepository.deleteByGroupIdAndUserId(groupId, memberId);
            return "Group member removed successfully";
        }
    }


    public GroupDTO3 getGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found"));
        User creator = group.getCreator();
        UserDTO2 creatorDTO = new UserDTO2(creator.getId(), creator.getUsername(), creator.getEmail());
        List<UserDTO2> admins = new ArrayList<>();
        List<Membership> adminMemberships = membershipRepository.findByGroupIdAndIsAdmin(groupId, true);
        for (Membership admin : adminMemberships) {
            UserDTO2 adminDTO = new UserDTO2(admin.getUser().getId(), admin.getUser().getUsername(), admin.getUser().getEmail());
            admins.add(adminDTO);
        }
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        if (membership == null) {
            throw new UnauthorizedException("You are not a member of this group");
        }
        return new GroupDTO3(group.getId(), group.getImagrUrl(), group.getName(), group.getDescription(), group.getIsOfficial(), group.getCreatedDate(), creatorDTO, admins);
    }

    public void addAdmin(Long groupId, Long userId) {
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        Membership membership2 = membershipRepository.findByUserIdAndGroupId(userId, groupId);
        if (membership2 == null) {
            throw new UnauthorizedException("User is not a member of this group");
        } else if (membership == null || !membership.getIsAdmin()) {
            throw new UnauthorizedException("You are not allowed to add admins to this group");
        } else {
            Membership m = membershipRepository.findById(new MembershipKey(userId, groupId)).orElseThrow(() -> new MembershipNotFoundException("Membership not found"));
            m.setIsAdmin(true);
            membershipRepository.save(m);
        }
    }

    public void removeAdmin(Long groupId, Long userId) {
        User connectedUser = functions.getConnectedUser();
        Membership membership = membershipRepository.findByUserIdAndGroupId(connectedUser.getId(), groupId);
        Membership membership2 = membershipRepository.findByUserIdAndGroupId(userId, groupId);
        if (membership2 == null) {
            throw new UnauthorizedException("User is not a member of this group");
        } else if (membership == null || !membership.getIsAdmin()) {
            throw new UnauthorizedException("You are not allowed to remove admins from this group");
        } else {
            Membership m = membershipRepository.findById(new MembershipKey(userId, groupId)).orElseThrow(() -> new MembershipNotFoundException("Membership not found"));
            m.setIsAdmin(false);
            membershipRepository.save(m);
        }
    }
    
}
