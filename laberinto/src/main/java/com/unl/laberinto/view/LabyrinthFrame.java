package com.unl.laberinto.view;

import com.unl.laberinto.model.Labyrinth;

import javax.swing.*;
import java.awt.*;

public class LabyrinthFrame extends JFrame {
    private final JTextField rowsField;
    private final JTextField colsField;
    private final JButton generateButton;
    private final JButton solveDijkstraButton;
    private final JButton solveFloydButton;
    private final JButton clearPathButton;
    private final LabyrinthPanel labyrinthPanel;

    private Labyrinth labyrinth;

    public LabyrinthFrame() {
        setTitle("Laberinto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        controlPanel.add(new JLabel("Filas (30-100):"));
        rowsField = new JTextField("30", 5);
        controlPanel.add(rowsField);

        controlPanel.add(new JLabel("Columnas (30-100):"));
        colsField = new JTextField("30", 5);
        controlPanel.add(colsField);

        generateButton = new JButton("Generar Laberinto");
        controlPanel.add(generateButton);

        solveDijkstraButton = new JButton("Resolver con Dijkstra");
        solveDijkstraButton.setEnabled(false);
        controlPanel.add(solveDijkstraButton);

        solveFloydButton = new JButton("Resolver con Floyd");
        solveFloydButton.setEnabled(false);
        controlPanel.add(solveFloydButton);

        clearPathButton = new JButton("Limpiar Camino");
        clearPathButton.setEnabled(false);
        controlPanel.add(clearPathButton);

        labyrinthPanel = new LabyrinthPanel();

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(labyrinthPanel), BorderLayout.CENTER);

        // Eventos
        generateButton.addActionListener(e -> {
            try {
                int rows = Integer.parseInt(rowsField.getText());
                int cols = Integer.parseInt(colsField.getText());
                if (rows < 30 || rows > 100 || cols < 30 || cols > 100) {
                    JOptionPane.showMessageDialog(LabyrinthFrame.this, "Dimensiones deben estar entre 30 y 100");
                    return;
                }
                labyrinth = new Labyrinth(rows, cols);
                labyrinthPanel.setLabyrinth(labyrinth);
                solveDijkstraButton.setEnabled(true);
                solveFloydButton.setEnabled(true);
                clearPathButton.setEnabled(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LabyrinthFrame.this, "Ingrese números válidos");
            }
        });

        solveDijkstraButton.addActionListener(e -> {
            if (labyrinth != null) {
                labyrinth.clearPath();
                labyrinth.solveWithDijkstra();
                labyrinthPanel.repaint();
                clearPathButton.setEnabled(true);
            }
        });

        solveFloydButton.addActionListener(e -> {
            if (labyrinth != null) {
                labyrinth.clearPath();
                labyrinth.solveWithFloyd();
                labyrinthPanel.repaint();
                clearPathButton.setEnabled(true);
            }
        });

        clearPathButton.addActionListener(e -> {
            if (labyrinth != null) {
                labyrinth.clearPath();
                labyrinthPanel.repaint();
                clearPathButton.setEnabled(false);
            }
        });
    }
}
