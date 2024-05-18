package ma.insea.connect.drive.service;

import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.Folder;
import ma.insea.connect.drive.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService{

    @Autowired
    private FolderRepository folderRepository;


    @Override
    public Folder getFolderById(Long id) {
        if (!folderRepository.existsById(id)) {
            return null;
        }
        return folderRepository.findById(id).get();
    }

    @Override
    public Folder updateFolder(Long id, Folder folder) {
        if (!folderRepository.existsById(id)) {
            return null;
        }
        Folder folderToUpdate = folderRepository.findById(id).get();
        folderToUpdate.setName(folder.getName());
        folderToUpdate.setUpdatedAt(folder.getUpdatedAt());
        folderToUpdate.setParent(folder.getParent());

        folderRepository.save(folderToUpdate);
        return folderToUpdate;
    }

    @Override
    public Boolean deleteFolder(Long id) {
        if (!folderRepository.existsById(id)) {
            return false;
        }
        folderRepository.deleteById(id);
        return true;
    }



    @Override
    public List<DriveItem> getFolderItems(Long id) {
        if(!folderRepository.existsById(id)){
            return null;
        }
        return folderRepository.findById(id).get().getChildren();
    }

    @Override
    public Folder createFolderItem(Long id, DriveItem driveItem) {
        if(!folderRepository.existsById(id)){
            return null;
        }
        Folder folder = folderRepository.findById(id).get();
        folder.getChildren().add(driveItem);
        return folderRepository.save(folder);
    }
}