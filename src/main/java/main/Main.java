package main;

import folder.MyFolder;
import myutil.MyFilesUtil;
import java.io.*;


public class Main {

    private String FIRST_PATH = "H:\\";
    private String SECOND_PATH = "E:\\флэшка";

    private void start() throws IOException {

        MyFolder folder1 = new MyFolder(FIRST_PATH, MyFilesUtil.getPathsByFolderName(FIRST_PATH));
        MyFolder folder2 = new MyFolder(SECOND_PATH, MyFilesUtil.getPathsByFolderName(SECOND_PATH));

        MyFilesUtil.copyOrCreateNewFiles(folder1, folder2);
        MyFilesUtil.copyOrCreateNewFiles(folder2, folder1);
    }

    public static void main(String[] args) throws IOException {
        new Main().start();
    }


}
