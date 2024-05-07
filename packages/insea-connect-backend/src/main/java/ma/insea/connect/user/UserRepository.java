package ma.insea.connect.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    List<User> findAllByStatus(Status status);
    // User findByNickName(String nickName);
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
}
