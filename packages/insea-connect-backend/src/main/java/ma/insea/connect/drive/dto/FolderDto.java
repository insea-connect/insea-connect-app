package ma.insea.connect.drive.dto;

import lombok.Data;

@Data
public class FolderDto {
    private String name;
    private String description;
    private DriveUserDto creator;
    private FolderDto parent;
}
