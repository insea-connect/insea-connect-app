package ma.insea.connect.drive.service;

import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.repository.DegreePathRepository;
import ma.insea.connect.drive.repository.DriveItemRepository;
import ma.insea.connect.user.DegreePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DriveItemServiceImpl implements DriveItemService {

    @Autowired
    public DriveItemRepository driveItemRepository;

    @Autowired
    public DegreePathRepository degreePathRepository;



    @Override
    public DriveItem createDriveItem(Long degreePathCode, DriveItem driveItem) {
        if(!degreePathRepository.existsById(degreePathCode)){
            return null;
        }
        DegreePath degreePath = degreePathRepository.findById(degreePathCode).get();
        driveItem.setDegreePath(degreePath);
        degreePath.getDriveItems().add(driveItem);
        return driveItemRepository.save(driveItem);
    }

    @Override
    public List<DriveItem> getDriveItems(Long degreePathCode) {
        if(!degreePathRepository.existsById(degreePathCode)){
            return null;
        }
        return driveItemRepository.findByDegreePathId(degreePathCode);
    }
}
