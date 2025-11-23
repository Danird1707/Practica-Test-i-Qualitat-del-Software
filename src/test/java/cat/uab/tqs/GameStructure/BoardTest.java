package cat.uab.tqs.GameStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.awt.Color;
import java.awt.Point;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private final Point[] shapeI = { new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0) }; // Pieza I

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testBoardInitialization() {
        Color[][] grid = board.getGrid();
        assertNotNull(grid);
        assertEquals(Board.BOARD_HEIGHT, grid.length);
        assertEquals(Board.BOARD_WIDTH, grid[0].length);
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                assertNull(grid[i][j], "All cells should be initialized to null");
            }
        }
    }

    // --- Tests para isValidPosition (Data-Driven) ---

    @ParameterizedTest
    @CsvSource({
            "0, 0, true",      // Posición válida en la esquina superior izquierda
            "-1, 0, false",     // Fuera por la izquierda
            "7, 0, false",      // Fuera por la derecha (shapeI tiene 4 de ancho)
            "0, 20, false",     // Fuera por abajo
            "0, -1, true"       // Válido si está parcialmente arriba
    })
    void testIsValidPosition_DataDriven(int x, int y, boolean expected) {
        assertEquals(expected, board.isValidPosition(shapeI, new Point(x, y)));
    }

    @Test
    void testIsValidPosition_Collision() {
        board.getGrid()[1][1] = Color.RED;
        Point[] singlePoint = {new Point(0,0)};
        assertFalse(board.isValidPosition(singlePoint, new Point(1, 1)));
    }

    // --- Tests para placePiece ---

    @Test
    void testPlacePiece() {
        board.placePiece(shapeI, new Point(3, 5), Color.CYAN);
        assertEquals(Color.CYAN, board.getGrid()[5][3]);
        assertEquals(Color.CYAN, board.getGrid()[5][4]);
        assertEquals(Color.CYAN, board.getGrid()[5][5]);
        assertEquals(Color.CYAN, board.getGrid()[5][6]);
    }
    
    @Test
    void testPlacePiece_PartiallyAboveBoard() {
        Point[] shape = {new Point(0,-1), new Point(0,0)}; // Una parte está fuera (y=-1)
        // El método no debería lanzar una excepción
        assertDoesNotThrow(() -> {
            board.placePiece(shape, new Point(0,0), Color.GREEN);
        });
        // Y la parte visible debería estar colocada
        assertEquals(Color.GREEN, board.getGrid()[0][0]);
    }


    // --- Tests para clearLines ---

    @Test
    void testClearLines_NoLines() {
        board.getGrid()[Board.BOARD_HEIGHT - 1][0] = Color.RED;
        assertEquals(0, board.clearLines());
    }

    @Test
    void testClearLines_OneLine() {
        // Llenar la última fila
        for (int j = 0; j < Board.BOARD_WIDTH; j++) {
            board.getGrid()[Board.BOARD_HEIGHT - 1][j] = Color.BLUE;
        }
        board.getGrid()[Board.BOARD_HEIGHT - 2][5] = Color.RED; // Añadir un bloque en la fila de arriba

        assertEquals(1, board.clearLines());
        assertNull(board.getGrid()[Board.BOARD_HEIGHT - 1][0]); // La última fila debería estar vacía
        assertEquals(Color.RED, board.getGrid()[Board.BOARD_HEIGHT - 1][5]); // El bloque de arriba debería haber bajado
    }

    @Test
    void testClearLines_MultipleLines() {
        // Llenar las dos últimas filas
        for (int j = 0; j < Board.BOARD_WIDTH; j++) {
            board.getGrid()[Board.BOARD_HEIGHT - 1][j] = Color.BLUE;
            board.getGrid()[Board.BOARD_HEIGHT - 2][j] = Color.GREEN;
        }
        board.getGrid()[0][0] = Color.YELLOW; // Un bloque en la primera fila

        assertEquals(2, board.clearLines());
        assertNull(board.getGrid()[Board.BOARD_HEIGHT - 1][0]);
        assertNull(board.getGrid()[Board.BOARD_HEIGHT - 2][0]);
        assertEquals(Color.YELLOW, board.getGrid()[2][0]); // El bloque de arriba debería haber bajado dos posiciones
    }
    
    @Test
    void testClearLines_FullBoard() {
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                board.getGrid()[i][j] = Color.WHITE;
            }
        }
        assertEquals(Board.BOARD_HEIGHT, board.clearLines());
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                assertNull(board.getGrid()[i][j]);
            }
        }
    }
    
    @Test
    void testClearLines_LineInTheMiddle() {
        int middleRow = Board.BOARD_HEIGHT / 2;
        for (int j = 0; j < Board.BOARD_WIDTH; j++) {
            board.getGrid()[middleRow][j] = Color.ORANGE;
        }
        board.getGrid()[0][0] = Color.RED;

        assertEquals(1, board.clearLines());
        assertNull(board.getGrid()[middleRow][0]);
        assertEquals(Color.RED, board.getGrid()[1][0]);
    }
}
