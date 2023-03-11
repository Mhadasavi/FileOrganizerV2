package com.inventech;

import javax.swing.*;
import java.io.IOException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FileOrganizer {

    public static void main(String... args) throws IOException {
        try {
            // set look and feel to system dependent
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        FileOrganizerUIPanel panel = new FileOrganizerUIPanel();
        JFrame frame = panel.showPanel();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


}