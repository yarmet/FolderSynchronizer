package myutil;

import folder.Folder;
import processor.Processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ruslan on 09.05.2017.
 */
public class FolderUtil {

    public static Set<Path> getPathsByFolderName(String path) throws IOException {
        Path pathAbsolute = Paths.get(path);
        Set<Path> tmp = Files.walk(Paths.get(path)).filter(Files::isRegularFile).map(pathAbsolute::relativize).collect(Collectors.toSet());
        tmp.remove(Paths.get(Processor.CHANGES_LOG_FILE_NAME));
        return tmp;
    }


    private static void createParentDirectoryIfNeed(Path path) throws IOException {
        Path tmp = path.getParent();
        if (tmp != null) Files.createDirectories(tmp);
    }


    /**
     * получаем список файлов папки 1
     * если вторая папка не содержит такой файл, то вставляем его туда
     * если вторая папка уже имеет такой файл, но дата его модификации более старая чем у файла из первого файла, то тоже обновляем его.
     */
    public static void copyOrCreateNewFiles(Folder sourceFolder, Folder destFolder) throws IOException {
        for (Path path : sourceFolder.getFolderState()) {
            Path sourceFolderFullPath = Paths.get(sourceFolder.getName(), path.toString());
            Path destFolderFullPath = Paths.get(destFolder.getName(), path.toString());

            if (!destFolder.getFolderState().contains(path)) {  // if destination file not found.
                createParentDirectoryIfNeed(destFolderFullPath);
                Files.copy(sourceFolderFullPath, destFolderFullPath);
                destFolder.getFolderState().add(path);
            } else {
                FileTime sourceFileModifiedTime = Files.getLastModifiedTime(sourceFolderFullPath);
                FileTime newFileModifiedTime = Files.getLastModifiedTime(destFolderFullPath);

                if (sourceFileModifiedTime.compareTo(newFileModifiedTime) > 0) {  // if destination file older than source file.
                    Files.copy(sourceFolderFullPath, destFolderFullPath, StandardCopyOption.REPLACE_EXISTING);
                    destFolder.getFolderState().add(path);
                }
            }
        }
    }

    /**
     * смотрим старое состояние папки 1 (лог файл в котором список файлов которые имелись при прошлом сканировании).
     * если в старом состоянии (лог файле) есть что-то, чего нет в текущем состоянии, значит это было удалено из папки 1
     * значит удаляем это из папки 2
     */
    public static void delete(Folder folder1, Folder folder2) throws IOException {
        for (String prevFolderState : folder1.getPreviousFolderState()) {
            Path oldPaths = Paths.get(prevFolderState);
            if (!folder1.getFolderState().contains(oldPaths)) {
                Files.deleteIfExists(Paths.get(folder2.getName(), prevFolderState));
                folder2.getFolderState().remove(oldPaths);
            }
        }
    }


}
