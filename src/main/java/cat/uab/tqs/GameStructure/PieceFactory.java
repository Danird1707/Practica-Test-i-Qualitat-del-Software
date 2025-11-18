package cat.uab.tqs.GameStructure;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class PieceFactory {
    // Tablero en el que se crearán las piezas
    private final Board board;
    // Generador de números aleatorios
    private final Random random;

    // Formas de las piezas
    private final Point[][] pieceShapes = {
            // I-shape
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
            // J-shape
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
            // L-shape
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
            // O-shape
            { new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
            // S-shape
            { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
            // T-shape
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
            // Z-shape
            { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) }
    };

    // Colores de las piezas
    private final Color[] pieceColors = {
            Color.CYAN, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED
    };

    /**
     * Constructor de la clase PieceFactory.
     * @param board El tablero en el que se crearán las piezas.
     */
    public PieceFactory(Board board) {
        this(board, new Random());
    }

    /**
     * Constructor para testing, permite inyectar un generador de números aleatorios.
     * @param board El tablero.
     * @param random El generador de números aleatorios.
     */
    public PieceFactory(Board board, Random random) {
        this.board = board;
        this.random = random;
    }

    /**
     * Devuelve una nueva pieza aleatoria.
     * @return Una nueva pieza aleatoria.
     */
    public Piece getNewPiece() {
        int randomIndex = random.nextInt(pieceShapes.length);
        Point[] shape = new Point[4];
        for(int i = 0; i < 4; i++){
            shape[i] = new Point(pieceShapes[randomIndex][i]);
        }
        Color color = pieceColors[randomIndex];
        return new Piece(shape, color, board);
    }
}

