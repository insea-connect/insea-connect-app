package ma.insea.connect.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;

@Component
@RequiredArgsConstructor
public class Functions {
    private static final String UPLOAD_DIR = "uploads";

    private final UserRepository userRepository;
    public User getConnectedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User connectedUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        return connectedUser;
    }

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }
        
        try {
            // Get the filename and sanitize it
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Copy the file to the upload directory
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // Return the path of the saved file
            return filePath.toString();
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }
    
}
