package ma.insea.connect.drive.controller;


import lombok.RequiredArgsConstructor;
import ma.insea.connect.drive.model.File;
import ma.insea.connect.drive.repository.FileRepository;
import ma.insea.connect.drive.service.FileServiceImpl;
import ma.insea.connect.user.User;
import ma.insea.connect.utils.Functions;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/drive/files")
@RequiredArgsConstructor
public class FileController {

    private final FileServiceImpl fileService;
    private final FileRepository fileRepository;
    private final Functions functions;
    
    @GetMapping("/{fileId}")
    public ResponseEntity<File> getFile(@PathVariable Long fileId) {
        if (!fileRepository.existsById(fileId)) {
            return ResponseEntity.notFound().build();
        }
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok(file);
    }

    @PreAuthorize("hasRole('CLASS_REP')")
    @PutMapping("/{fileId}")
    public ResponseEntity<File> updateFile(@PathVariable Long fileId, File file) {
        User user = functions.getConnectedUser();
        if(!functions.checkPermission(user, file.getDegreePath())){
            return ResponseEntity.notFound().build();
        }
        if(!fileRepository.existsById(fileId)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fileService.updateFile(fileId, file));
    }
}