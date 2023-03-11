package com.uc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FOUi {

    private boolean startFlag = false;
    private boolean destinationFlag = false;
    private boolean ackFlag = true;
    private static JRadioButton rbtn1;
    private static JRadioButton rbtn2;
    private static JRadioButton rbtn3;
    private static JLabel logsLabel;
    private static ButtonGroup buttonGroup;
//    private final JButton selectFolderButton;
//    private final JButton saveFolderButton;
    private static JButton fileOrganizerButton;
    private JFileChooser chooser;
    private File fromPath;
    private File toPath;
//    private final JFrame frame;
//    private final Box inputContent;
//    private final Box titleText;
//    private final Box mainContent;
//    private final JLabel ack;



    protected static void createAndShowGUI() {
        // Set the Nimbus look and feel
        try {
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
        JLabel label = new JLabel("Select any option to organize file : ");
        mainContentPane.add(label);


        rbtn1=new JRadioButton("By Year");
        rbtn2=new JRadioButton("By Month & Year");
        rbtn3=new JRadioButton("By File Type");

        buttonGroup=new ButtonGroup();
        buttonGroup.add(rbtn1);
        buttonGroup.add(rbtn2);
        buttonGroup.add(rbtn3);

        mainWindow.add(rbtn1);
        mainWindow.add(rbtn2);
        mainWindow.add(rbtn3);

        // Add a button to the content pane
        fileOrganizerButton = new JButton("Organize Files");
        fileOrganizerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Navigate to the file organizer window
                showFileOrganizerWindow();
            }
        });

        mainContentPane.add(fileOrganizerButton);

        logsLabel=new JLabel("Logs will display here...");
        mainContentPane.add(logsLabel);

        // Set the size and location of the main window
        mainWindow.setSize(300, 100);
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);

        // Show the main window
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
