package ma.insea.connect.chat.group;

import lombok.Data;
import lombok.NoArgsConstructor;
import ma.insea.connect.user.User;
import lombok.AllArgsConstructor;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imagrUrl;
    private String name;
    private String description;
    private Boolean isOfficial;
    private Date createdDate;

    @OneToMany(mappedBy = "group",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Membership> memberships;


    public void addMembership(Membership membership){
        this.memberships.add(membership);
    }

    @ManyToOne
    @JoinColumn(name = "creator")
    private User creator;

}
