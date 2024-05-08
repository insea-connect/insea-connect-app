package ma.insea.connect.drive.repository;

import ma.insea.connect.drive.model.DriveItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriveItemRepository extends JpaRepository<DriveItem, Long> {
}
