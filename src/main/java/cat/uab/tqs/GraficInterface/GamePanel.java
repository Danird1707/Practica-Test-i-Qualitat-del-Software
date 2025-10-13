package cat.uab.tqs.GraficInterface;

import cat.uab.tqs.GameStructure.Board;
import cat.uab.tqs.GameStructure.Piece;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class GamePanel extends JPanel {
    // Tamaño de cada bloque del Tetris
    private static final int BLOCK_SIZE = 30;
    // Tablero del juego
    private final Board board;
    // Pieza actual
    private Piece currentPiece;

    /**
     * Constructor de la clase GamePanel.
     * @param board El tablero del juego.
     */
    public GamePanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(Board.BOARD_WIDTH * BLOCK_SIZE, Board.BOARD_HEIGHT * BLOCK_SIZE));
        setBackground(Color.BLACK);
    }

    /**
     * Establece la pieza actual.
     * @param currentPiece La pieza actual.
     */
    public void setCurrentPiece(Piece currentPiece) {
        this.currentPiece = currentPiece;
    }

    /**
     * Dibuja los componentes del juego.
     * @param g El objeto Graphics.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        if (currentPiece != null) {
            drawPiece(g, currentPiece);
        }
        drawGridLines(g);
    }

    /**
     * Dibuja el tablero.
     * @param g El objeto Graphics.
     */
    private void drawBoard(Graphics g) {
        Color[][] grid = board.getGrid();
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                if (grid[i][j] != null) {
                    g.setColor(grid[i][j]);
                    g.fillRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    /**
     * Dibuja una pieza.
     * @param g El objeto Graphics.
     * @param piece La pieza a dibujar.
     */
    private void drawPiece(Graphics g, Piece piece) {
        g.setColor(piece.getColor());
        Point[] shape = piece.getShape();
        Point position = piece.getPosition();
        for (Point p : shape) {
            int x = (position.x + p.x) * BLOCK_SIZE;
            int y = (position.y + p.y) * BLOCK_SIZE;
            g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    /**
     * Dibuja las líneas de la rejilla.
     * @param g El objeto Graphics.
     */
    private void drawGridLines(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= Board.BOARD_HEIGHT; i++) {
            g.drawLine(0, i * BLOCK_SIZE, Board.BOARD_WIDTH * BLOCK_SIZE, i * BLOCK_SIZE);
        }
        for (int j = 0; j <= Board.BOARD_WIDTH; j++) {
            g.drawLine(j * BLOCK_SIZE, 0, j * BLOCK_SIZE, Board.BOARD_HEIGHT * BLOCK_SIZE);
        }
    }
}
