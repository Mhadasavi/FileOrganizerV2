package com.uc;

import com.inventech.FileOrganizeMechanism;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class RadioButtonsDemo {
    private static JLabel label1, label2;
    private static JRadioButton radioButton1, radioButton2, radioButton3;
    private static JButton button1, button2, submitButton;
    private static JFileChooser chooser;
    private File fromPath;
    private File toPath;
    private boolean ackFlag = true;
    private boolean destinationFlag = true;
    private boolean startFlag = true;
    private static boolean isMonthRequire = false;



    public static void main(String[] args) {
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

        // Add labels to the content pane
        label1 = new JLabel("Select an option:");
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(label1);
        labelPanel.setBackground(Color.ORANGE);
        mainContentPane.add(labelPanel);

        label2 = new JLabel("");
        mainContentPane.add(label2);

        // Add radio buttons to the content pane
        radioButton1 = new JRadioButton("By Year");
        radioButton2 = new JRadioButton("By Month & Year");
        radioButton3 = new JRadioButton("By type");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        buttonGroup.add(radioButton3);

        JPanel radioButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioButtonsPanel.add(radioButton1);
        radioButtonsPanel.add(radioButton2);
        radioButtonsPanel.add(radioButton3);
        radioButtonsPanel.setBackground(Color.ORANGE);
        mainContentPane.add(radioButtonsPanel);

        // Add buttons to the content pane
        button1 = new JButton("Browse");
        button1.setVisible(false);
        button2 = new JButton("Destination");
        button2.setVisible(false);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.add(button1);
        buttonsPanel.add(button2);
        buttonsPanel.setBackground(Color.ORANGE);
        mainContentPane.add(buttonsPanel);

        // Add item listener to radio buttons
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (radioButton1.isSelected() || radioButton2.isSelected()) {
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button2.setEnabled(false);
                        if(radioButton2.isSelected()) {
                            isMonthRequire=true;
                        }
                    } else {
                        button2.setVisible(false);
                    }
//                    label2.setText("You selected " + ((JRadioButton) e.getItem()).getText());
                }
            }
        };
        radioButton1.addItemListener(itemListener);
        radioButton2.addItemListener(itemListener);
        radioButton3.addItemListener(itemListener);

        // Add submit button to the content pane
        submitButton = new JButton("Organize");
        JPanel submitButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitButtonPanel.add(submitButton);
        submitButtonPanel.setBackground(Color.ORANGE);
        mainContentPane.add(submitButtonPanel);
        String data[][] = {{"001","vinod","Bihar","India","Biology","65","First"}};

                String col[] = {"Roll","Name","State","country","Math","Marks","Grade"};
        JTable table = new JTable(data,col);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);
        JScrollPane pane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mainContentPane.add((pane));

        mainContentPane.setLayout(new BoxLayout(mainContentPane, BoxLayout.Y_AXIS));
        mainWindow.getContentPane().setBackground(Color.BLUE);
        // Set the size and location of the main window
        mainWindow.setSize(400, 200);
        mainWindow.setLocationRelativeTo(null);

        // Show the main window
        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new RadioButtonsDemo().addListeners();
    }

    private void addListeners() {
        button1.addActionListener(actionListener -> {
            chooser = new JFileChooser();
            chooser.setDialogTitle("Select source folder");
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fromPath = new File(chooser.getSelectedFile().getPath());
                ackFlag = false;
                destinationFlag = true;
                button2.setEnabled(true);
                System.err.println(fromPath);
            }
        });

        button2.addActionListener(actionListener -> {
            chooser.setDialogTitle("select destination folder");
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                toPath = new File(chooser.getSelectedFile().getPath());
                System.err.println(toPath);
                startFlag = true;
            }
        });

        submitButton.addActionListener(actionListener -> {
            try {
                FOMechanism.startFileProcessing(getFromPath(), getToPath(), isMonthRequire);
                destinationFlag = false;
                startFlag = false;
                ackFlag = true;
//                saveFolderButton.setVisible(destinationFlag);
//                fileOrganizerButton.setVisible(startFlag);
//                ack.setText("<html><span style='color: blue;'>File Organized Successfully!</span><p> Source Path: "+fromPath+"<br> Destination Path: "+toPath+"</html>");
//                ack.setVisible(ackFlag);
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

