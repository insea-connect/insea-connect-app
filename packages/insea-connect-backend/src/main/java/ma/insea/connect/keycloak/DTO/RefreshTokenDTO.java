package ma.insea.connect.keycloak.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RefreshTokenDTO {
    private String refreshToken;
}
