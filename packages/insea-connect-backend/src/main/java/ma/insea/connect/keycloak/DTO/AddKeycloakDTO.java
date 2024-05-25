package ma.insea.connect.keycloak.DTO;

import lombok.*;
import ma.insea.connect.user.DTO.AddUserDTO;
import ma.insea.connect.user.Role;
import ma.insea.connect.user.User;


@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddKeycloakDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public static AddKeycloakDTO mapToAddKeycloakDTO(AddUserDTO addUserDTO) {
        return new AddKeycloakDTO(
                addUserDTO.getUsername(),
                addUserDTO.getEmail(),
                addUserDTO.getPassword(),
                addUserDTO.getFirstName(),
                addUserDTO.getLastName()


        );
}



}

