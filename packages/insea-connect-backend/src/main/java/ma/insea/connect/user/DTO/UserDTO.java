package ma.insea.connect.user.DTO;
import java.util.Date;

import lombok.*;
import ma.insea.connect.user.model.Status;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Status status;
    private Date lastLogin;
}
