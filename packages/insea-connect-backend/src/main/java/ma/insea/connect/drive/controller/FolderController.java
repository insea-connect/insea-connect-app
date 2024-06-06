package ma.insea.connect.drive.controller;

import ma.insea.connect.chat.group.GroupRepository;
import ma.insea.connect.drive.dto.DriveItemDto;
import ma.insea.connect.drive.dto.DriveUserDto;
import ma.insea.connect.drive.dto.FolderDto;
import ma.insea.connect.drive.model.File;
import ma.insea.connect.drive.service.DriveItemService;
import ma.insea.connect.drive.service.FolderService;
import ma.insea.connect.user.DegreePath;
import ma.insea.connect.user.User;
import ma.insea.connect.utils.Functions;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;


import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.Folder;
import ma.insea.connect.drive.repository.DegreePathRepository;
import ma.insea.connect.drive.repository.FileRepository;
import ma.insea.connect.drive.repository.FolderRepository;
import ma.insea.connect.drive.service.FolderServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/drive/folders")
@RequiredArgsConstructor
public class FolderController {


    private final FolderServiceImpl folderService;
    private final Functions functions;
    private final DriveItemService driveItemService;
    private final DegreePathRepository degreePathRepository;
    private final FolderRepository folderRepository;

    @PreAuthorize("hasRole('CLASS_REP')")
    @PostMapping("drive/{degreePathId}/folders/{parentId}/items")
    public ResponseEntity<FolderDto> createItem(@PathVariable Long degreePathId, @PathVariable Long parentId, @RequestBody FolderDto folderDto) {
        if(parentId==0){
            Folder folder = new Folder();
            folder.setName(folderDto.getName());
            folder.setCreatedAt(LocalDateTime.now());
            folder.setDegreePath(degreePathRepository.findById(degreePathId).get());
            folder.setDescription(folderDto.getDescription());
            folder.setParent(null);
            folder.setCreator(functions.getConnectedUser());
            folderRepository.save(folder);
            return ResponseEntity.ok(folderDto);
        }else{
        User user = functions.getConnectedUser();
        DriveUserDto driveUserDto = new DriveUserDto();
        Folder parent = folderService.getFolderById(parentId);
        
        FolderDto parentDto = new FolderDto();
        parentDto.setName(parent.getName());
        parentDto.setDescription(parent.getDescription());
        parentDto.setCreator(driveUserDto);
        parentDto.setParent(null);
        folderDto.setParent(parentDto);
        
        Folder folder = new Folder();
        folder.setParent(parent);
        folder.setName(folderDto.getName());
        folder.setCreatedAt(LocalDateTime.now());
        folder.setDegreePath(degreePathRepository.findById(degreePathId).get());
        folder.setDescription(folderDto.getDescription());
        folder.setCreator(functions.getConnectedUser());
        folderRepository.save(folder);

        driveUserDto.setId(user.getId());
        driveUserDto.setEmail(user.getEmail());
        driveUserDto.setUsername(user.getUsername());

        folderDto.setCreator(driveUserDto);

        if (folderService.createFolderItem(parentId, folder) == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(folderDto);}
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<FolderDto> getFolder(@PathVariable Long folderId) {
        Folder folder = folderService.getFolderById(folderId);

        if (folderService.getFolderById(folderId) == null) {
            return ResponseEntity.notFound().build();
        }

        FolderDto folderDto = new FolderDto();
        folderDto.setName(folder.getName());
        folderDto.setDescription(folder.getDescription());
        DriveUserDto driveUserDto = new DriveUserDto();
        driveUserDto.setId(folder.getCreator().getId());
        driveUserDto.setEmail(folder.getCreator().getEmail());
        driveUserDto.setUsername(folder.getCreator().getUsername());
        folderDto.setCreator(driveUserDto);
        folderDto.setParent(null);

        return ResponseEntity.ok(folderDto);
    }

    @PreAuthorize("hasRole('CLASS_REP')")
    @PutMapping("/{folderId}")
    public ResponseEntity<Folder> updateFolder(@PathVariable Long folderId, Folder folder) {
        User user = functions.getConnectedUser();
        if(!functions.checkPermission(user, folder.getDegreePath())){
            return ResponseEntity.notFound().build();
        }
        if(folderService.getFolderById(folderId) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(folderService.updateFolder(folderId, folder));
    }

    @PreAuthorize("hasRole('CLASS_REP')")
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Boolean> deleteFolder(@PathVariable Long folderId, Folder folder) {
        User user = functions.getConnectedUser();
        if(!functions.checkPermission(user, folder.getDegreePath())){
            return ResponseEntity.notFound().build();
        }
        if (!folderService.deleteFolder(folderId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}