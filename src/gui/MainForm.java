package gui;

import processor.Processor;
import properties.RecentOpenFolderManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by ruslan on 12.05.2017.
 */
public class MainForm {

    private Processor processor = new Processor();
    private RecentOpenFolderManager propertiesManager = new RecentOpenFolderManager();

    private JPanel panel1;
    private JTextField folder2Field;
    private JTextField folder1Field;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JLabel statusLabel;


    public MainForm() {
        panel1.setBorder(new EmptyBorder(15, 15, 15, 15));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        propertiesManager.load();

        button1.addActionListener(e -> openFileDialog(fileChooser, RecentOpenFolderManager.FOLDER1, folder1Field));
        button2.addActionListener(e -> openFileDialog(fileChooser, RecentOpenFolderManager.FOLDER2, folder2Field));

        button3.addActionListener(this::startScanButtonClicked);
    }


    void startScanButtonClicked(ActionEvent actionEvent) {
        String folder1Path = folder1Field.getText();
        String folder2Path = folder2Field.getText();

        if (folder1Path.equals("") || folder2Path.equals("")) {
            statusLabel.setText("пути к папкам не должны быть пустыми");
            return;
        }
        if (folder1Path.equals(folder2Path)) {
            statusLabel.setText("нельзя синхронизировать папку саму с собой");
            return;
        }
        enableButtons(false);
        new Thread(() -> processor.start(folder1Path, folder2Path, statusLabel, () -> enableButtons(true))).start();
        propertiesManager.save();
    }


    private void enableButtons(boolean enabled) {
        button1.setEnabled(enabled);
        button2.setEnabled(enabled);
        button3.setEnabled(enabled);
    }


    public static void main(String[] args) {
        JFrame mainForm = new JFrame();
        mainForm.setContentPane(new MainForm().panel1);
        mainForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainForm.setResizable(false);
        mainForm.setPreferredSize(new Dimension(600, 200));
        mainForm.pack();
        mainForm.setLocationRelativeTo(null);
        mainForm.setVisible(true);
    }


    private void openFileDialog(JFileChooser fileChooser, String folderName, JTextField statusField) {
        String fold = propertiesManager.getFolderPath(folderName);
        fileChooser.setCurrentDirectory(new File(fold));
        int result = fileChooser.showOpenDialog(panel1);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            statusField.setText(selectedFile.getAbsolutePath());
            propertiesManager.changeFolder(folderName, selectedFile.getAbsolutePath());
        }
    }

}
