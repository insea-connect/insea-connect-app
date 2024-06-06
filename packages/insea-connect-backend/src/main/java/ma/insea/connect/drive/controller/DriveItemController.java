package ma.insea.connect.drive.controller;


import lombok.RequiredArgsConstructor;
import ma.insea.connect.drive.dto.DriveItemDto;
import ma.insea.connect.drive.dto.DriveUserDto;
import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.File;
import ma.insea.connect.drive.model.Folder;
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
import java.util.Optional;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DriveItemController {

    private final Functions functions;

    @Autowired
    private ma.insea.connect.drive.service.FolderServiceImpl folderService;
    @Autowired
    private ma.insea.connect.drive.repository.DegreePathRepository degreePathRepository;
    @Autowired
    private ma.insea.connect.drive.repository.FileRepository fileRepository;
    @Autowired
    private ma.insea.connect.drive.repository.DriveItemRepository driveItemRepository;

    @GetMapping("drive/{degreePathId}/folders/{parentId}/items")
    public ResponseEntity<List<DriveItemDto>> getDriveItems(@PathVariable Long degreePathId, @PathVariable Long parentId) {
        List<DriveItem> driveItems;
        if(parentId != 0) {
            Folder folder = folderService.getFolderById(parentId);
            if(folder == null) {return ResponseEntity.notFound().build();}
            driveItems = folder.getChildren();
        }else{
            driveItems = driveItemRepository.findByDegreePathIdAndParent(degreePathId, null);}
        List<DriveItemDto> driveItemDtos = new ArrayList<>();
        if (driveItems == null) {return ResponseEntity.notFound().build();}
        for(DriveItem driveItem : driveItems){
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

            if(driveItem instanceof Folder) {driveItemDto.setFolder(true);}
            if(driveItem instanceof File) {
                driveItemDto.setFolder(false);
                driveItemDto.setSize(((File) driveItem).getSize());
                driveItemDto.setMimeType(((File) driveItem).getMimeType());
                driveItemDto.setItemUrl(((File) driveItem).getFileUrl());}
            driveItemDtos.add(driveItemDto);
        }
        return ResponseEntity.ok(driveItemDtos);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLASS_REP')")
    @PostMapping("drive/{degreePathId}/folders/{parentId}/upload")
    public ResponseEntity<HttpStatus> handleFileUpload(@RequestParam("file") MultipartFile file,@PathVariable Long degreePathId,@PathVariable Long parentId) throws Exception{
        User user=functions.getConnectedUser();
        DegreePath degreePath = degreePathRepository.findById(degreePathId).get();

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
        fileObj.setFileUrl(fileUrl);

        driveUserDto.setUsername(user.getUsername());
        driveUserDto.setEmail(user.getEmail());
        driveUserDto.setId(user.getId());

        if(parentId != 0) {
            Folder folder = folderService.getFolderById(parentId);
            if(folder == null) {return ResponseEntity.notFound().build();}
            fileObj.setParent(folder);
        }else{
                fileObj.setParent(null);
            }
        fileRepository.save(fileObj);

        return new ResponseEntity(HttpStatus.CREATED);

    }
    @GetMapping("/degreePaths")
    public List<DegreePath> getDegreePaths() {
        return degreePathRepository.findAll();
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLASS_REP')")
    @DeleteMapping("drive/items/{itemId}")
    public ResponseEntity<HttpStatus> deleteDriveItem( @PathVariable Long itemId) {
        User user = functions.getConnectedUser();
        Optional<DriveItem> optionalDriveItem = driveItemRepository.findById(itemId);
        if (!optionalDriveItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        DriveItem driveItem = optionalDriveItem.get();        if(!functions.checkPermission(user, driveItem.getDegreePath())){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if(driveItem instanceof Folder){
            folderService.deleteFolder(itemId);
        }else{
            fileRepository.deleteById(itemId);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
}