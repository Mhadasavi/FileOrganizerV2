package com.inventech;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileOrganizer2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    private static void createAndShowGUI() {
        // Set the Nimbus look and feel
        try {
//            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the main window
        JFrame mainWindow = new JFrame("File Organizer");

        // Create the content pane for the main window
        JPanel mainContentPane = new JPanel();
        mainWindow.setContentPane(mainContentPane);

        // Add a label to the content pane
        JLabel label = new JLabel("Click the button below to organize files in a directory.");
        mainContentPane.add(label);

        // Add a button to the content pane
        JButton button = new JButton("Organize Files");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Navigate to the file organizer window
                showFileOrganizerWindow();
            }
        });
        mainContentPane.add(button);

        // Set the size and location of the main window
        mainWindow.setSize(300, 100);
        mainWindow.setLocationRelativeTo(null);

        // Show the main window
        mainWindow.setVisible(true);
    }


    private static void showFileOrganizerWindow() {
        // Create a file chooser dialog to select the directory to organize
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a directory to organize");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();

            // Organize the files in the directory
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    String type = getFileType(file.getName());
                    if (type != null) {
                        File typeDir = new File(dir.getPath() + File.separator + type);
                        typeDir.mkdir();
                        file.renameTo(new File(typeDir.getPath() + File.separator + file.getName()));
                    }
                }
            }

            System.out.println("Files organized successfully.");
        } else {
            System.out.println("No directory selected.");
        }
    }

    private static String getFileType(String fileName) {
        String type = null;
        int pos = fileName.lastIndexOf('.');
        if (pos > 0 && pos < fileName.length() - 1) {
            type = fileName.substring(pos + 1).toLowerCase();
        }
        return type;
    }
}
