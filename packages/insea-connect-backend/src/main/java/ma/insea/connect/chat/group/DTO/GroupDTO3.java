package ma.insea.connect.chat.group.DTO;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import ma.insea.connect.user.DTO.UserDTO2;
import java.util.List;
@Data
@AllArgsConstructor
public class GroupDTO3 {
    private Long id;
    private String imagrUrl;
    private String name;
    private String description;
    private Boolean isOfficial;
    private Date createdDate;
    private UserDTO2 creator;
    private List<UserDTO2> admins;
}
