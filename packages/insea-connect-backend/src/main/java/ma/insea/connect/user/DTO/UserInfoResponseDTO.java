package ma.insea.connect.user.DTO;

import lombok.*;
import ma.insea.connect.user.DegreePath;
import ma.insea.connect.user.Role;
import ma.insea.connect.user.User;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserInfoResponseDTO {
    private Long id;
    private String email;
    private String username;
    private String imagrUrl;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private Role role = Role.STUDENT;
    private DegreePath degreePath;


    public static UserInfoResponseDTO mapToUserInfoDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserInfoResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .imagrUrl(user.getImageUrl())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth()) // Assuming this is stored as a String
                .role(user.getRole())  // Assuming there's a getRole method, and it's directly compatible
                .degreePath(user.getDegreePath())
                .build();
    }
}
