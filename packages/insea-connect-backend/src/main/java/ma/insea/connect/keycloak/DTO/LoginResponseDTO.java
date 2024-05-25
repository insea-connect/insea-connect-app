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
    private String access_token;
    private String refresh_token;
    private int access_token_duration;
    private int refresh_token_duration;
    private String access_token_expiration;
    private String refresh_token_expiration;
    private String thread_id;





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
            this.access_token = jsonObj.getString("access_token");
            this.access_token_duration = jsonObj.getInt("expires_in");
            this.refresh_token = jsonObj.getString("refresh_token");
            this.refresh_token_duration = jsonObj.getInt("refresh_expires_in");

            // Get the current time and calculate expiration times
            Instant now = Instant.now();
            OffsetDateTime accessExpiresAt = now.plusSeconds(access_token_duration).atOffset(ZoneOffset.UTC);
            OffsetDateTime refreshExpiresAt = now.plusSeconds(refresh_token_duration).atOffset(ZoneOffset.UTC);

            // Format the expiration dates using ISO 8601 format
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            this.access_token_expiration = formatter.format(accessExpiresAt);
            this.refresh_token_expiration = formatter.format(refreshExpiresAt);
        } catch (Exception e) {
            // Log the error
            log.warn("Error extracting token info: " + e.getMessage());
            // Optionally rethrow or handle the exception
        }
    }}

