package ma.insea.connect.keycloak.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ma.insea.connect.user.DTO.AddUserDTO;


@AllArgsConstructor
@Getter
@Setter
public class AddKeycloakDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public static AddKeycloakDTO mapToUserDTO(AddUserDTO addUserDTO) {
        return new AddKeycloakDTO(
                addUserDTO.getUsername(),
                addUserDTO.getFirstName(),
                addUserDTO.getLastName(),
                addUserDTO.getEmail(),
                addUserDTO.getPassword()
        );
}


}

