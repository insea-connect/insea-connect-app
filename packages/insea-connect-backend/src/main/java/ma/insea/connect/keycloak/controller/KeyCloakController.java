package ma.insea.connect.keycloak.controller;


import ma.insea.connect.keycloak.DTO.UserDTO;
import ma.insea.connect.keycloak.service.KeyCloakService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Annotates this class as a REST controller to handle incoming HTTP requests.
// Maps the base path of this controller to "api/user".
@RestController
@RequestMapping(path = "/api/v1/keyCloakUser")
public class KeyCloakController {

    // Injects the KeyCloakService bean to manage Keycloak-related operations.
    @Autowired
    KeyCloakService service;

    // Handles POST requests to "/addUser" to add a new user.
    @PostMapping(path = "/addUser")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) {
        try {
            // Calls the service method to add a new user.
            service.addUser(userDTO);
            // Returns a 201 Created status on success.
            return ResponseEntity.status(HttpStatus.CREATED).body("User Added Successfully.");
        } catch (Exception e) {
            // Returns a 400 Bad Request status if an exception is caught.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to add user", e);
        }
    }

    // Handles GET requests to "/{userName}" to fetch user details by username.
    @GetMapping(path = "/{userName}")
    public ResponseEntity<List<UserRepresentation>> getUser(@PathVariable("userName") String userName) {
        try {
            // Calls the service method to fetch user details based on username.
            List<UserRepresentation> user = service.getUser(userName);
            if (user.isEmpty()) {
                // Returns a 404 Not Found status if the user is not found.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Returns a 200 OK status along with the user data.
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // Returns a 500 Internal Server Error status if an error occurs.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user", e);
        }
    }

    // Handles PUT requests to "/update/{userId}" to update user details by user ID.
    @PutMapping(path = "/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") String userId, @RequestBody UserDTO userDTO) {
        try {
            // Calls the service method to update the user details.
            service.updateUser(userId, userDTO);
            // Returns a 200 OK status on successful update.
            return ResponseEntity.ok("User Details Updated Successfully.");
        } catch (Exception e) {
            // Returns a 400 Bad Request status if an error occurs during update.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update user", e);
        }
    }

    // Handles DELETE requests to "/delete/{userId}" to delete a user by user ID.
    @DeleteMapping(path = "/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
        try {
            // Calls the service method to delete the user.
            service.deleteUser(userId);
            // Returns a 200 OK status on successful deletion.
            return ResponseEntity.ok("User Deleted Successfully.");
        } catch (Exception e) {
            // Returns a 500 Internal Server Error status if an error occurs during deletion.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting user", e);
        }
    }

    // Handles GET requests to "/verification-link/{userId}" to send a verification link by user ID.
    @GetMapping(path = "/verification-link/{userId}")
    public ResponseEntity<String> sendVerificationLink(@PathVariable("userId") String userId) {
        try {
            // Calls the service method to send a verification link.
            service.sendVerificationLink(userId);
            // Returns a 200 OK status on successful sending.
            return ResponseEntity.ok("Verification Link Sent to Registered E-mail.");
        } catch (Exception e) {
            // Returns a 500 Internal Server Error status if an error occurs during sending.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending verification link", e);
        }
    }

    // Handles GET requests to "/reset-password/{userId}" to send a reset password link by user ID.
    @GetMapping(path = "/reset-password/{userId}")
    public ResponseEntity<String> sendResetPassword(@PathVariable("userId") String userId) {
        try {
            // Calls the service method to send a reset password link.
            service.sendResetPassword(userId);
            // Returns a 200 OK status on successful sending.
            return ResponseEntity.ok("Reset Password Link Sent Successfully.");
        } catch (Exception e) {
            // Returns a 500 Internal Server Error status if an error occurs during sending.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending reset password link", e);
        }
    }
}
