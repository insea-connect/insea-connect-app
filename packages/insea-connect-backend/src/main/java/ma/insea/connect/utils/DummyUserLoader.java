package ma.insea.connect.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ma.insea.connect.chat.common.chatMessage.ChatMessage;
import ma.insea.connect.chat.common.chatMessage.ChatMessageRepository;
import ma.insea.connect.chat.common.chatMessage.GroupMessage;
import ma.insea.connect.chat.common.chatMessage.GroupMessageRepository;
import ma.insea.connect.chat.conversation.Conversation;
import ma.insea.connect.chat.conversation.ConversationRepository;
import ma.insea.connect.chat.group.Group;
import ma.insea.connect.chat.group.GroupRepository;
import ma.insea.connect.chat.group.Membership;
import ma.insea.connect.chat.group.MembershipKey;
import ma.insea.connect.chat.group.MembershipRepository;
import ma.insea.connect.keycloak.DTO.AddKeycloakDTO;
import ma.insea.connect.user.DegreePath;
import ma.insea.connect.user.Role;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserController;
import ma.insea.connect.user.UserRepository;
import ma.insea.connect.user.UserService;
import ma.insea.connect.user.DTO.AddUserDTO;

@Component
@Profile("dev")
@AllArgsConstructor
public class DummyUserLoader implements CommandLineRunner {

    private final UserController userController;
    private final UserService userService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;


    @Override
    public void run(String... args) throws Exception {
        loadDummyUsers(userRepository,groupRepository,membershipRepository, groupMessageRepository,chatMessageRepository,conversationRepository);
    }

    private void loadDummyUsers(UserRepository userRepository,GroupRepository groupRepository, MembershipRepository membershipRepository,GroupMessageRepository groupMessageRepository , ChatMessageRepository chatMessageRepository,ConversationRepository conversationRepository) {
        AddUserDTO user = AddUserDTO.builder()
					.username("anas")
					.email("anas@gmail.com")
					.firstName("anas")
					.lastName("anas")
					.role(Role.ADMIN)
					.password("anas")
					.build();
			System.out.println("here it is "+AddKeycloakDTO.mapToAddKeycloakDTO(user).toString());
			userController.addUser1(user);



			AddUserDTO user2 = AddUserDTO.builder()
					.username("soulayman")
					.email("soulayman@example.com")
					.firstName("soulayman")
					.lastName("Barday")
					.role(Role.CLASS_REP)
					.password("admin")
					
					.build();
			System.out.println("here it is "+AddKeycloakDTO.mapToAddKeycloakDTO(user2).toString());
			userController.addUser1(user2);


			AddUserDTO user3 = AddUserDTO.builder()
					.username("hamza")
					.email("hamza@example.com")
					.firstName("hamza")
					.lastName("Amimi")
					.role(Role.STUDENT)
					.password("admin")
					
					.build();
			System.out.println("here it is "+AddKeycloakDTO.mapToAddKeycloakDTO(user3).toString());
			userController.addUser1(user3);


            User anas =userRepository.findByUsername("anas").get();
            User hamza =userRepository.findByUsername("hamza").get();
            User soulayman =userRepository.findByUsername("soulayman").get();
            Group group = new Group();
            group.setName("group1");
            group.setCreator(anas);
            group.setDescription("group1");
            Membership m1 = new Membership();
            m1.setId(new MembershipKey(anas.getId(),group.getId()));
            m1.setGroup(group);
            m1.setUser(anas);
            m1.setIsAdmin(true);
            m1.setJoiningDate(new java.sql.Date(System.currentTimeMillis()));
            Membership m2 = new Membership();
            m2.setId(new MembershipKey(soulayman.getId(),group.getId()));
            m2.setGroup(group);
            m2.setUser(soulayman);
            m2.setIsAdmin(false);
            m2.setJoiningDate(new java.sql.Date(System.currentTimeMillis()));
            List<Membership> memberships = new ArrayList<Membership>();
            memberships.add(m1);
            memberships.add(m2);
            groupRepository.save(group);
            group.addMembership(m1);
            group.addMembership(m2);
            // group.setMemberships(memberships);
            // membershipRepository.saveAll(memberships);
            groupRepository.save(group);

            GroupMessage groupMessage1=new GroupMessage();
            groupMessage1.setGroupId(group.getId());
            groupMessage1.setContent("this is anas from the group");
            groupMessage1.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            groupMessage1.setSender(anas);
            groupMessageRepository.save(groupMessage1);

            GroupMessage groupMessage2=new GroupMessage();
            groupMessage2.setGroupId(group.getId());
            groupMessage2.setContent("welcome anas , this is soulayman");
            groupMessage2.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            groupMessage2.setSender(soulayman);
            groupMessageRepository.save(groupMessage2);




        ChatMessage chatMessage1 = new ChatMessage();
        var chatId = getChatRoomId(Long.toString(anas.getId()),Long.toString(soulayman.getId()), true);
        chatMessage1.setChatId(chatId);
        chatMessage1.setSender(anas);
        chatMessage1.setRecipient(soulayman);
        chatMessage1.setContent("hello soulayman , my name is anas");
        chatMessage1.setTimestamp(new Date());
        chatMessageRepository.save(chatMessage1);

        Conversation conversation = new Conversation();
        conversation.setChatId(chatId);
        conversation.setMember1(anas);
        conversation.setMember2(soulayman);
        conversationRepository.save(conversation);   
        
        ChatMessage chatMessage2 = new ChatMessage();
        var chatId2 = getChatRoomId(Long.toString(anas.getId()),Long.toString(soulayman.getId()), true);
        chatMessage2.setChatId(chatId2);
        chatMessage2.setSender(soulayman);
        chatMessage2.setRecipient(anas);
        chatMessage2.setContent("nice to meet you anas , my name is soulayman");
        chatMessage2.setTimestamp(new Date());
        chatMessageRepository.save(chatMessage2);
        
    }
    public String getChatRoomId(String senderId,String recipientId,boolean createNewRoomIfNotExists)
         {
            var first = senderId.compareTo(recipientId) < 0 ? senderId : recipientId;
            var second = senderId.compareTo(recipientId) < 0 ? recipientId : senderId;
            var chatId = String.format("%s_%s",first, second);
            return chatId;
}

    

	
}