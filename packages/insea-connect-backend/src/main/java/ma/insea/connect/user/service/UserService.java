package ma.insea.connect.user.service;


import lombok.AllArgsConstructor;

import ma.insea.connect.user.DTO.UserDTO;
import ma.insea.connect.user.model.Status;
import ma.insea.connect.user.model.User;
import ma.insea.connect.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

    public User findByUsername(String username){
        User user = userRepository.findByUsername(username).orElse(null);
        return user;

    }
}
