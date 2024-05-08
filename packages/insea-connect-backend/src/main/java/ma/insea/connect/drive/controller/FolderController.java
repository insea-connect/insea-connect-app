package ma.insea.connect.drive.controller;


import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.Folder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drive/folders")
public class FolderController {


    @GetMapping("/{folderId}/items")
    public List<DriveItem> getItems(@PathVariable Long folderId) {
        return null;
    }

    @PostMapping("/{folderId}/items")
    public void createItem(@PathVariable Long folderId, @RequestBody DriveItem driveItem) {
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<Folder> getFolder(@PathVariable Long folderId) {
        return null;
    }

    @PutMapping("/{folderId}")
    public void updateFolder(@PathVariable Long folderId, Folder folder) {
    }

    @PostMapping("/{folderId}")
    public void createFolder(@PathVariable Long folderId, Folder folder) {
    }
}
