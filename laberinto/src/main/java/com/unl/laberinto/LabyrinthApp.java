package com.unl.laberinto;

import com.unl.laberinto.view.LabyrinthFrame;

import javax.swing.SwingUtilities;

public class LabyrinthApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LabyrinthFrame frame = new LabyrinthFrame();
            frame.setVisible(true);
        });
    }
}
