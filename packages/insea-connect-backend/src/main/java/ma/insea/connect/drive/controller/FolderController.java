package ma.insea.connect.drive.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;


import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.Folder;
import ma.insea.connect.drive.service.FolderServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/drive/folders")
@RequiredArgsConstructor
public class FolderController {


    @Autowired
    private FolderServiceImpl folderService;


    @GetMapping("/{folderId}/items")
    public ResponseEntity<List<DriveItem>> getItems(@PathVariable Long folderId) {
        if (folderService.getFolderById(folderId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(folderService.getFolderItems(folderId));
    }

    @PostMapping("/{folderId}/items")
    public ResponseEntity<DriveItem> createItem(@PathVariable Long folderId, @RequestBody DriveItem driveItem) {
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

    @PutMapping("/{folderId}")
    public ResponseEntity<Folder> updateFolder(@PathVariable Long folderId, Folder folder) {
        if(folderService.getFolderById(folderId) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(folderService.updateFolder(folderId, folder));
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Boolean> deleteFolder(@PathVariable Long folderId, Folder folder) {

        if (!folderService.deleteFolder(folderId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}