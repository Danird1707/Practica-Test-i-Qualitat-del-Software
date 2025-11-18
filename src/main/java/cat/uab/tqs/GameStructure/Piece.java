package cat.uab.tqs.GameStructure;

import java.awt.Color;
import java.awt.Point;

public class Piece {
    // Forma de la pieza
    private Point[] shape;
    // Color de la pieza
    private final Color color;
    // Posición de la pieza
    private Point position;
    // Tablero en el que se encuentra la pieza
    private final Board board;

    /**
     * Constructor de la clase Piece.
     * @param shape La forma de la pieza.
     * @param color El color de la pieza.
     * @param board El tablero en el que se encuentra la pieza.
     */
    public Piece(Point[] shape, Color color, Board board) {
        this.shape = shape;
        this.color = color;
        this.board = board;
        this.position = new Point(Board.BOARD_WIDTH / 2 - 1, 0);
    }

    /**
     * Devuelve la forma de la pieza.
     * @return La forma de la pieza.
     */
    public Point[] getShape() {
        return shape;
    }

    /**
     * Devuelve el color de la pieza.
     * @return El color de la pieza.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Devuelve la posición de la pieza.
     * @return La posición de la pieza.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Mueve la pieza hacia abajo.
     */
    public void moveDown() {
        if (board.isValidPosition(shape, new Point(position.x, position.y + 1))) {
            position.y++;
        }
    }

    /**
     * Mueve la pieza hacia la izquierda.
     */
    public void moveLeft() {
        if (board.isValidPosition(shape, new Point(position.x - 1, position.y))) {
            position.x--;
        }
    }

    /**
     * Mueve la pieza hacia la derecha.
     */
    public void moveRight() {
        if (board.isValidPosition(shape, new Point(position.x + 1, position.y))) {
            position.x++;
        }
    }

    /**
     * Rota la pieza.
     */
    public void rotate() {
        Point[] newShape = new Point[shape.length];
        // Asumimos que el punto de pivote es el segundo punto de la forma (índice 1)
        // Esto es una simplificación, en un Tetris real se manejaría de forma más robusta
        Point pivot = shape[1];

        for (int i = 0; i < shape.length; i++) {
            // Trasladar el punto para que el pivote sea el origen
            int translatedX = shape[i].x - pivot.x;
            int translatedY = shape[i].y - pivot.y;

            // Rotar 90 grados en sentido horario (x' = -y, y' = x)
            newShape[i] = new Point(-translatedY, translatedX);

            // Trasladar el punto de vuelta
            newShape[i].x += pivot.x;
            newShape[i].y += pivot.y;
        }

        // Comprobar si la nueva forma es válida en el tablero
        if (board.isValidPosition(newShape, position)) {
            this.shape = newShape;
        }
    }

    /**
     * Comprueba si la pieza puede moverse hacia abajo.
     * @return true si la pieza puede moverse hacia abajo, false en caso contrario.
     */
    public boolean canMoveDown() {
        return board.isValidPosition(shape, new Point(position.x, position.y + 1));
    }
}
