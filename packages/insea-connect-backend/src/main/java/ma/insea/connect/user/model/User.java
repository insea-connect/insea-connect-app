package ma.insea.connect.user.model;

import jakarta.persistence.*;
import lombok.*;
import ma.insea.connect.chat.common.chatMessage.model.ChatMessage;
import ma.insea.connect.chat.conversation.Conversation;
import ma.insea.connect.chat.group.model.Group;
import ma.insea.connect.chat.group.model.Membership;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(name="_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails
 {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @Column(unique = true)
    private String username;
    private String imageUrl;
    private String firstName;
    private String lastName;
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


    @ElementCollection
    private List<Long> groups = new ArrayList<>();
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

    @OneToMany(mappedBy = "creator")
    @JsonIgnore
    private List<Group> createdGroups;

    @ManyToOne
    private DegreePath degreePath;






    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
       return null;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public String getUsername() {
        return username;
    }







}