package com.inventech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FileOrganizerUIPanel extends JPanel {

    private boolean startFlag = false;
    private boolean destinationFlag = false;
    private boolean ackFlag = true;
    private final JButton selectFolderButton;
    private final JButton saveFolderButton;
    private final JButton fileOrganizerButton;
    private JFileChooser chooser;
    private File fromPath;
    private File toPath;
    private final JFrame frame;
    private final Box inputContent;
    private final Box titleText;
    private final Box mainContent;
    private final JLabel ack;

    FileOrganizerUIPanel() {
        frame = new JFrame("File Organizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setPreferredSize(new Dimension(800, 375));
        frame.setMinimumSize(new Dimension(600, 450));

        titleText = Box.createHorizontalBox();
        JLabel title = new JLabel("<html><span style='color: teal;'>&nbsp;&nbsp;File Organizer&nbsp;&nbsp;</span><hr /></html>");
        title.setFont(title.getFont().deriveFont(64.0f));
        JLabel version = new JLabel("<html>Created by Wizards<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version 1.0</html>");
        JLabel slogan = new JLabel("<html>Sorting files Based on Year and Month</html>");
        titleText.add(version);
        titleText.add(title);
        titleText.add(slogan);
        titleText.setAlignmentX(frame.getWidth() / 2);

        selectFolderButton = new JButton("Browse");

        ack = new JLabel("<html><span style='color: blue;'>Select source folder to proceed &nbsp;</span></html>");
        ack.setFont(ack.getFont().deriveFont(14.0f));
        ack.setVisible(ackFlag);

        saveFolderButton = new JButton("Select destination");
        saveFolderButton.setVisible(destinationFlag);

        fileOrganizerButton = new JButton("Start File Organizing...");
        fileOrganizerButton.setVisible(startFlag);

        inputContent = Box.createHorizontalBox();
        inputContent.setBackground(Color.white);
        inputContent.add(ack);
        inputContent.add(selectFolderButton);
        inputContent.add(saveFolderButton);

        mainContent = Box.createHorizontalBox();
        mainContent.add(fileOrganizerButton);
    }

    public JFrame showPanel() {
        addListeners();
        frame.add(titleText);
        frame.add(inputContent);
        frame.add(mainContent);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    private void addListeners() {
        selectFolderButton.addActionListener(actionListener -> {
            chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogTitle("Select source folder");
            chooser.showSaveDialog(null);
            chooser.setAcceptAllFileFilterUsed(false);
            fromPath = new File(chooser.getSelectedFile().getAbsolutePath());
            if(fromPath!=null){
                ackFlag = false;
                destinationFlag = true;
                saveFolderButton.setVisible(destinationFlag);
                ack.setVisible(ackFlag);
            }
        });

        saveFolderButton.addActionListener(actionListener -> {
            chooser.setDialogTitle("select destination folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.showSaveDialog(null);
            chooser.setAcceptAllFileFilterUsed(false);
            toPath = new File(chooser.getSelectedFile().getAbsolutePath());
            if(toPath!=null){
                startFlag = true;
                fileOrganizerButton.setVisible(startFlag);
            }
        });

        fileOrganizerButton.addActionListener(actionListener -> {
            try {
                FileOrganizeMechanism.startFileProcessing(getFromPath(), getToPath());
                destinationFlag = false;
                startFlag = false;
                ackFlag = true;
                saveFolderButton.setVisible(destinationFlag);
                fileOrganizerButton.setVisible(startFlag);
                ack.setText("<html><span style='color: blue;'>File Organized Successfully!</span><p> Source Path: "+fromPath+"<br> Destination Path: "+toPath+"</html>");
                ack.setVisible(ackFlag);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }



    public File getFromPath() {
        return fromPath;
    }

    public File getToPath() {
        return toPath;
    }

}
