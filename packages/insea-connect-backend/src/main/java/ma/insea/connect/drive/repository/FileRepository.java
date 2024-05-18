package ma.insea.connect.drive.repository;

import ma.insea.connect.drive.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long>{
}