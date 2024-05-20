package ma.insea.connect.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.user.User;
import ma.insea.connect.user.UserRepository;

@Component
@RequiredArgsConstructor
public class Functions {
    private final UserRepository userRepository;
    public User getConnectedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User connectedUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        return connectedUser;
    }
    
}
