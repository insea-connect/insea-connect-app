package ma.insea.connect.drive.controller;


import lombok.RequiredArgsConstructor;
import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.File;
import ma.insea.connect.drive.model.Folder;
import ma.insea.connect.drive.service.DriveItemServiceImpl;
import ma.insea.connect.utils.Functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/drive")
@RequiredArgsConstructor
public class DriveItemController {

    private static final String UPLOAD_DIR = "uploads";
    private final Functions functions;

    @Autowired
    private DriveItemServiceImpl driveItemService;



    @GetMapping("/degreePaths/{degreePathCode}/items")
    public ResponseEntity<List<DriveItem>> getDriveItems(@PathVariable Long degreePathCode) {
        if (driveItemService.getDriveItems(degreePathCode) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driveItemService.getDriveItems(degreePathCode));
    }

    @PostMapping("/degreePaths/{degreePathCode}/folder")
    public ResponseEntity<Folder> CreateDriveItem(@PathVariable Long degreePathCode, @RequestBody Folder folder) {
        if (driveItemService.createDriveItem(degreePathCode, folder) == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(folder);
    }

    @PostMapping("/degreePaths/{degreePathCode}/upload")
    public File handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {return null;}

        File fileObj = new File();
        fileObj.setFileUrl(functions.uploadFile(file));
        fileObj.setName(file.getOriginalFilename());
        fileObj.setSize(file.getSize());
        fileObj.setMimeType(file.getContentType());
        fileObj.setCreatedAt(LocalDateTime.now());

        return fileObj;

    }
}