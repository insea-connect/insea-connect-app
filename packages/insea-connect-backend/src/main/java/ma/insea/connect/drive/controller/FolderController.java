package ma.insea.connect.drive.controller;

import ma.insea.connect.drive.model.File;
import ma.insea.connect.user.User;
import ma.insea.connect.utils.Functions;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;


import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.Folder;
import ma.insea.connect.drive.service.FolderServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/drive/folders")
@RequiredArgsConstructor
public class FolderController {


    @Autowired
    private FolderServiceImpl folderService;
    private final Functions functions;


    @GetMapping("/{folderId}/items")
    public ResponseEntity<List<DriveItem>> getItems(@PathVariable Long folderId) {
        if (folderService.getFolderById(folderId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(folderService.getFolderItems(folderId));
    }

    @PreAuthorize("hasRole('CLASS_REP')")
    @PostMapping("/{folderId}/items")
    public ResponseEntity<DriveItem> createItem(@PathVariable Long folderId, @RequestBody DriveItem driveItem) {
        User user = functions.getConnectedUser();
        if(!functions.checkPermission(user, driveItem.getDegreePath())){
            return ResponseEntity.notFound().build();
        }
        if (folderService.getFolderById(folderId) == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(folderService.createFolderItem(folderId, driveItem));
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<Folder> getFolder(@PathVariable Long folderId) {
        if (folderService.getFolderById(folderId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(folderService.getFolderById(folderId));
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

    @PostMapping("/{folderId}/upload")
    public File handleFileUpload(@PathVariable Long folderId, @RequestParam("file") MultipartFile file) {
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
        fileObj.setParent(folderService.getFolderById(folderId));

        return fileObj;
    }
}