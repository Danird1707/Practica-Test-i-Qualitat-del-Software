package cat.uab.tqs.GameStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Color;
import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PieceTest {

    @Mock
    private Board board;

    private Piece piece;
    private final Point[] shapeL = { new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(1, 2) };

    @BeforeEach
    void setUp() {
        piece = new Piece(shapeL.clone(), Color.ORANGE, board);
    }

    @Test
    void testPieceInitialization() {
        assertArrayEquals(shapeL, piece.getShape());
        assertEquals(Color.ORANGE, piece.getColor());
        assertEquals(new Point(Board.BOARD_WIDTH / 2 - 1, 0), piece.getPosition());
    }

    // --- Tests para movimientos (Data-Driven) ---

    @ParameterizedTest
    @CsvSource({
            "DOWN,  true,  0,  1",
            "DOWN,  false, 0,  0",
            "LEFT,  true,  -1, 0",
            "LEFT,  false, 0,  0",
            "RIGHT, true,  1,  0",
            "RIGHT, false, 0,  0"
    })
    void testPieceMovement_DataDriven(String direction, boolean isValid, int expectedDx, int expectedDy) {
        // Given
        when(board.isValidPosition(any(), any(Point.class))).thenReturn(isValid);
        Point initialPosition = new Point(piece.getPosition());

        // When
        switch (direction) {
            case "DOWN":
                piece.moveDown();
                break;
            case "LEFT":
                piece.moveLeft();
                break;
            case "RIGHT":
                piece.moveRight();
                break;
        }

        // Then
        assertEquals(initialPosition.x + expectedDx, piece.getPosition().x);
        assertEquals(initialPosition.y + expectedDy, piece.getPosition().y);
    }

    // --- Tests para rotación ---

    @Test
    void testRotate_Valid() {
        when(board.isValidPosition(any(), any(Point.class))).thenReturn(true);

        Point[] before = new Point[piece.getShape().length];
        for (int i = 0; i < piece.getShape().length; i++) {
            before[i] = new Point(piece.getShape()[i]);
        }

        piece.rotate();

        boolean changed = false;
        for (int i = 0; i < before.length; i++) {
            if (!before[i].equals(piece.getShape()[i])) {
                changed = true;
                break;
            }
        }

        assertTrue(changed, "The piece shape should change after a valid rotation");
    }

    @Test
    void testRotate_Invalid() {
        when(board.isValidPosition(any(), any(Point.class))).thenReturn(false);
        Point[] initialShape = piece.getShape();
        piece.rotate();
        assertArrayEquals(initialShape, piece.getShape());
    }

    // --- Tests para canMoveDown ---

    @Test
    void testCanMoveDown_True() {
        when(board.isValidPosition(piece.getShape(), new Point(piece.getPosition().x, piece.getPosition().y + 1))).thenReturn(true);
        assertTrue(piece.canMoveDown());
    }

    @Test
    void testCanMoveDown_False() {
        when(board.isValidPosition(piece.getShape(), new Point(piece.getPosition().x, piece.getPosition().y + 1))).thenReturn(false);
        assertFalse(piece.canMoveDown());
    }
}
