package ma.insea.connect.drive.dto;

import lombok.Data;
import ma.insea.connect.user.DegreePath;

import java.time.LocalDateTime;

@Data
public class DriveItemDto {
    private Long id;
    private String name;
    private String description;
    private DriveUserDto creator;
    private Long size;
    private String mimeType;
    private boolean isFolder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DegreePath degreePath;
    private String itemUrl;
    private FolderDto parent;

}
