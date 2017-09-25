package main;

import folder.MyFolder;
import lombok.extern.log4j.Log4j;
import myutil.LogUtil;
import myutil.MyFilesUtil;

import javax.swing.*;


@Log4j
public class Main {

    public static String CHANGES_LOG_FILE_NAME = "Folder-Synchronizer";

    public void start(String selectedFolder1, String selectedFolder2, JLabel status) {
        try {
            status.setText("сканируем папки");
            MyFolder folder1 = MyFolder.scan(selectedFolder1);
            MyFolder folder2 = MyFolder.scan(selectedFolder2);

            status.setText("синхронизируем удаленные файлы");
            MyFilesUtil.delete(folder1, folder2);
            MyFilesUtil.delete(folder2, folder1);

            status.setText("синхронизируем новые/обновленные файлы");
            MyFilesUtil.copyOrCreateNewFiles(folder1, folder2);
            MyFilesUtil.copyOrCreateNewFiles(folder2, folder1);

            status.setText("записываем изменения");
            LogUtil.saveToLogFile(folder1);
            LogUtil.saveToLogFile(folder2);

            status.setText("завершено");
        } catch (Exception e) {
            status.setText("ошибка, смотри лог файл");
            log.error(e);
        }
    }


}
