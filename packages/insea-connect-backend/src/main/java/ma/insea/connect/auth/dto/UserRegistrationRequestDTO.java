package ma.insea.connect.auth.dto;

public record UserRegistrationRequestDTO(
        String username,
        String firstName,
        String lastName,
        String password
) {
}
