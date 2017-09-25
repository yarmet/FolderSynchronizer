package myutil;

import folder.MyFolder;
import main.Main;

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
public class MyFilesUtil {

    public static Set<Path> getPathsByFolderName(String path) throws IOException {
        Path pathAbsolute = Paths.get(path);
        Set<Path> tmp = Files.walk(Paths.get(path)).filter(Files::isRegularFile).map(pathAbsolute::relativize).collect(Collectors.toSet());
        tmp.remove(Paths.get(Main.CHANGES_LOG_FILE_NAME));
        return tmp;
    }


    private static void createParentDirectoriesIfNeed(Path path) throws IOException {
        Path tmp = path.getParent();
        if (tmp != null) Files.createDirectories(tmp);
    }


    public static void copyOrCreateNewFiles(MyFolder sourceFolder, MyFolder destFolder) throws IOException {
        for (Path path : sourceFolder.getNestedFiles()) {
            Path sourceFolderFullPath = Paths.get(sourceFolder.getName(), path.toString());
            Path destFolderFullPath = Paths.get(destFolder.getName(), path.toString());

            if (!destFolder.getNestedFiles().contains(path)) {  // if destination file not found.
                createParentDirectoriesIfNeed(destFolderFullPath);
                Files.copy(sourceFolderFullPath, destFolderFullPath);
                destFolder.getNestedFiles().add(path);
            } else {
                FileTime sourceFileModifiedTime = Files.getLastModifiedTime(sourceFolderFullPath);
                FileTime newFileModifiedTime = Files.getLastModifiedTime(destFolderFullPath);

                if (sourceFileModifiedTime.compareTo(newFileModifiedTime) > 0) {  // if destination file older than source file.
                    Files.copy(sourceFolderFullPath, destFolderFullPath, StandardCopyOption.REPLACE_EXISTING);
                    destFolder.getNestedFiles().add(path);
                }
            }
        }
    }


    public static void delete(MyFolder folder1, MyFolder folder2) throws IOException {
        for (String prev : folder1.getLogs()) {
            Path tmpPath = Paths.get(prev);
            if (!folder1.getNestedFiles().contains(tmpPath)) {
                Files.deleteIfExists(Paths.get(folder2.getName(), prev));
                folder2.getNestedFiles().remove(tmpPath);
            }
        }
    }


}
