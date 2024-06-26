package ma.insea.connect.drive.service;

import ma.insea.connect.drive.model.File;


public interface FileService {
    public File getFileById(Long id);
    public File updateFile(Long id, File file);
    public boolean deleteFile(Long id);
}