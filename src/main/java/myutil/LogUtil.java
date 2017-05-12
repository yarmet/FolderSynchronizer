package myutil;

import folder.MyFolder;
import main.Main;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by ruslan on 10.05.2017.
 */
public class LogUtil {

    public static List<String> loadLogFile(String path) throws IOException {
        return Files.lines(Paths.get(path, Main.LOG_FILE)).collect(Collectors.toList());
    }

    public static void saveToLogFile(MyFolder folder) throws IOException {
        try (FileOutputStream fo = new FileOutputStream(String.valueOf(Paths.get(folder.getName(), Main.LOG_FILE))); PrintWriter pw = new PrintWriter(fo)) {
            for (Path elem : folder.getNestedFiles())
                pw.println(elem);
        }
    }

}
