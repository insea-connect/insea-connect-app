package ma.insea.connect.drive.service;

import ma.insea.connect.drive.dto.DriveUserDto;
import ma.insea.connect.drive.dto.FolderDto;
import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.Folder;
import ma.insea.connect.drive.repository.DegreePathRepository;
import ma.insea.connect.drive.repository.DriveItemRepository;
import ma.insea.connect.drive.repository.FolderRepository;
import ma.insea.connect.user.DegreePath;
import ma.insea.connect.user.User;
import ma.insea.connect.utils.Functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DriveItemServiceImpl implements DriveItemService {
    private final DriveItemRepository driveItemRepository;
    private final DegreePathRepository degreePathRepository;
    private final Functions functions;
    private final FolderRepository folderRepository;




    @Override
    public DriveItem createDriveItem(Long degreePathCode, DriveItem driveItem) {
        User user = functions.getConnectedUser();
        DegreePath degreePath = degreePathRepository.findById(degreePathCode).get();
        System.out.println("Filiere user : "+user.getDegreePath().toString());
        System.out.println("Filiere driveItem : "+degreePath.toString());
        if(!user.getDegreePath().getMajor().equals(degreePath.getMajor())){
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

    public FolderDto createFolder(Long degreePathCode, FolderDto folderDto) {
        User user = functions.getConnectedUser();
        DriveUserDto driveUserDto = new DriveUserDto();
        Folder folder = new Folder();
        folder.setName(folderDto.getName());
        folder.setCreatedAt(LocalDateTime.now());
        folder.setDegreePath(degreePathRepository.findById(degreePathCode).get());
        folder.setDescription(folderDto.getDescription());
        folder.setParent(null);
        folder.setCreator(functions.getConnectedUser());

        driveUserDto.setId(user.getId());
        driveUserDto.setEmail(user.getEmail());
        driveUserDto.setUsername(user.getUsername());

        folderDto.setCreator(driveUserDto);
        folderRepository.save(folder);

        return folderDto;
    }
}