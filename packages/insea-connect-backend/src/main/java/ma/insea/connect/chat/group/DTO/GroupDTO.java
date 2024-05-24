package ma.insea.connect.chat.group.DTO;

import java.util.List;

import lombok.Data;
@Data
public class GroupDTO {
    private Long id;
    private String name;
    private String description;
    private List<Long> members;
    
}
