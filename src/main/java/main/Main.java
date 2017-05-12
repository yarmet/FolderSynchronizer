package main;

import folder.MyFolder;
import myutil.LogUtil;
import myutil.MyFilesUtil;

import java.io.*;


public class Main {

    public static String LOG_FILE = "Folder-Synchronizer";

    private String FIRST_PATH = "folder1";
    private String SECOND_PATH = "folder2";

    private void start() throws IOException {

        MyFolder folder1 = MyFolder.scan(FIRST_PATH);
        MyFolder folder2 = MyFolder.scan(SECOND_PATH);

        MyFilesUtil.delete(folder1, folder2);
        MyFilesUtil.delete(folder2, folder1);

        MyFilesUtil.copyOrCreateNewFiles(folder1, folder2);
        MyFilesUtil.copyOrCreateNewFiles(folder2, folder1);

        LogUtil.saveToLogFile(folder1);
        LogUtil.saveToLogFile(folder2);

    }

    public static void main(String[] args) throws IOException {
        new Main().start();
    }

}
