package ma.insea.connect.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.insea.connect.user.model.DegreePath;
import ma.insea.connect.user.model.Role;
import ma.insea.connect.user.model.Status;
import ma.insea.connect.user.model.User;

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
        User user = new User();
        user.setEmail(addUserDTO.getEmail());
        user.setUsername(addUserDTO.getUsername());
        user.setImageUrl(addUserDTO.getImageUrl());
        user.setFirstName(addUserDTO.getFirstName());
        user.setLastName(addUserDTO.getLastName());
        user.setDateOfBirth(addUserDTO.getDateOfBirth());  // Assuming the date format is correctly handled elsewhere
        user.setBio(addUserDTO.getBio());
        user.setRole(addUserDTO.getRole() != null ? addUserDTO.getRole() : Role.STUDENT);
        user.setStatus(addUserDTO.getStatus());
        user.setLastLogin(addUserDTO.getLastLogin());
        user.setGroups(addUserDTO.getGroups() != null ? new ArrayList<>(addUserDTO.getGroups()) : new ArrayList<>());
        user.setDegreePath(addUserDTO.getDegreePath());

        // Note: UserDetails-related properties and relationships are not directly set here as they require session/context-specific data.
        return user;
    }


    public AddUserDTO toAddUserDTO() {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setEmail(this.getEmail());
        addUserDTO.setUsername(this.getUsername());
        addUserDTO.setImageUrl(this.getImageUrl());
        addUserDTO.setFirstName(this.getFirstName());
        addUserDTO.setLastName(this.getLastName());
        addUserDTO.setDateOfBirth(this.getDateOfBirth());  // Ensure format consistency
        addUserDTO.setBio(this.getBio());
        addUserDTO.setRole(this.getRole());
        addUserDTO.setStatus(this.getStatus());
        addUserDTO.setLastLogin(this.getLastLogin());
        addUserDTO.setGroups(new ArrayList<>(this.getGroups()));  // Copy to prevent outside modifications
        addUserDTO.setDegreePath(this.getDegreePath());
        // Optionally handle other fields and complex mappings if needed
        return addUserDTO;
    }
}
