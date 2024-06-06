package ma.insea.connect.drive.repository;

import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.Folder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriveItemRepository extends JpaRepository<DriveItem, Long> {
    public List<DriveItem> findByDegreePathId(Long degreePathId);

    public List<DriveItem> findByDegreePathIdAndParent(Long degreePathId,Folder object);
}