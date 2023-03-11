package com.uc;

import javax.swing.*;

import static com.uc.FOUi.createAndShowGUI;

public class FOMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
