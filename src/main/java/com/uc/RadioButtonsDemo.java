package com.uc;

import com.inventech.FileOrganizeMechanism;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;

import static com.uc.FOMechanism.getLogs;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadioButtonsDemo {
    private static JLabel label1, label2;
    private static JRadioButton radioButton1, radioButton2, radioButton3;
    private static JButton button1, button2, submitButton;
    private static JFileChooser chooser;
    private File fromPath;
    private File toPath;
    private static boolean isTypeRequire = false;
    private static boolean isYearRequire = false;
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
        button1.setBackground(Color.ORANGE);
        button2 = new JButton("Destination");
        button2.setVisible(false);
        button2.setBackground(Color.ORANGE);

        submitButton = new JButton("Organize");
        submitButton.setBackground(Color.ORANGE);
        // Create an empty label with preferred width for the left space
        JLabel leftSpace = new JLabel();
        leftSpace.setPreferredSize(new Dimension(10, 50));
        // Create an empty label with preferred width for the right space
        JLabel rightSpace = new JLabel();
        rightSpace.setPreferredSize(new Dimension(10, 50));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(leftSpace);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(button1);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0))); // add some spacing
        buttonsPanel.add(button2);
        buttonsPanel.add(Box.createHorizontalGlue()); // align submitButton to the right
        buttonsPanel.add(submitButton);
        buttonsPanel.setBackground(Color.ORANGE);
        buttonsPanel.add(rightSpace);
        mainContentPane.add(buttonsPanel);


        // Add item listener to radio buttons
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (radioButton1.isSelected()) {
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button2.setEnabled(false);
                        isYearRequire = true;
                        isTypeRequire = false;
                        isMonthRequire = false;
                    }
                    if (radioButton2.isSelected()) {
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button2.setEnabled(false);
                        isYearRequire = false;
                        isTypeRequire = false;
                        isMonthRequire = true;
                    }
                    if (radioButton3.isSelected()) {
                        button1.setVisible(true);
                        button2.setVisible(true);
                        button2.setEnabled(false);
                        isYearRequire = false;
                        isTypeRequire = true;
                        isMonthRequire = false;
                    }
//                    label2.setText("You selected " + ((JRadioButton) e.getItem()).getText());
                }
            }
        };
        radioButton1.addItemListener(itemListener);
        radioButton2.addItemListener(itemListener);
        radioButton3.addItemListener(itemListener);
        radioButton1.setBackground(Color.ORANGE);
        radioButton2.setBackground(Color.ORANGE);
        radioButton3.setBackground(Color.ORANGE);


        String[] col = {"Path", "Total Files", "Size", "Time taken", "Date"};
        String[][] data = getLogs(col);
        TableModel model = new DefaultTableModel(data, col);
        JTable table = new JTable(model);

        Color color = Color.decode("#006699");
        MatteBorder border = new MatteBorder(0, 0, 1, 0, color);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.LIGHT_GRAY);

        JTableHeader header = table.getTableHeader();
        header.setBorder(border);
        JScrollPane pane = new JScrollPane(table);
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSize(400, 400);
        pane.setBorder(new MatteBorder(1, 0, 1, 0, color));
        mainContentPane.setBorder(new MatteBorder(0, 0, 2, 0, color));
        mainContentPane.add((pane));

        mainContentPane.setLayout(new BoxLayout(mainContentPane, BoxLayout.Y_AXIS));
        mainWindow.getContentPane().setBackground(Color.BLUE);
        mainWindow.setBackground(Color.CYAN);
        // Set the size and location of the main window
        mainWindow.setSize(600, 300);
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
                //ackFlag = false;
//                destinationFlag = true;
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
                FOMechanism.startFileProcessing(getFromPath(), getToPath(), Arrays.asList(isYearRequire, isMonthRequire, isTypeRequire));
                startFlag = false;
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

