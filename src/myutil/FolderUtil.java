package myutil;

import folder.Folder;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;

/**
 * Created by ruslan on 09.05.2017.
 */
public class FolderUtil {

    private static String CREATING_NEW_FILE = "Создаю новый файл: ";
    private static String UPDATING_FILE = "Обновляю измененный файл: ";
    private static String DELETING_FILE = "Удаляем: ";


    private static void createParentDirectoryIfNeed(Path path) throws IOException {
        Path tmp = path.getParent();
        if (tmp != null) Files.createDirectories(tmp);
    }

    /**
     * получаем список файлов папки 1
     * если вторая папка не содержит такой файл, то вставляем его туда
     * если вторая папка уже имеет такой файл, но дата его модификации более старая чем у файла из первого файла, то тоже обновляем его.
     */
    public static void copyOrCreateNewFiles(Folder sourceFolder, Folder destFolder, JLabel statusLabel) throws IOException {
        for (Path newFile : sourceFolder.getNewFiles()) {
            Path sourceFolderFullPath = Paths.get(sourceFolder.getName(), newFile.toString());
            Path destFolderFullPath = Paths.get(destFolder.getName(), newFile.toString());

            if (!destFolder.getFolderState().contains(newFile)) {
                statusLabel.setText(CREATING_NEW_FILE.concat(newFile.toString()));
                createParentDirectoryIfNeed(destFolderFullPath);
                Files.copy(sourceFolderFullPath, destFolderFullPath);
                destFolder.getFolderState().add(newFile);
            } else {
                FileTime sourceFileModifiedTime = Files.getLastModifiedTime(sourceFolderFullPath);
                FileTime newFileModifiedTime = Files.getLastModifiedTime(destFolderFullPath);

                if (sourceFileModifiedTime.compareTo(newFileModifiedTime) > 0) {
                    statusLabel.setText(UPDATING_FILE.concat(newFile.toString()));
                    Files.copy(sourceFolderFullPath, destFolderFullPath, StandardCopyOption.REPLACE_EXISTING);
                    destFolder.getFolderState().add(newFile);
                }
            }
        }
    }


    /**
     * смотрим старое состояние папки 1 (лог файл в котором список файлов которые имелись при прошлом сканировании).
     * если в старом состоянии (лог файле) есть что-то, чего нет в текущем состоянии, значит это было удалено из папки 1
     * значит удаляем это из папки 2
     */
    public static void delete(Folder folder1, Folder folder2, JLabel statusLabel) throws IOException {
        for (Path oldPath : folder1.getDeletedFiles()) {
            boolean deleted = Files.deleteIfExists(Paths.get(folder2.getName(), oldPath.toString()));
            if (deleted) {
                folder2.getFolderState().remove(oldPath);
                statusLabel.setText(DELETING_FILE + oldPath.toString());
            }
        }
    }


}
