package com.unl.laberinto.view;

import com.unl.laberinto.model.Labyrinth;

import javax.swing.*;
import java.awt.*;

public class LabyrinthPanel extends JPanel {
    private Labyrinth labyrinth;

    public LabyrinthPanel() {
        setPreferredSize(new Dimension(600, 600));
    }

    public void setLabyrinth(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (labyrinth == null) return;

        char[][] maze = labyrinth.getMaze();
        int[][] distances = labyrinth.getDistances();
        int rows = labyrinth.getRows();
        int cols = labyrinth.getCols();

        int w = getWidth();
        int h = getHeight();

        int cellWidth = w / cols;
        int cellHeight = h / rows;

        g.setFont(new Font("Arial", Font.BOLD, Math.min(cellWidth, cellHeight) / 2));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char val = maze[r][c];
                switch (val) {
                    case '0': // muro
                        g.setColor(Color.BLACK);
                        g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                        break;
                    case '1': // camino
                        g.setColor(Color.WHITE);
                        g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                        break;
                    case 'S': // inicio
                        g.setColor(Color.GREEN);
                        g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                        break;
                    case 'E': // fin
                        g.setColor(Color.RED);
                        g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                        break;
                    case '*': // camino resuelto
                        g.setColor(Color.YELLOW);
                        g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                        break;
                    default:
                        g.setColor(Color.WHITE);
                        g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                }
                if (val == '0') {
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                }

                // Mostrar distancia en camino resuelto
                if (val == '*' && distances != null && distances[r][c] >= 0) {
                    g.setColor(Color.BLACK);
                    String distStr = Integer.toString(distances[r][c]);
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(distStr);
                    int textHeight = fm.getAscent();
                    int x = c * cellWidth + (cellWidth - textWidth) / 2;
                    int y = r * cellHeight + (cellHeight + textHeight) / 2 - 2;
                    g.drawString(distStr, x, y);
                }
            }
        }
    }
}
