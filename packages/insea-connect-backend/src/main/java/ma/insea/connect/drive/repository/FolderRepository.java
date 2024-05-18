package ma.insea.connect.drive.repository;

import ma.insea.connect.drive.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}
