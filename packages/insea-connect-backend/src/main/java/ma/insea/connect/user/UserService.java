package ma.insea.connect.user;


import lombok.AllArgsConstructor;
import ma.insea.connect.utils.Functions;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Functions functions;

    public User saveUser(User user) {
        user.setStatus(Status.ONLINE);
        userRepository.save(user);
        return user;
    }

    public void disconnect(User user) {
        var storedUser = userRepository.findByEmail(user.getEmail());
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            storedUser.setLastLogin(new java.util.Date(System.currentTimeMillis()));
            userRepository.save(storedUser);
        }
    }

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getStatus(), user.getLastLogin());
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    public OnlineDTO getUserStatus(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return new OnlineDTO(user.getStatus(), user.getLastLogin());
        }
        return null;
    }
    public void updateUserLastSeen() {
        User connectedUser = functions.getConnectedUser();
        connectedUser.setLastLogin(new java.util.Date(System.currentTimeMillis()));
        userRepository.save(connectedUser);
    }

    @Scheduled(fixedRate = 60000)
    public void checkUserStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            LocalDateTime lastLogin = LocalDateTime.ofInstant(user.getLastLogin().toInstant(), ZoneId.systemDefault());
            if (lastLogin.isBefore(lastLogin.now().minusMinutes(2))) { // Offline if no heartbeat for 2 minutes
                user.setStatus(Status.OFFLINE);
                userRepository.save(user);
            }
        }
    }
}
