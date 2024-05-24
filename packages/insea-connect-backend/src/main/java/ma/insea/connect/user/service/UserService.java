package ma.insea.connect.user.service;


import lombok.AllArgsConstructor;

import ma.insea.connect.exceptions.user.UserNotFoundException;
import ma.insea.connect.exceptions.user.UserPersistenceException;
import ma.insea.connect.keycloak.DTO.AddKeycloakDTO;
import ma.insea.connect.keycloak.service.KeyCloakService;
import ma.insea.connect.user.DTO.AddUserDTO;
import ma.insea.connect.user.DTO.UserDTO;
import ma.insea.connect.user.model.Status;
import ma.insea.connect.user.model.User;
import ma.insea.connect.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ma.insea.connect.user.DTO.AddUserDTO.mapToUser;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KeyCloakService keyCloakService;

    public User saveUser(User user) {
        try {
            user.setStatus(Status.ONLINE);
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new UserPersistenceException("Failed to save user: " + user.getUsername(), e);
        }
    }

    public User disconnect(User user) {
        User storedUser = findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + user.getEmail()));
        storedUser.setStatus(Status.OFFLINE);
        storedUser.setLastLogin(new java.util.Date(System.currentTimeMillis()));
        try {
            userRepository.save(storedUser);
            return storedUser;
        } catch (Exception e) {
            throw new UserPersistenceException("Failed to update user status: " + storedUser.getUsername(), e);
        }
    }

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getStatus(), user.getLastLogin()))
                .collect(Collectors.toList());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User addUser(AddUserDTO addUserDTO) {
        User user = findByEmail(addUserDTO.getEmail())
                .orElseGet(() -> new User());

        user.setEmail(addUserDTO.getEmail());
        user.setUsername(addUserDTO.getUsername());
        user.setFirstName(addUserDTO.getFirstName());
        user.setLastName(addUserDTO.getLastName());
        user.setStatus(Status.ONLINE);
        saveUser(user);
        keyCloakService.addUser(AddKeycloakDTO.mapToAddKeycloakDTO(addUserDTO));
        return user;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public User getUserInfo(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}
