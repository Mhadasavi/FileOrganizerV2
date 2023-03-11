package com.uc;

import javax.swing.*;

import static com.inventech.FileOrganizer2.createAndShowGUI;


public class FOMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
