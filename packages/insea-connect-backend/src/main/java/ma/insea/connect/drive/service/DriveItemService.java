package ma.insea.connect.drive.service;


import ma.insea.connect.drive.model.DriveItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriveItemService {
    public DriveItem createDriveItem(Long degreePathCode, DriveItem driveItem);
    public List<DriveItem> getDriveItems(Long degreePathCode);
}