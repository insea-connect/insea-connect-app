package ma.insea.connect.drive.controller;


import lombok.RequiredArgsConstructor;
import ma.insea.connect.drive.model.File;
import ma.insea.connect.drive.repository.FileRepository;
import ma.insea.connect.drive.service.FileServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drive/files")
@RequiredArgsConstructor
public class FileController {

    private final FileServiceImpl fileService;
    private final FileRepository fileRepository;
    
    @GetMapping("/{fileId}")
    public ResponseEntity<File> getFile(@PathVariable Long fileId) {
        if (fileRepository.existsById(fileId)) {
            return ResponseEntity.notFound().build();
        }
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok(file);
    }

    @PreAuthorize("hasRole('CLASS_REP')")
    @PutMapping("/{fileId}")
    public ResponseEntity<File> updateFile(@PathVariable Long fileId, File file) {
        if(fileRepository.existsById(fileId)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fileService.updateFile(fileId, file));
    }

    @PreAuthorize("hasRole('CLASS_REP')")
    @DeleteMapping("/{fileId}")
    public ResponseEntity<File> deleteFile(@PathVariable Long fileId) {
        if (!fileService.deleteFile(fileId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}