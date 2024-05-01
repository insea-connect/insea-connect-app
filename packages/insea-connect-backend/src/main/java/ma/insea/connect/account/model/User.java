package ma.insea.connect.account.model;

import jakarta.persistence.*;
import lombok.*;
import ma.insea.connect.chat.common.model.Message;
import ma.insea.connect.chat.conversation.model.Conversation;
import ma.insea.connect.chat.conversation.model.DirectMessage;
import ma.insea.connect.chat.group.model.Group;
import ma.insea.connect.chat.group.model.GroupMembership;
import ma.insea.connect.chat.group.model.GroupMessage;
import ma.insea.connect.drive.model.DriveItem;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(name="_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
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

    @ManyToOne
    private DegreePath degreePath;

    @OneToMany(mappedBy = "creator")
    private List<DriveItem> driveItems;


    @OneToMany(mappedBy = "memberOne")
    private List<Conversation> conversationsAsMemberOne;

    @OneToMany(mappedBy = "memberTwo")
    private List<Conversation> conversationsAsMemberTwo;


    @OneToMany(mappedBy = "user")
    private List<GroupMembership> memberships;


    @OneToMany(mappedBy = "sender")
    private List<DirectMessage> directMessagesSent;

    @OneToMany(mappedBy = "receiver")
    private List<DirectMessage> directMessagesReceived;

    @OneToMany(mappedBy = "sender")
    private List<GroupMessage> groupMessages;

    public List<Conversation> getConversations(){
        return Stream.concat(conversationsAsMemberOne.stream(),conversationsAsMemberTwo.stream())
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
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


}
