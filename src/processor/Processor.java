package processor;

import folder.Folder;
import lombok.extern.log4j.Log4j;
import myutil.LogUtil;
import myutil.FolderUtil;
import javax.swing.*;


@Log4j
public class Processor {

    public static String CHANGES_LOG_FILE_NAME = "Folder-Synchronizer";

    public void start(String selectedFolder1, String selectedFolder2, JLabel statusLabel) {
        try {
            statusLabel.setText("сканируем папки");
            Folder folder1 = Folder.scan(selectedFolder1);
            Folder folder2 = Folder.scan(selectedFolder2);

            statusLabel.setText("синхронизируем удаленные файлы");
            FolderUtil.delete(folder1, folder2);
            FolderUtil.delete(folder2, folder1);

            statusLabel.setText("синхронизируем новые/обновленные файлы");
            FolderUtil.copyOrCreateNewFiles(folder1, folder2);
            FolderUtil.copyOrCreateNewFiles(folder2, folder1);

            statusLabel.setText("записываем изменения");
            LogUtil.saveToLogFile(folder1);
            LogUtil.saveToLogFile(folder2);

            statusLabel.setText("завершено");
        } catch (Exception e) {
            statusLabel.setText("ошибка, смотри лог файл");
            log.error(e);
        }
    }


}
