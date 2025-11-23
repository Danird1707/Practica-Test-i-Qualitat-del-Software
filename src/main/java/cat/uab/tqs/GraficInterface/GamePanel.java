package cat.uab.tqs.GraficInterface;

import cat.uab.tqs.GameControl.Game;
import cat.uab.tqs.GameControl.Inputs;
import cat.uab.tqs.GameStructure.Board;
import cat.uab.tqs.GameStructure.Piece;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que dibuja el tablero y la pieza actual.
 */
public class GamePanel extends JPanel {

    private static final int CELL_SIZE = 30; // Tamaño de cada bloque
    private final Game game;

    public GamePanel(Game game) {
        this.game = game;
        int width = Board.BOARD_WIDTH * CELL_SIZE;
        int height = Board.BOARD_HEIGHT * CELL_SIZE;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow(); // pide el foco
        addKeyListener(new Inputs(game)); // añade el listener de teclas
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawCurrentPiece(g);
    }

    /**
     * Dibuja las celdas fijas del tablero.
     */
    private void drawBoard(Graphics g) {
        Color[][] grid = game.getBoard().getGrid();
        for (int y = 0; y < Board.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Board.BOARD_WIDTH; x++) {
                Color color = grid[y][x];
                if (color != null) {
                    drawCell(g, x, y, color);
                }
            }
        }

        // Líneas de la cuadrícula (opcional)
        g.setColor(new Color(50, 50, 50));
        for (int x = 0; x <= Board.BOARD_WIDTH; x++) {
            g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, Board.BOARD_HEIGHT * CELL_SIZE);
        }
        for (int y = 0; y <= Board.BOARD_HEIGHT; y++) {
            g.drawLine(0, y * CELL_SIZE, Board.BOARD_WIDTH * CELL_SIZE, y * CELL_SIZE);
        }
    }

    /**
     * Dibuja la pieza actual que está cayendo.
     */
    private void drawCurrentPiece(Graphics g) {
        Piece piece = game.getCurrentPiece();
        if (piece == null) return;

        Point[] shape = piece.getShape();
        Point position = piece.getPosition();
        Color color = piece.getColor();

        for (Point p : shape) {
            int x = position.x + p.x;
            int y = position.y + p.y;
            drawCell(g, x, y, color);
        }
    }

    /**
     * Dibuja una celda individual.
     */
    private void drawCell(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
}