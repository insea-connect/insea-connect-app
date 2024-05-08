package ma.insea.connect.drive.service;

import ma.insea.connect.drive.model.Folder;

public interface FolderService {
    public Folder getFolderById(Long id);
    public Folder updateFolder(Long id, Folder folder);
    public Folder deleteFolder(Long id);

}
