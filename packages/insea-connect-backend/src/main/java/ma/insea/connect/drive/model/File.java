package ma.insea.connect.drive.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class File extends DriveItem {

    private Long size;
    private String mimeType;
    private String fileUrl;
}