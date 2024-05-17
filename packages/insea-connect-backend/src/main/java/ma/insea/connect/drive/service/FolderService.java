package ma.insea.connect.drive.service;

import ma.insea.connect.drive.model.DriveItem;
import ma.insea.connect.drive.model.Folder;

import java.util.List;

public interface FolderService {
    public Folder getFolderById(Long id);
    public Folder updateFolder(Long id, Folder folder);
    public Boolean deleteFolder(Long id);
    public List<DriveItem> getFolderItems(Long id);
    public Folder createFolderItem(Long id, DriveItem driveItem);

}
