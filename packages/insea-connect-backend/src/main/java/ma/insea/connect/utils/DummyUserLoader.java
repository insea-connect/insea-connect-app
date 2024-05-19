package ma.insea.connect.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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


    @Override
    public void run(String... args) throws Exception {
        loadDummyUsers(userRepository,groupRepository,membershipRepository);
    }

    private void loadDummyUsers(UserRepository userRepository,GroupRepository groupRepository, MembershipRepository membershipRepository) {
        AddUserDTO user = AddUserDTO.builder()
					.username("anas")
					.email("anas")
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
        
    }

    

	
}

