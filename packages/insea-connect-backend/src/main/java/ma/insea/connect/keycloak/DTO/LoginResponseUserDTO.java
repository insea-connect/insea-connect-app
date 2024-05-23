package ma.insea.connect.keycloak.DTO;

import lombok.*;
import ma.insea.connect.user.model.Role;
import ma.insea.connect.user.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public  class LoginResponseUserDTO {
    private Long id;
    private String username;
    private String email;

    private Role roles;

    public static LoginResponseUserDTO mapToLoginUserResponseDTO(User user){
        return new LoginResponseUserDTO(user.getId(),user.getUsername(), user.getEmail(),user.getRole());

    }

}