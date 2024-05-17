package ma.insea.connect.drive.service;

import ma.insea.connect.drive.model.File;
import ma.insea.connect.drive.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LongSummaryStatistics;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService{

    private FileRepository fileRepository;

    @Override
    public File getFileById(Long id) {
        if (!fileRepository.existsById(id)) {
            return null;
        }
        return fileRepository.findById(id).get();
    }

    @Override
    public File updateFile(Long id, File file) {
        if (!fileRepository.existsById(id)) {
            return null;
        }
        File fileToUpdate = fileRepository.findById(id).get();
        fileToUpdate.setFileUrl(file.getFileUrl());
        fileToUpdate.setUpdatedAt(LocalDateTime.now());
        fileToUpdate.setDescription(file.getDescription());
        fileToUpdate.setParent(file.getParent());

        fileRepository.save(fileToUpdate);
        return fileToUpdate;
    }

    @Override
    public boolean deleteFile(Long id) {
        if (!fileRepository.existsById(id)) {
            return false;
        }
        fileRepository.deleteById(id);
        return true;
    }
}
