package ma.insea.connect.keycloak.DTO;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.time.*;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class LoginResponseDTO {
    private LoginResponseUserDTO user;
    private String accessToken;
    private String refreshToken;
    private int accessTokenDuration;
    private int refreshTokenDuration;
    private String accessTokenExpiration;
    private String refreshTokenExpiration;
    private String threadId;





    public void extractTokenInfo(String rawJson) {
        try {
            // Step-by-step transformation to ensure proper JSON format
            String json = rawJson
                    .replace("=", ":")
                    .replaceAll("([\\w-]+):", "\"$1\":")
                    .replaceAll(":([^,{}]+)", ":\"$1\"")
                    .replaceAll(",\\s*(\\w)", ", \"$1")
                    .replace("{ ", "{\"")
                    .replace(" }", "\"}");

            // Fix for any embedded quotes or escape sequences
            json = json.replace("\"\"", "\"");

            // Log the transformed JSON
            log.warn("Transformed JSON: " + json);

            // Create a JSONObject from the string
            JSONObject jsonObj = new JSONObject(json);

            // Extract tokens and their expiry times in seconds
            this.accessToken = jsonObj.getString("access_token");
            this.accessTokenDuration = jsonObj.getInt("expires_in");
            this.refreshToken = jsonObj.getString("refresh_token");
            this.refreshTokenDuration = jsonObj.getInt("refresh_expires_in");

            // Get the current time and calculate expiration times
            Instant now = Instant.now();
            OffsetDateTime accessExpiresAt = now.plusSeconds(accessTokenDuration).atOffset(ZoneOffset.UTC);
            OffsetDateTime refreshExpiresAt = now.plusSeconds(refreshTokenDuration).atOffset(ZoneOffset.UTC);

            // Format the expiration dates using ISO 8601 format
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            this.accessTokenExpiration = formatter.format(accessExpiresAt);
            this.refreshTokenExpiration = formatter.format(refreshExpiresAt);
        } catch (Exception e) {
            // Log the error
            log.warn("Error extracting token info: " + e.getMessage());
            // Optionally rethrow or handle the exception
        }
    }}

