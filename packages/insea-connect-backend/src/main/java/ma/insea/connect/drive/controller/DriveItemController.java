package ma.insea.connect.drive.controller;


import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.service.DriveItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drive")
public class DriveItemController {

    @Autowired
    private DriveItemServiceImpl driveItemService;



    @GetMapping("/degreePaths/{degreePathCode}/items")
    public ResponseEntity<List<DriveItem>> getDriveItems(@PathVariable Long degreePathCode) {
        if (driveItemService.getDriveItems(degreePathCode) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driveItemService.getDriveItems(degreePathCode));
    }

    @PostMapping("/degreePaths/{degreePathCode}/items")
    public ResponseEntity<DriveItem> CreateDriveItem(@PathVariable Long degreePathCode, @RequestBody DriveItem driveItem) {
        if (driveItemService.createDriveItem(degreePathCode, driveItem) == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(driveItem);
    }
}