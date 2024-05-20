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
					.email("anas@example.com")
					.firstName("anas")
					.lastName("anas")
					.role(Role.ADMIN)
					.password("admin")
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

            AddUserDTO user4 = AddUserDTO.builder()
					.username("mohammed")
					.email("mohammed@example.com")
					.firstName("mohammed")
					.lastName("Amimi")
					.role(Role.STUDENT)
					.password("admin")
					
					.build();
			System.out.println("here it is "+AddKeycloakDTO.mapToAddKeycloakDTO(user4).toString());
			userController.addUser1(user4);

            AddUserDTO user5 = AddUserDTO.builder()
					.username("ahmed")
					.email("ahmed@example.com")
					.firstName("ahmed")
					.lastName("Amimi")
					.role(Role.CLASS_REP)
					.password("admin")
					
					.build();
			System.out.println("here it is "+AddKeycloakDTO.mapToAddKeycloakDTO(user5).toString());
			userController.addUser1(user5);

            AddUserDTO user6 = AddUserDTO.builder()
					.username("saad")
					.email("saad@example.com")
					.firstName("saad")
					.lastName("Amimi")
					.role(Role.ADMIN)
					.password("admin")
					
					.build();
			System.out.println("here it is "+AddKeycloakDTO.mapToAddKeycloakDTO(user6).toString());
			userController.addUser1(user6);



            User anas =userRepository.findByUsername("anas").get();
            User hamza =userRepository.findByUsername("hamza").get();
            User soulayman =userRepository.findByUsername("soulayman").get();
            User mohammed =userRepository.findByUsername("mohammed").get();
            User ahmed =userRepository.findByUsername("ahmed").get();
            User saad =userRepository.findByUsername("saad").get();



            Group group = new Group();
            group.setName("1A dse");
            group.setCreator(anas);
            group.setDescription("group for 1A dse students");
            group.setCreatedDate(new java.util.Date(System.currentTimeMillis()));

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
            groupRepository.save(group);
            group.addMembership(m1);
            group.addMembership(m2);
            groupRepository.save(group);


            Group group2 = new Group();
            group2.setName("2A DSE");
            group2.setCreator(ahmed);
            group2.setDescription("group for 2A dse students");
            group2.setCreatedDate(new java.util.Date(System.currentTimeMillis()));
            Membership m11 = new Membership();
            m11.setId(new MembershipKey(ahmed.getId(),group2.getId()));
            m11.setGroup(group2);
            m11.setUser(ahmed);
            m11.setIsAdmin(true);
            m11.setJoiningDate(new java.sql.Date(System.currentTimeMillis()));
            Membership m33 = new Membership();
            m33.setId(new MembershipKey(anas.getId(),group2.getId()));
            m33.setGroup(group2);
            m33.setUser(anas);
            m33.setIsAdmin(false);
            m33.setJoiningDate(new java.sql.Date(System.currentTimeMillis()));
            Membership m22 = new Membership();
            m22.setId(new MembershipKey(mohammed.getId(),group2.getId()));
            m22.setGroup(group2);
            m22.setUser(mohammed);
            m22.setIsAdmin(false);
            m22.setJoiningDate(new java.sql.Date(System.currentTimeMillis()));
            groupRepository.save(group2);
            group2.addMembership(m11);
            group2.addMembership(m22);
            group2.addMembership(m33);
            groupRepository.save(group2);

            GroupMessage groupMessage0=new GroupMessage();
            groupMessage0.setGroupId(group2.getId());
            groupMessage0.setContent("this is anas from the group2");
            groupMessage0.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            groupMessage0.setSender(anas);
            groupMessageRepository.save(groupMessage0);


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

            GroupMessage groupMessage3=new GroupMessage();
            groupMessage3.setGroupId(group.getId());
            groupMessage3.setContent("this is a nce group");
            groupMessage3.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            groupMessage3.setSender(anas);
            groupMessageRepository.save(groupMessage3);


            var chatId = getChatRoomId(Long.toString(anas.getId()),Long.toString(soulayman.getId()), true);

            Conversation conversation = new Conversation();
            conversation.setChatId(chatId);
            conversation.setMember1(anas);
            conversation.setMember2(soulayman);
            conversationRepository.save(conversation);


            ChatMessage chatMessage1 = new ChatMessage();
            chatMessage1.setChatId(chatId);
            chatMessage1.setSender(anas);
            chatMessage1.setRecipient(soulayman);
            chatMessage1.setContent("hello soulayman , my name is anas");
            chatMessage1.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            chatMessageRepository.save(chatMessage1);
        
            ChatMessage chatMessage2 = new ChatMessage();
            var chatId2 = getChatRoomId(Long.toString(anas.getId()),Long.toString(soulayman.getId()), true);
            chatMessage2.setChatId(chatId2);
            chatMessage2.setSender(soulayman);
            chatMessage2.setRecipient(anas);
            chatMessage2.setContent("nice to meet you anas , my name is soulayman");
            chatMessage2.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            chatMessageRepository.save(chatMessage2);

            ChatMessage chatMessage3 = new ChatMessage();
            chatMessage3.setChatId(chatId);
            chatMessage3.setSender(anas);
            chatMessage3.setRecipient(soulayman);
            chatMessage3.setContent("nice to meet you too soulayman");
            chatMessage3.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            chatMessageRepository.save(chatMessage3);


            var chatId3 = getChatRoomId(Long.toString(anas.getId()),Long.toString(ahmed.getId()), true);

            Conversation conversation2 = new Conversation();
            conversation2.setChatId(chatId3);
            conversation2.setMember1(anas);
            conversation2.setMember2(ahmed);
            conversationRepository.save(conversation2);


            ChatMessage chatMessage11 = new ChatMessage();
            chatMessage11.setChatId(chatId3);
            chatMessage11.setSender(anas);
            chatMessage11.setRecipient(ahmed);
            chatMessage11.setContent("hello ahmed , my name is anas");
            chatMessage11.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            chatMessageRepository.save(chatMessage11);
        
            ChatMessage chatMessage22 = new ChatMessage();
            chatMessage22.setChatId(chatId3);
            chatMessage22.setSender(ahmed);
            chatMessage22.setRecipient(anas);
            chatMessage22.setContent("nice to meet you anas , my name is ahmed");
            chatMessage22.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            chatMessageRepository.save(chatMessage22);

            ChatMessage chatMessage33 = new ChatMessage();
            chatMessage33.setChatId(chatId3);
            chatMessage33.setSender(anas);
            chatMessage33.setRecipient(ahmed);
            chatMessage33.setContent("nice to meet you too ahmed");
            chatMessage33.setTimestamp(new java.sql.Date(System.currentTimeMillis()));
            chatMessageRepository.save(chatMessage33);
        
    }
    public String getChatRoomId(String senderId,String recipientId,boolean createNewRoomIfNotExists)
         {
            var first = senderId.compareTo(recipientId) < 0 ? senderId : recipientId;
            var second = senderId.compareTo(recipientId) < 0 ? recipientId : senderId;
            var chatId = String.format("%s_%s",first, second);
            return chatId;
}

    

	
}