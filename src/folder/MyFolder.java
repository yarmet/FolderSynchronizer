package folder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import myutil.LogUtil;
import myutil.MyFilesUtil;

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
public class MyFolder {

    private String name;
    private Set<Path> nestedFiles;
    private List<String> logs;

    public static MyFolder scan(String folderName) throws IOException {
        Set<Path> paths = MyFilesUtil.getPathsByFolderName(folderName);
        List<String> logs;
        try {
            logs = LogUtil.loadLogFile(folderName);
        } catch (IOException e) {
            logs = new ArrayList<>();
            for (Path p : paths) logs.add(p.toString());
        }
        return new MyFolder(folderName, paths, logs);
    }
}