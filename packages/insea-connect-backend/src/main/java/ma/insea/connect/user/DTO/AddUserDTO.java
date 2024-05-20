package ma.insea.connect.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.insea.connect.user.DegreePath;
import ma.insea.connect.user.Role;
import ma.insea.connect.user.Status;
import ma.insea.connect.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO {
    private String email;
    private String username;
    private String imageUrl;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String bio;
    private Role role;
    private Status status;
    private Date lastLogin;
    private List<Long> groups;
    private String password;
    private DegreePath degreePath;

    public static User mapToUser(AddUserDTO addUserDTO) {
        return User.builder()
                .email(addUserDTO.getEmail())
                .username(addUserDTO.getUsername())
                .imageUrl(addUserDTO.getImageUrl())
                .firstName(addUserDTO.getFirstName())
                .lastName(addUserDTO.getLastName())
                .dateOfBirth(addUserDTO.getDateOfBirth())
                .bio(addUserDTO.getBio())
                .role(addUserDTO.getRole() != null ? addUserDTO.getRole() : Role.STUDENT)
                .degreePath(addUserDTO.getDegreePath() != null ? addUserDTO.getDegreePath() : null)
                .status(addUserDTO.getStatus())
                .lastLogin(addUserDTO.getLastLogin())
                .groups(addUserDTO.getGroups() != null ? addUserDTO.getGroups() : new ArrayList<>())
                .build();
    }
}
