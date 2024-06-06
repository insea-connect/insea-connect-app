package ma.insea.connect.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
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
import ma.insea.connect.drive.dto.DriveUserDto;
import ma.insea.connect.drive.dto.FolderDto;
import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.File;
import ma.insea.connect.drive.model.Folder;
import ma.insea.connect.drive.repository.DegreePathRepository;
import ma.insea.connect.drive.repository.FileRepository;
import ma.insea.connect.drive.repository.FolderRepository;
import ma.insea.connect.drive.service.DriveItemService;
import ma.insea.connect.drive.service.DriveItemServiceImpl;
import ma.insea.connect.keycloak.DTO.AddKeycloakDTO;
import ma.insea.connect.user.DegreePath;
import ma.insea.connect.user.Role;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserController;
import ma.insea.connect.user.UserRepository;
import ma.insea.connect.user.DTO.AddUserDTO;

@Component
@Profile("dev")
@AllArgsConstructor
public class DummyUserLoader implements CommandLineRunner {

    private final UserController userController;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;
    private final DegreePathRepository degreePathRepository;
    private final DriveItemServiceImpl driveItemService;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;

    @Override
    public void run(String... args) throws Exception {
        loadDummyUsers(userRepository,groupRepository,membershipRepository, groupMessageRepository,chatMessageRepository,conversationRepository,degreePathRepository,driveItemService,folderRepository,fileRepository);
    }

    private void loadDummyUsers(UserRepository userRepository,GroupRepository groupRepository, MembershipRepository membershipRepository,GroupMessageRepository groupMessageRepository , ChatMessageRepository chatMessageRepository,ConversationRepository conversationRepository,DegreePathRepository degreePathRepository,DriveItemServiceImpl driveItemService,FolderRepository folderRepository,FileRepository fileRepository) {
        AddUserDTO bot = AddUserDTO.builder()
                .username("bot")
                .email("bot@example.com")
                .firstName("bot")
                .lastName("bot")
                .role(Role.ADMIN)
                .password("admin")

                .build();
        userController.addUser1(bot);

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
            User the_bot =userRepository.findByUsername("bot").get();
            
            
            


            List<User> users = List.of(anas,hamza,soulayman,mohammed,saad);
            //initialize conversation with the bot
            for(User u:users){
                var chatId = getChatRoomId(Long.toString(u.getId()),Long.toString(the_bot.getId()), true);
                Conversation conversation = new Conversation();
                conversation.setChatId(chatId);
                conversation.setMember1(u);
                conversation.setMember2(the_bot);
                conversationRepository.save(conversation);
            }

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


            Membership mBot = new Membership();
            mBot.setId(new MembershipKey(the_bot.getId(),group.getId()));
            mBot.setUser(the_bot);
            mBot.setGroup(group);
            mBot.setJoiningDate(new java.sql.Date(System.currentTimeMillis()));
            mBot.setIsAdmin(true);
            group.addMembership(mBot);


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


            Membership mBot2 = new Membership();
            mBot2.setId(new MembershipKey(the_bot.getId(),group2.getId()));
            mBot2.setUser(the_bot);
            mBot2.setGroup(group2);
            mBot2.setJoiningDate(new java.sql.Date(System.currentTimeMillis()));
            mBot2.setIsAdmin(true);
            group2.addMembership(mBot2);


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
            groupMessage3.setTimestamp(new java.sql.Date(System.currentTimeMillis()-1000000));
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
            chatMessage33.setTimestamp(new java.sql.Date(System.currentTimeMillis()-1000000));
            chatMessageRepository.save(chatMessage33);

            var chatId4 = getChatRoomId(Long.toString(anas.getId()),Long.toString(saad.getId()), true);
                Conversation conversation4 = new Conversation();
                conversation4.setChatId(chatId4);
                conversation4.setMember1(anas);
                conversation4.setMember2(saad);
                conversationRepository.save(conversation4);

            //drives

            //initialize DegreePaths
            List<DegreePath> degreePaths = new ArrayList<>();
            for(int i=1;i<=3;i++){
                DegreePath degreePath1 = new DegreePath();
                degreePath1.setCycle("ing");
                degreePath1.setMajor("DSE");
                degreePath1.setPathYear(i);
                degreePaths.add(degreePath1);
                DegreePath degreePath2 = new DegreePath();
                degreePath2.setCycle("ing");
                degreePath2.setMajor("DS");
                degreePath2.setPathYear(i);
                degreePaths.add(degreePath2);
                DegreePath degreePath3 = new DegreePath();
                degreePath3.setCycle("ing");
                degreePath3.setMajor("RO");
                degreePath3.setPathYear(i);
                degreePaths.add(degreePath3);
                DegreePath degreePath4 = new DegreePath();
                degreePath4.setCycle("ing");
                degreePath4.setMajor("AF");
                degreePath4.setPathYear(i);
                degreePaths.add(degreePath4);
                DegreePath degreePath5 = new DegreePath();
                degreePath5.setCycle("ing");
                degreePath5.setMajor("SE");
                degreePath5.setPathYear(i);
                degreePaths.add(degreePath5);
                DegreePath degreePath6 = new DegreePath();
                degreePath6.setCycle("ing");
                degreePath6.setMajor("SD");
                degreePath6.setPathYear(i);
                degreePaths.add(degreePath6);
            }
            degreePathRepository.saveAll(degreePaths);
            DegreePath degreePath1 = degreePathRepository.findByCycleAndMajorAndPathYear("ing","DSE",2).get();
            anas.setDegreePath(degreePath1);
            soulayman.setDegreePath(degreePath1);
            hamza.setDegreePath(degreePath1);
            userRepository.save(hamza);
            userRepository.save(soulayman);
            userRepository.save(anas);
            
        //add folder S3 to 2nd year DSE students
        DegreePath degreePath = degreePathRepository.findByCycleAndMajorAndPathYear("ing","DSE",2).get();
        Folder folder = new Folder();
        folder.setName("Genie Logiciel");
        folder.setCreatedAt(LocalDateTime.now());
        folder.setDegreePath(degreePath);
        folder.setDescription("genie logiciel cours et td");
        folder.setParent(null);
        folder.setCreator(anas);
        folderRepository.save(folder);



        Folder folder3 = new Folder();
        folder3.setName("Frameworks");
        folder3.setCreatedAt(LocalDateTime.now());
        folder3.setDegreePath(degreePath);
        folder3.setDescription("frameworks de developpement web cours et td");
        folder3.setParent(null);
        folder3.setCreator(hamza);
        folderRepository.save(folder3);

        //add files to Framework folder

        File fileObj = new File();
        fileObj.setFileUrl("/uploads/Chapitre1.pdf");
        fileObj.setName("cours complet");
        fileObj.setSize((long)134000);
        fileObj.setMimeType("application/pdf");
        fileObj.setCreatedAt(LocalDateTime.now());
        fileObj.setFileUrl("uploads/cours_complet.pdf");
        fileObj.setParent(folder3);
        fileObj.setCreator(hamza);
        fileRepository.save(fileObj);




    }


    public String getChatRoomId(String senderId,String recipientId,boolean createNewRoomIfNotExists)
         {
            var first = senderId.compareTo(recipientId) < 0 ? senderId : recipientId;
            var second = senderId.compareTo(recipientId) < 0 ? recipientId : senderId;
            var chatId = String.format("%s_%s",first, second);
            return chatId;
}

    

	
}