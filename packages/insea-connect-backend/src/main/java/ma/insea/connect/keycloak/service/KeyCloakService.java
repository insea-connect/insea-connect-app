package ma.insea.connect.keycloak.service;


import lombok.AllArgsConstructor;
import ma.insea.connect.keycloak.Credentials;
import ma.insea.connect.keycloak.DTO.UserDTO;
import ma.insea.connect.keycloak.KeycloakConfig;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// This class provides methods to interact with Keycloak users such as adding, updating, deleting,
// and performing various actions like sending verification links and resetting passwords.

@AllArgsConstructor
@Service

public class KeyCloakService {

    // Method to add a new user to Keycloak.
    public void addUser(UserDTO userDTO){
        // Create password credential from userDTO's password.
        CredentialRepresentation credential = Credentials.createPasswordCredentials(userDTO.getPassword());

        // Create a new user representation with userDTO's details.
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential)); // Set user's credentials.
        user.setEnabled(true); // Enable the user.

        // Get an instance of UsersResource and create the user.
        UsersResource instance = getInstance();
        instance.create(user);
    }

    // Method to get a user by username from Keycloak.
    public List<UserRepresentation> getUser(String userName){
        // Get an instance of UsersResource and search for user by username.
        UsersResource usersResource = getInstance();
        List<UserRepresentation> user = usersResource.search(userName, true);
        return user;
    }

    // Method to update a user's details in Keycloak.
    public void updateUser(String userId, UserDTO userDTO){
        // Create password credential from userDTO's password.
        CredentialRepresentation credential = Credentials.createPasswordCredentials(userDTO.getPassword());

        // Create a new user representation with userDTO's details.
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential)); // Set user's credentials.

        // Get an instance of UsersResource and update the user.
        UsersResource usersResource = getInstance();
        usersResource.get(userId).update(user);
    }

    // Method to delete a user from Keycloak.
    public void deleteUser(String userId){
        // Get an instance of UsersResource and remove the user.
        UsersResource usersResource = getInstance();
        usersResource.get(userId).remove();
    }

    // Method to send a verification email to a user in Keycloak.
    public void sendVerificationLink(String userId){
        // Get an instance of UsersResource and send verification email to the user.
        UsersResource usersResource = getInstance();
        usersResource.get(userId).sendVerifyEmail();
    }

    // Method to send a reset password email to a user in Keycloak.
    public void sendResetPassword(String userId){
        // Get an instance of UsersResource and execute action to send reset password email to the user.
        UsersResource usersResource = getInstance();
        usersResource.get(userId).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }

    // Method to get an instance of UsersResource from KeycloakConfig.
    public UsersResource getInstance(){
        return KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
    }
}
