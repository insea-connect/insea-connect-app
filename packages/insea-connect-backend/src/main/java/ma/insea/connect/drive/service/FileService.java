package ma.insea.connect.drive.service;

import ma.insea.connect.drive.model.File;

import java.util.Optional;

public interface FileService {
    public Optional<File> getFolderById(Long id);
    public void updateFolder(Long id, File file);
    public void deleteFolder(Long id);
}
