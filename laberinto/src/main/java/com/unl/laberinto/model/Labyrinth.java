package com.unl.laberinto.model;

import java.util.Random;

public class Labyrinth {

    private final int rows;
    private final int cols;
    private final char[][] maze;
    private final Random random = new Random();

    private Point start;
    private Point end;

    private int[][] distances; // Para pesos en el camino

    public Labyrinth(int rows, int cols) {
        if (rows < 30) rows = 30;
        if (cols < 30) cols = 30;
        if (rows > 100) rows = 100;
        if (cols > 100) cols = 100;
        this.rows = rows;
        this.cols = cols;
        this.maze = new char[rows][cols];
        this.distances = new int[rows][cols];
        generateMaze();
    }

    public char[][] getMaze() {
        return maze;
    }

    public int[][] getDistances() {
        return distances;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public static class Point {
        public int r, c;
        public Point parent;

        public Point(int r, int c, Point parent) {
            this.r = r;
            this.c = c;
            this.parent = parent;
        }

        public Point opposite() {
            if (parent == null) return null;
            int dr = r - parent.r;
            int dc = c - parent.c;
            return new Point(r + dr, c + dc, this);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return this.r == p.r && this.c == p.c;
        }

        @Override
        public int hashCode() {
            return r * 31 + c;
        }
    }

    private void generateMaze() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                maze[i][j] = '0';
                distances[i][j] = -1;
            }

        int entradaCol = random.nextInt(cols - 2) + 1;
        int salidaCol = random.nextInt(cols - 2) + 1;

        maze[0][entradaCol] = 'S';
        maze[rows - 1][salidaCol] = 'E';

        start = new Point(0, entradaCol, null);
        end = new Point(rows - 1, salidaCol, null);

        for (int c = 0; c < cols; c++) {
            if (c != entradaCol) maze[0][c] = '0';
            if (c != salidaCol) maze[rows - 1][c] = '0';
        }
        for (int r = 0; r < rows; r++) {
            maze[r][0] = '0';
            maze[r][cols - 1] = '0';
        }

        com.unl.laberinto.model.LinkedList<Point> frontier = new com.unl.laberinto.model.LinkedList<>();
        addFrontier(start, frontier);

        while (!frontier.isEmpty()) {
            int idx = random.nextInt(frontier.getLength());
            Point current;
            try {
                current = frontier.get(idx);
                frontier.delete(idx);
            } catch (Exception e) {
                break;
            }

            Point opposite = current.opposite();

            if (opposite != null && isInBounds(opposite.r, opposite.c)
                    && maze[current.r][current.c] == '0'
                    && maze[opposite.r][opposite.c] == '0') {

                if (current.r == 0 && current.c != entradaCol) continue;
                if (current.r == rows - 1 && current.c != salidaCol) continue;
                if (current.c == 0 || current.c == cols - 1) continue;

                maze[current.r][current.c] = '1';
                maze[opposite.r][opposite.c] = '1';

                addFrontier(opposite, frontier);
            }
        }
    }

    private void addFrontier(Point p, com.unl.laberinto.model.LinkedList<Point> frontier) {
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++) {
            int nr = p.r + dr[i];
            int nc = p.c + dc[i];
            if (isInBounds(nr, nc) && maze[nr][nc] == '0') {
                frontier.add(new Point(nr, nc, p));
            }
        }
    }

    private boolean isInBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public void clearPath() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == '*') maze[i][j] = '1';
                distances[i][j] = -1;
            }
    }

    public void solveWithDijkstra() {
        Point[][] points = new Point[rows][cols];
        int INF = Integer.MAX_VALUE / 2;
        int[][] dist = new int[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                points[i][j] = new Point(i, j, null);
                dist[i][j] = INF;
                visited[i][j] = false;
                distances[i][j] = -1;
            }

        dist[start.r][start.c] = 0;
        distances[start.r][start.c] = 0;

        while (true) {
            int minDist = INF;
            Point current = null;
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    if (!visited[i][j] && maze[i][j] != '0' && dist[i][j] < minDist) {
                        minDist = dist[i][j];
                        current = points[i][j];
                    }
            if (current == null) break;
            if (current.equals(end)) break;

            visited[current.r][current.c] = true;

            int[] dr = {-1, 1, 0, 0};
            int[] dc = {0, 0, -1, 1};
            for (int k = 0; k < 4; k++) {
                int nr = current.r + dr[k];
                int nc = current.c + dc[k];
                if (isInBounds(nr, nc) && maze[nr][nc] != '0' && !visited[nr][nc]) {
                    int alt = dist[current.r][current.c] + 1;
                    if (alt < dist[nr][nc]) {
                        dist[nr][nc] = alt;
                        points[nr][nc].parent = current;
                        distances[nr][nc] = alt;
                    }
                }
            }
        }

        Point p = points[end.r][end.c];
        while (p != null && !p.equals(points[start.r][start.c])) {
            if (maze[p.r][p.c] != 'S' && maze[p.r][p.c] != 'E') {
                maze[p.r][p.c] = '*';
            }
            p = p.parent;
        }
    }

    public void solveWithFloyd() {
        com.unl.laberinto.model.LinkedList<Point> nodes = new com.unl.laberinto.model.LinkedList<>();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (maze[i][j] != '0')
                    nodes.add(new Point(i, j, null));

        int n = nodes.getLength();
        int INF = Integer.MAX_VALUE / 2;
        int[][] dist = new int[n][n];
        int[][] next = new int[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                dist[i][j] = (i == j) ? 0 : INF;
                next[i][j] = -1;
            }

        for (int i = 0; i < n; i++) {
            Point p = nodes.get(i);
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                Point q = nodes.get(j);
                if (areNeighbors(p, q)) {
                    dist[i][j] = 1;
                    next[i][j] = j;
                }
            }
        }

        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }

        int startIdx = indexOf(nodes, start);
        int endIdx = indexOf(nodes, end);

        if (startIdx == -1 || endIdx == -1 || next[startIdx][endIdx] == -1) {
            System.out.println("No hay camino entre inicio y fin.");
            return;
        }

        int u = startIdx;
        int step = 0;
        while (u != endIdx) {
            Point p = nodes.get(u);
            if (maze[p.r][p.c] != 'S' && maze[p.r][p.c] != 'E') {
                maze[p.r][p.c] = '*';
                distances[p.r][p.c] = step++;
            }
            u = next[u][endIdx];
        }
        Point pEnd = nodes.get(endIdx);
        if (maze[pEnd.r][pEnd.c] != 'E') {
            maze[pEnd.r][pEnd.c] = '*';
            distances[pEnd.r][pEnd.c] = step;
        }
    }

    private boolean areNeighbors(Point p, Point q) {
        int dr = Math.abs(p.r - q.r);
        int dc = Math.abs(p.c - q.c);
        return (dr == 1 && dc == 0) || (dr == 0 && dc == 1);
    }

    private int indexOf(com.unl.laberinto.model.LinkedList<Point> list, Point p) {
        for (int i = 0; i < list.getLength(); i++) {
            try {
                if (list.get(i).equals(p)) return i;
            } catch (Exception e) {
                // ignore
            }
        }
        return -1;
    }
}
