// package ma.insea.connect.auth.service;

// import lombok.RequiredArgsConstructor;
// import ma.insea.connect.user.UserRepository;

// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;


// @Service
// @RequiredArgsConstructor
// public class UserDetailsServiceImpl implements UserDetailsService {

//     private final UserRepository repository;
//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//         return repository.findByUsername(username)
//                 .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//     }
// }
