package ma.insea.connect.keycloak.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ma.insea.connect.user.DTO.AddUserDTO;


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

