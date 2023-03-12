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
import java.awt.*;

public class RadioButtonsDemo {
    private static JLabel label1, label2;
    private static JRadioButton radioButton1, radioButton2, radioButton3;
    private static JButton button1, button2, submitButton;

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

        mainContentPane.setLayout(new BoxLayout(mainContentPane, BoxLayout.Y_AXIS));
        mainWindow.getContentPane().setBackground(Color.BLUE);
        // Set the size and location of the main window
        mainWindow.setSize(400, 200);
        mainWindow.setLocationRelativeTo(null);

        // Show the main window
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

