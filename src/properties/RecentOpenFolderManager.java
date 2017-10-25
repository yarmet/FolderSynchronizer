package properties;

import lombok.extern.log4j.Log4j;

import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created by ruslan on 12.05.2017.
 */
@Log4j
public class RecentOpenFolderManager {

    public static final String FOLDER1 = "folder1";
    public static final String FOLDER2 = "folder2";

    private Properties properties = new Properties();


    public void load() {
        try (InputStream inputStream = new FileInputStream(new File("lastFolder"))) {
            properties.load(inputStream);
        } catch (IOException e) {
        }
    }


    public void save() {
        try (OutputStream outputStream = new FileOutputStream(new File("lastFolder"))) {
            properties.store(outputStream, new Date().toString());
        } catch (IOException e) {
            log.error(e);
        }
    }

    public void changeFolder(String folderName, String folderPath) {
        properties.put(folderName, folderPath);
    }

    public String getFolderPath(String folderName) {
        String res = properties.getProperty(folderName);
        if (res == null) {
            return System.getProperty("user.dir");
        } else {
            return res;
        }
    }

}
