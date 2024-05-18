package ma.insea.connect.keycloak.controller;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RefreshTokenDTO {
    private String refreshToken;
}
