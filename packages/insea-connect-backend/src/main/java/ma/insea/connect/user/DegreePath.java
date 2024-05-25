package ma.insea.connect.user;

import jakarta.persistence.*;
import lombok.*;
import ma.insea.connect.drive.model.DriveItem;

import java.util.ArrayList;
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
    @JsonIgnore
    private List<User> students;


    @OneToMany(mappedBy = "degreePath")
    @JsonIgnore
    private List<DriveItem> driveItems;
    public DegreePath(String cycle, String major, int pathYear) {
        this.cycle = cycle;
        this.major = major;
        this.pathYear = pathYear;
    }

    public void addStudent(User student) {
        if(this.students == null){
            this.students = new ArrayList<>();
        }
        this.students.add(student);
    }
}