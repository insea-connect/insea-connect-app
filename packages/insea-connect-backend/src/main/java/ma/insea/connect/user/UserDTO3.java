package ma.insea.connect.user;

import lombok.*;
@Data
@AllArgsConstructor
public class UserDTO3 {
    private Long id;
    private String username;
    private String email;
    private Boolean isAdmin;
    private Boolean isCreator;
}
