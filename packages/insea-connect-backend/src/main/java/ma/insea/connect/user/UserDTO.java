package ma.insea.connect.user;
import java.util.Date;

import lombok.*;
@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Status status;
    private Date lastLogin;
}
