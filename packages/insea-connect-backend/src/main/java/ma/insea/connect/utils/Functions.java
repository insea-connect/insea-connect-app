package ma.insea.connect.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ma.insea.connect.user.DegreePath;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;


@Component
@RequiredArgsConstructor
public class Functions {
    @Value("${UPLOAD_DIR}")
    private String UPLOAD_DIR;

    private final UserRepository userRepository;
    public User getConnectedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User connectedUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        return connectedUser;
    }

    public String uploadFile(MultipartFile file)throws IOException{
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }
        
            // Get the filename and sanitize it
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            fileName = "A"+System.currentTimeMillis()+fileName;

            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Copy the file to the upload directory
             Path filePath = uploadPath.resolve(fileName);
             Files.copy(file.getInputStream(), filePath);

            // Return the path of the saved file
            return "uploads/" + fileName;
    }

    public boolean checkPermission(User user, DegreePath degreePath) {
        if(user.getDegreePath().getMajor().equals(degreePath.getMajor())){
            return true;
        }
        return false;
    }
    
}
