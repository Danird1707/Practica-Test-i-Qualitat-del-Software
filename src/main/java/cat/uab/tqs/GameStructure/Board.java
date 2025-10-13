package cat.uab.tqs.GameStructure;

import java.awt.Color;
import java.awt.Point;

public class Board {
    // Ancho del tablero
    public static final int BOARD_WIDTH = 10;
    // Alto del tablero
    public static final int BOARD_HEIGHT = 20;

    // Rejilla del tablero, almacena el color de cada celda
    private final Color[][] grid;

    /**
     * Constructor de la clase Board.
     * Inicializa la rejilla del tablero a null (vacío).
     */
    public Board() {
        grid = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                grid[i][j] = null; // null representa una celda vacía
            }
        }
    }

    /**
     * Devuelve la rejilla del tablero.
     * @return La rejilla del tablero.
     */
    public Color[][] getGrid() {
        return grid;
    }

    /**
     * Comprueba si una posición es válida para una pieza.
     * @param pieceShape La forma de la pieza.
     * @param position La posición de la pieza.
     * @return true si la posición es válida, false en caso contrario.
     */
    public boolean isValidPosition(Point[] pieceShape, Point position) {
        for (Point p : pieceShape) {
            int x = position.x + p.x;
            int y = position.y + p.y;

            // Comprueba si la pieza está fuera de los límites del tablero
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }

            // Comprueba si la celda ya está ocupada
            if (grid[y][x] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Coloca una pieza en el tablero.
     * @param pieceShape La forma de la pieza.
     * @param position La posición de la pieza.
     * @param color El color de la pieza.
     */
    public void placePiece(Point[] pieceShape, Point position, Color color) {
        for (Point p : pieceShape) {
            int x = position.x + p.x;
            int y = position.y + p.y;
            if (y >= 0) {
                grid[y][x] = color;
            }
        }
    }

    /**
     * Elimina las líneas completas del tablero.
     * @return El número de líneas eliminadas.
     */
    public int clearLines() {
        int linesCleared = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (grid[i][j] == null) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                linesCleared++;
                // Mueve todas las líneas superiores hacia abajo
                for (int k = i; k > 0; k--) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        grid[k][j] = grid[k - 1][j];
                    }
                }
                // Limpia la línea superior
                for (int j = 0; j < BOARD_WIDTH; j++) {
                    grid[0][j] = null;
                }
                i++; // Vuelve a comprobar la línea actual
            }
        }
        return linesCleared;
    }
}
