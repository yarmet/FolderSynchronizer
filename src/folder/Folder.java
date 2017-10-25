package folder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import myutil.LogUtil;
import myutil.FolderUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ruslan on 09.05.2017.
 */

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Folder {

    //название папки
    private String name;

    // список вложенных файлов
    private Set<Path> folderState;

    //список вложенных файлов при прошлом сканировании
    private List<String> previousFolderState;

    public static Folder scan(String folderName) throws IOException {
        Set<Path> paths = FolderUtil.getPathsByFolderName(folderName);
        List<String> logs;
        try {
            logs = LogUtil.loadLogFile(folderName);
        } catch (IOException e) {
            logs = new ArrayList<>();
            for (Path p : paths) logs.add(p.toString());
        }
        return new Folder(folderName, paths, logs);
    }
}
