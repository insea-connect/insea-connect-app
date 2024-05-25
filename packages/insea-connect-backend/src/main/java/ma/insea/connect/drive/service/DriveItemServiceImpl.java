package ma.insea.connect.drive.service;

import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.repository.DegreePathRepository;
import ma.insea.connect.drive.repository.DriveItemRepository;
import ma.insea.connect.user.DegreePath;
import ma.insea.connect.user.User;
import ma.insea.connect.utils.Functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DriveItemServiceImpl implements DriveItemService {
    private final DriveItemRepository driveItemRepository;
    private final DegreePathRepository degreePathRepository;
    private final Functions functions;




    @Override
    public DriveItem createDriveItem(Long degreePathCode, DriveItem driveItem) {
        User user = functions.getConnectedUser();
        DegreePath degreePath = degreePathRepository.findById(degreePathCode).get();
        if(!user.getDegreePath().equals(degreePath)){
            return null;
        }
        if(!degreePathRepository.existsById(degreePathCode)){
            return null;
        }
        
        driveItem.setDegreePath(degreePath);
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