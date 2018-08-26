package folder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import myutil.LogUtil;
import processor.Processor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ruslan on 09.05.2017.
 */

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Folder {

    private String name;
    private Set<Path> folderState;
    private Set<Path> deletedFiles;
    private Set<Path> newFiles;


    public static Folder scan(String folderName) throws IOException {
        Set<Path> actualFolderState = getPathsByFolderName(folderName);

        Set<Path> deletedFiles;
        Set<Path> newFiles;

        try {
            List<Path> previousFolderState = LogUtil.loadFolderPreviousState(folderName);
            deletedFiles = getDeletedFiles(actualFolderState, previousFolderState);
            newFiles = getNewFiles(actualFolderState, previousFolderState);
        } catch (IOException e) {
            deletedFiles = Collections.emptySet();
            newFiles = actualFolderState;
        }
        return new Folder(folderName, actualFolderState, deletedFiles, newFiles);
    }


    private static Set<Path> getNewFiles(Set<Path> actualState, List<Path> previousState) {
        Set<Path> tmp = new HashSet<>(actualState);
        tmp.removeAll(previousState);
        return tmp;
    }


    private static Set<Path> getDeletedFiles(Set<Path> actualState, List<Path> previousState) {
        Set<Path> tmp = new HashSet<>(previousState);
        tmp.removeAll(actualState);
        return tmp;
    }


    private static Set<Path> getPathsByFolderName(String path) throws IOException {
        Path pathAbsolute = Paths.get(path);
        Set<Path> tmp = Files.walk(Paths.get(path)).filter(Files::isRegularFile).map(pathAbsolute::relativize).collect(Collectors.toSet());
        tmp.remove(Paths.get(Processor.CHANGES_LOG_FILE_NAME));
        return tmp;
    }


}
