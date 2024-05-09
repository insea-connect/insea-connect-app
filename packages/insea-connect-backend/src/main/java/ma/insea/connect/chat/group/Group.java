package ma.insea.connect.chat.group;

import lombok.Data;
import lombok.NoArgsConstructor;
import ma.insea.connect.user.User;
import lombok.AllArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_group",schema = "testo")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imagrUrl;
    private String name;
    private String description;
    private Boolean isOffecial;
    private Date createdDate;

    
    
    // @ManyToMany(cascade = CascadeType.ALL)
    // @JsonManagedReference
    // @JoinTable(name = "group_user",
    //            joinColumns = @JoinColumn(name = "group_id"),
    //            inverseJoinColumns = @JoinColumn(name = "user_id"))
    // private List<User> users=new ArrayList<>();
    @OneToMany(mappedBy = "group",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Membership> memberships;

    private Long creator;

    // public void addUser(User user) {
    //     users.add(user);
    // }
    // public void removeUser(User user) {
    //     users.remove(user);
    // }
}
