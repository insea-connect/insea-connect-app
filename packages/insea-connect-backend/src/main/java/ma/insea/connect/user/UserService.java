package ma.insea.connect.user;


import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.sql.Date;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        userRepository.save(user);
    }

    public void disconnect(User user) {
        var storedUser = userRepository.findByEmail(user.getEmail());
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            storedUser.setLastLogin(new java.util.Date());
            userRepository.save(storedUser);
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
