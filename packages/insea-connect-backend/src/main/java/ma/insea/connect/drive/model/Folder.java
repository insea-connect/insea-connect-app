package ma.insea.connect.drive.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Folder extends DriveItem {

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<DriveItem> children = new ArrayList<>();
}