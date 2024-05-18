package ma.insea.connect.user;

import jakarta.persistence.*;
import lombok.*;
import ma.insea.connect.drive.model.DriveItem;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DegreePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cycle;
    private String major;
    private int pathYear;

    @OneToMany(mappedBy = "degreePath")
    private List<User> students;


    @OneToMany(mappedBy = "degreePath")
    @JsonIgnore
    private List<DriveItem> driveItems;
}