package ma.insea.connect.user.repository;

import ma.insea.connect.user.model.Status;
import ma.insea.connect.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    List<User> findAllByStatus(Status status);
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
}
