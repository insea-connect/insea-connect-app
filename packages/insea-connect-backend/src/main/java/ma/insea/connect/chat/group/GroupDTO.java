package ma.insea.connect.chat.group;

import java.util.List;

import lombok.Data;
@Data
public class GroupDTO {
    private String name;
    private String description;
    private Long creator;
    private List<Long> members;
    
}
