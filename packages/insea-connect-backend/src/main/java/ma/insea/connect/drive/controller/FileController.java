package ma.insea.connect.drive.controller;


import lombok.RequiredArgsConstructor;
import ma.insea.connect.drive.model.File;
import ma.insea.connect.drive.model.Folder;
import ma.insea.connect.drive.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drive/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/{fileId}")
    public ResponseEntity<File> getFile(@PathVariable Long fileId) {
        return fileService.getFolderById(fileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{fileId}")
    public void updateFile(@PathVariable Long fileId, File file) {

    }

    @DeleteMapping("/{fileId}")
    public void deleteFile(@PathVariable Long fileId, File file) {
    }

}
