package ma.insea.connect.user;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OnlineDTO {
    private Status status;
    private Date lastLogin;

}
