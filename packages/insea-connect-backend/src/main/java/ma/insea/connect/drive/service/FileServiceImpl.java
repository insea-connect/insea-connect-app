package ma.insea.connect.drive.service;

import ma.insea.connect.drive.model.File;
import ma.insea.connect.drive.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileServiceImpl implements FileService{

    private FileRepository fileRepository;

    @Override
    public Optional<File> getFolderById(Long id) {
        return fileRepository.findById(id);
    }

    @Override
    public void updateFolder(Long id, File file) {
    }

    @Override
    public void deleteFolder(Long id) {
    }
}
