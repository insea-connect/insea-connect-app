package ma.insea.connect.user;

import jakarta.persistence.*;
import lombok.*;
import ma.insea.connect.chat.common.chatMessage.ChatMessage;
import ma.insea.connect.chat.conversation.Conversation;
import ma.insea.connect.chat.group.Group;
import ma.insea.connect.chat.group.Membership;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(name="chat_user",schema = "testo")
@EntityListeners(AuditingEntityListener.class)
public class User 
// implements UserDetails
 {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String passwordHash;
    private String imagrUrl;
    private String firstname;
    private String lastname;
    private String dateOfBirth;
    private String bio;

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.STUDENT;

    @CreatedDate
    @Column(updatable = false,nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;
    
    private Status status;
    private Date lastLogin;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Membership> membership;
    

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "sender")
    @JsonIgnore
    private List<ChatMessage> sentMessages;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "recipient")
    private List<ChatMessage> receivedMessages;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "member1")
    private List<Conversation> member1conversations;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "member2")
    private List<Conversation> member2conversations;

    // public void addGroup(Group group){
    //     groups.add(group);
    // }

    



    // public void addGroup(Long group){
    //         List<Long> groups = this.getGroups();
    //         if (groups == null) 
    //         {
    //             groups = new ArrayList<Long>();
                
    //         }
    //         groups.add(group);

    //     }


    // public void removeGroup(Long groupId) {
    //     List<Long> groups = this.getGroups();
    //     if (groups == null) {
    //         groups = new ArrayList<Long>();
    //     }
    //     groups.remove(groupId);
    // }

    

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //    return List.of(new SimpleGrantedAuthority(role.name()));
    // }

   

    // @Override
    // public boolean isAccountNonExpired() {
    //     return true;
    // }

    // @Override
    // public boolean isAccountNonLocked() {
    //     return true;
    // }

    // @Override
    // public boolean isCredentialsNonExpired() {
    //     return true;
    // }

    // @Override
    // public boolean isEnabled() {
    //     return true;
    // }


    // @Override
    // public String getUsername() {
    //     return username;
    // }


    // @Override
    // public String getPassword() {
    //     return passwordHash;
    // }


  

     


}