package ma.insea.connect.keycloak.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ma.insea.connect.user.User;


@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public static UserDTO mapToUserDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword()
        );
}}

