package gui;

import main.Main;
import properties.PropertiManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by ruslan on 12.05.2017.
 */
public class MainForm {

    private Main main = new Main();
    private PropertiManager propertiManager = new PropertiManager();

    private JPanel panel1;
    private JTextField textField2;
    private JTextField textField1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JLabel status;


    public MainForm() {
        panel1.setBorder(new EmptyBorder(15, 15, 15, 15));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        propertiManager.load();

        button1.addActionListener(e -> openFileDialog(fileChooser, PropertiManager.FOLDER1, textField1));
        button2.addActionListener(e -> openFileDialog(fileChooser, PropertiManager.FOLDER2, textField2));

        button3.addActionListener(e -> {
            if(textField1.getText().equals("")  || textField2.getText().equals("")) {
                status.setText("пути к папкам не должны быть пустыми");
                return;
            }

            new Thread(() -> main.start(textField1.getText(), textField2.getText(), status)).start();
            propertiManager.save();
        });

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


    private void openFileDialog(JFileChooser fileChooser, String folder, JTextField textField) {
        String fold = propertiManager.getFolderPath(folder);
        fileChooser.setCurrentDirectory(new File(fold));
        int result = fileChooser.showOpenDialog(panel1);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textField.setText(selectedFile.getAbsolutePath());
            propertiManager.changeFolder(folder, selectedFile.getAbsolutePath());
        }
    }

}
