package ma.insea.connect.drive.repository;

import ma.insea.connect.user.model.DegreePath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DegreePathRepository extends JpaRepository<DegreePath, Long> {
}