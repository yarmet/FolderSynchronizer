package myutil;

import folder.MyFolder;
import main.Main;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by ruslan on 10.05.2017.
 */
public class LogUtil {


    public static List<String> loadLogFile(String path) throws IOException {
        return Files.lines(Paths.get(path, Main.CHANGES_LOG_FILE_NAME), Charset.forName("UTF-8")).collect(Collectors.toList());
    }

    public static void saveToLogFile(MyFolder folder) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(folder.getName(), Main.CHANGES_LOG_FILE_NAME), Charset.forName("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Path s : folder.getNestedFiles()) {
                writer.append(s.toString()).write('\n');
            }
        }
    }

}
