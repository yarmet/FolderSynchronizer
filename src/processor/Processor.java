package processor;

import folder.Folder;
import lombok.extern.log4j.Log4j;
import myutil.LogUtil;
import myutil.FolderUtil;

import javax.swing.*;


@Log4j
public class Processor {

    public static String CHANGES_LOG_FILE_NAME = "Folder-Synchronizer";


    public void start(String selectedFolder1, String selectedFolder2, JLabel statusLabel, Runnable unblockButtons) {
        try {
            statusLabel.setText("сканируем папки");
            Folder folder1 = Folder.scan(selectedFolder1);
            Folder folder2 = Folder.scan(selectedFolder2);

            boolean hasChanges = false;

            if (!folder1.getDeletedFiles().isEmpty() || !folder2.getDeletedFiles().isEmpty()) {
                FolderUtil.delete(folder1, folder2, statusLabel);
                FolderUtil.delete(folder2, folder1, statusLabel);
                hasChanges = true;
            }

            if (!folder1.getNewFiles().isEmpty() || !folder2.getNewFiles().isEmpty()) {
                FolderUtil.copyOrCreateNewFiles(folder1, folder2, statusLabel);
                FolderUtil.copyOrCreateNewFiles(folder2, folder1, statusLabel );
                hasChanges = true;
            }

            if (hasChanges) {
                statusLabel.setText("записываем изменения");
                LogUtil.saveFolderStateToLogFile(folder1);
                LogUtil.saveFolderStateToLogFile(folder2);
            }

            statusLabel.setText(hasChanges ? "завершено" : "нечего обновлять");
            unblockButtons.run();
        } catch (Exception e) {
            statusLabel.setText("Ошибка, смотри лог файл");
            log.error(e);
        }
    }


}
