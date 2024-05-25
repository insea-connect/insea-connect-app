package ma.insea.connect.drive.controller;


import lombok.RequiredArgsConstructor;
import ma.insea.connect.drive.dto.DriveItemDto;
import ma.insea.connect.drive.dto.FolderDto;
import ma.insea.connect.drive.dto.DriveUserDto;
import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.File;
import ma.insea.connect.drive.model.Folder;
import ma.insea.connect.drive.service.DriveItemServiceImpl;
import ma.insea.connect.user.DegreePath;
import ma.insea.connect.user.User;
import ma.insea.connect.utils.Functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/drive")
@RequiredArgsConstructor
public class DriveItemController {

    private final Functions functions;

    @Autowired
    private DriveItemServiceImpl driveItemService;
    @Autowired
    private ma.insea.connect.drive.service.FolderServiceImpl folderService;
    @Autowired
    private ma.insea.connect.drive.repository.DegreePathRepository degreePathRepository;
    @Autowired
    private ma.insea.connect.drive.repository.FileRepository fileRepository;

    @GetMapping("/degreePaths/{degreePathCode}/items")
    public ResponseEntity<List<DriveItemDto>> getDriveItems(@PathVariable Long degreePathCode) {
        List<DriveItemDto> driveItemDtos = new ArrayList<>();
        if (driveItemService.getDriveItems(degreePathCode) == null) {
            return ResponseEntity.notFound().build();
        }
        for(DriveItem driveItem : driveItemService.getDriveItems(degreePathCode)){
            DriveItemDto driveItemDto = new DriveItemDto();
            DriveUserDto driveUserDto = new DriveUserDto();

            driveUserDto.setId(driveItem.getCreator().getId());
            driveUserDto.setEmail(driveItem.getCreator().getEmail());
            driveUserDto.setUsername(driveItem.getCreator().getUsername());

            driveItemDto.setId(driveItem.getId());
            driveItemDto.setName(driveItem.getName());
            driveItemDto.setDescription(driveItem.getDescription());
            driveItemDto.setCreatedAt(driveItem.getCreatedAt());
            driveItemDto.setUpdatedAt(driveItem.getUpdatedAt());
            driveItemDto.setCreator(driveUserDto);
            driveItemDto.setDegreePath(driveItem.getDegreePath());

            driveItemDto.setParent(null);
            if(driveItem instanceof Folder) {
                driveItemDto.setFolder(true);
            }
            driveItemDtos.add(driveItemDto);
        }

        return ResponseEntity.ok(driveItemDtos);
    }

    @PreAuthorize("hasRole('CLASS_REP')")
    @PostMapping("/degreePaths/{degreePathCode}/folder")
    public ResponseEntity<FolderDto> CreateDriveItem(@PathVariable Long degreePathCode, @RequestBody FolderDto folderDto) {

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

        if (driveItemService.createDriveItem(degreePathCode, folder) == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(folderDto);
    }

    @PreAuthorize("hasRole('CLASS_REP')")
    @PostMapping("/degreePaths/{degreePathCode}/upload")
    public ResponseEntity<DriveItemDto> handleFileUpload(@RequestParam("file") MultipartFile file,@PathVariable Long degreePathCode) throws Exception{
        User user=functions.getConnectedUser();
        DegreePath degreePath = degreePathRepository.findById(degreePathCode).get();

        DriveItemDto driveItemDto = new DriveItemDto();
        DriveUserDto driveUserDto = new DriveUserDto();
        File fileObj = new File();

        if(!functions.checkPermission(user, degreePath)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if (file.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String fileUrl = functions.uploadFile(file);

        fileObj.setCreatedAt(LocalDateTime.now());
        fileObj.setDegreePath(degreePath);
        fileObj.setCreator(user);
        fileObj.setDescription(null);
        fileObj.setName(file.getOriginalFilename());
        fileObj.setSize(file.getSize());
        fileObj.setMimeType(file.getContentType());
        fileObj.setParent(null);
        fileObj.setFileUrl(fileUrl);

        driveUserDto.setUsername(user.getUsername());
        driveUserDto.setEmail(user.getEmail());
        driveUserDto.setId(user.getId());

        driveItemDto.setItemUrl(fileUrl);
        driveItemDto.setName(file.getOriginalFilename());
        driveItemDto.setSize(file.getSize());
        driveItemDto.setMimeType(file.getContentType());
        driveItemDto.setCreatedAt(LocalDateTime.now());

        driveItemDto.setCreator(driveUserDto);

        driveItemDto.setParent(null);
        driveItemDto.setDegreePath(degreePath);

        fileRepository.save(fileObj);

        return ResponseEntity.ok(driveItemDto);

    }
    @PreAuthorize("hasRole('CLASS_REP')")
    @PostMapping("/{folderId}/upload")
    public File handleFileUploadOnFolder(@RequestParam("file") MultipartFile file, @PathVariable Long folderId) throws Exception{

        User user = functions.getConnectedUser();
        if(!functions.checkPermission(user, folderService.getFolderById(folderId).getDegreePath())){
            return null;
        }
        if (file.isEmpty()) {return null;}

        File fileObj = new File();
        fileObj.setFileUrl(functions.uploadFile(file));
        fileObj.setName(file.getOriginalFilename());
        fileObj.setSize(file.getSize());
        fileObj.setMimeType(file.getContentType());
        fileObj.setCreatedAt(LocalDateTime.now());
        Folder folder = folderService.getFolderById(folderId);
        fileObj.setParent(folder);
        return fileObj;
    }
    @GetMapping("/degreePaths")
    public List<DegreePath> getDegreePaths() {
        return degreePathRepository.findAll();
    }
    
}