package ma.insea.connect.drive.repository;

import ma.insea.connect.drive.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long>{
}