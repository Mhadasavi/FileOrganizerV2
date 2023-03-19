package com.uc;

import com.inventech.FileOrganizeMechanism;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import javax.swing.*;
import javax.swing.table.JTableHeader;
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

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.add(button1);
        buttonsPanel.add(button2);
        buttonsPanel.setBackground(Color.ORANGE);
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

        // Add submit button to the content pane
        submitButton = new JButton("Organize");
        submitButton.setBackground(Color.ORANGE);
        JPanel submitButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitButtonPanel.add(submitButton);
        submitButtonPanel.setBackground(Color.ORANGE);
        mainContentPane.add(submitButtonPanel);


        String[] col = {"Path", "Total Files", "Size", "Time taken", "Date"};
        String[][] data = getLogs(col);
        JTable table = new JTable(data, col);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);
        JScrollPane pane = new JScrollPane(table);
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSize(400, 400);
        mainContentPane.add((pane));

        mainContentPane.setLayout(new BoxLayout(mainContentPane, BoxLayout.Y_AXIS));
        mainWindow.getContentPane().setBackground(Color.BLUE);
        // Set the size and location of the main window
        mainWindow.setSize(600, 300);
        mainWindow.setLocationRelativeTo(null);

        // Show the main window
        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new RadioButtonsDemo().addListeners();
    }

    private static String[][] getLogs(String[] col) {
        // String[] col = {"Path", "Total Files", "Size", "Time taken", "Date"};
        String[][] data = new String[0][col.length]; // initialize the array with 0 rows
        //String regex = "Moved (\\d+) files from ([^\\s]+) to ([^\\s]+)(?:\\s)?Total file size: (\\d+) Kilobytes. Time taken: (\\d+) ms. Date: (.+)";
        String regex = "Moved (\\d+) files from ([^\\s]+) to ([^\\s]+(\\s+[^\\s]+)*).+Total file size: (\\d+) Kilobytes. Time taken: (\\d+) ms. Date: (.+)";

        try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "//output.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
//                String[] temp = line.split("\\s+");
//                String[] row = new String[col.length];
//                row[0] = temp[6];
//                row[1] = temp[1];
//                row[2] = temp[10]+" MB";
//                row[3] = temp[14]; //+ " " + temp[9];
//                row[4] = temp[17];
                if (line.matches(regex)) {
                    Matcher matcher = Pattern.compile(regex).matcher(line);
                    matcher.matches(); // this is necessary to populate the matcher object with the matched groups
                    String[] row = new String[col.length];
                    row[0] = matcher.group(3);
                    row[1] = matcher.group(1);
                    row[2] = matcher.group(4) + " Kilobytes";
                    row[3] = matcher.group(5) + " ms";
                    row[4] = matcher.group(6);
                    data = addRowToArray(data, row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//            // print the data array to verify that it was populated correctly
//            for (String[] row : data) {
//                for (String cell : row) {
//                    System.out.print(cell + " ");
//                }
//                System.out.println();
//            }
        return data;
    }

    private static String[][] addRowToArray(String[][] array, String[] row) {
        String[][] newArray = new String[array.length + 1][row.length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = row;
        return newArray;
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
//                destinationFlag = false;
                startFlag = false;
//                ackFlag = true;
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

