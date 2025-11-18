package cat.uab.tqs.GameStructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PieceFactoryTest {

    @Mock
    private Board board; // ✅ solo mockeamos Board

    @Test
    void testGetNewPiece_CreatesIShape() {
        // Given
        Random predictableRandom = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0; // Fuerza la pieza I
            }
        };
        PieceFactory pieceFactory = new PieceFactory(board, predictableRandom);

        // When
        Piece newPiece = pieceFactory.getNewPiece();

        // Then
        assertNotNull(newPiece);
        assertEquals(Color.CYAN, newPiece.getColor());
        Point[] expectedShape = { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) };
        assertArrayEquals(expectedShape, newPiece.getShape());
    }

    @Test
    void testGetNewPiece_CreatesOShape() {
        // Given
        Random predictableRandom = new Random() {
            @Override
            public int nextInt(int bound) {
                return 3; // Fuerza la pieza O
            }
        };
        PieceFactory pieceFactory = new PieceFactory(board, predictableRandom);

        // When
        Piece newPiece = pieceFactory.getNewPiece();

        // Then
        assertNotNull(newPiece);
        assertEquals(Color.YELLOW, newPiece.getColor());
        Point[] expectedShape = {
                new Point(0, 0), new Point(1, 0),
                new Point(0, 1), new Point(1, 1)
        };
        assertArrayEquals(expectedShape, newPiece.getShape());
    }

    @Test
    void testGetNewPiece_NotNull() {
        // Given
        PieceFactory pieceFactory = new PieceFactory(board); // Usa Random real

        // When
        Piece newPiece = pieceFactory.getNewPiece();

        // Then
        assertNotNull(newPiece);
        assertNotNull(newPiece.getColor());
        assertNotNull(newPiece.getShape());
    }
}