package cat.uab.tqs.GameControl;

import cat.uab.tqs.GameStructure.Board;
import cat.uab.tqs.GameStructure.Piece;
import cat.uab.tqs.GameStructure.PieceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameTestV1 {

    @Mock
    private Board board;

    @Mock
    private PieceFactory pieceFactory;

    @Mock
    private Piece currentPiece;

    private Game game;

    @BeforeEach
    void setUp() {
        // Manually initialize Game with mocks to ensure correct injection
        // We mock the behavior of spawnNewPiece for a controlled setup
        when(pieceFactory.getNewPiece()).thenReturn(currentPiece);
        when(board.isValidPosition(any(), any())).thenReturn(true);
        game = new Game(board, pieceFactory);
    }

    @Test
    void testGameInitialization() {
        // This test now verifies the state set up in the @BeforeEach method
        assertNotNull(game.getBoard());
        assertEquals(currentPiece, game.getCurrentPiece());
        assertFalse(game.isGameOver());
        // Verify that spawnNewPiece was called once by the constructor
        verify(pieceFactory, times(1)).getNewPiece();
        verify(board, times(1)).isValidPosition(any(), any());
    }

    @Test
    void testMoveLeft() {
        // Given
        game.setCurrentPieceForTest(currentPiece); // Helper method to set piece after construction

        // When
        game.moveLeft();

        // Then
        verify(currentPiece, times(1)).moveLeft();
    }

    @Test
    void testMoveRight() {
        // Given
        game.setCurrentPieceForTest(currentPiece);

        // When
        game.moveRight();

        // Then
        verify(currentPiece, times(1)).moveRight();
    }

    @Test
    void testRotatePiece() {
        // Given
        game.setCurrentPieceForTest(currentPiece);

        // When
        game.rotatePiece();

        // Then
        verify(currentPiece, times(1)).rotate();
    }

    @Test
    void testMoveDown_CanMove() {
        // Given
        game.setCurrentPieceForTest(currentPiece);
        when(currentPiece.canMoveDown()).thenReturn(true);

        // When
        game.moveDown();

        // Then
        verify(currentPiece, times(1)).moveDown();
        verify(board, never()).placePiece(any(), any(), any());
    }

    @Test
    void testMoveDown_CannotMove() {
        // Given
        game.setCurrentPieceForTest(currentPiece);
        when(currentPiece.canMoveDown()).thenReturn(false);
        when(currentPiece.getShape()).thenReturn(new Point[]{new Point(0, 0)});
        when(currentPiece.getPosition()).thenReturn(new Point(0,0));
        when(currentPiece.getColor()).thenReturn(Color.RED);

        Piece newPiece = mock(Piece.class);
        // Re-stub the mock behavior for the spawnNewPiece call inside moveDown
        when(pieceFactory.getNewPiece()).thenReturn(newPiece);
        when(board.isValidPosition(any(), any())).thenReturn(true);

        // When
        game.moveDown();

        // Then
        verify(board, times(1)).placePiece(any(), any(), any());
        verify(board, times(1)).clearLines();
        // Verify getNewPiece was called once in setUp and once in moveDown
        verify(pieceFactory, times(2)).getNewPiece();
        assertEquals(newPiece, game.getCurrentPiece());
    }

    @Test
    void testGameOverInitialization() {
        // Given
        reset(board, pieceFactory); // Reset mocks from setUp
        when(pieceFactory.getNewPiece()).thenReturn(currentPiece);
        when(currentPiece.getShape()).thenReturn(new Point[]{new Point(0, 0)});
        when(currentPiece.getPosition()).thenReturn(new Point(0,0));
        when(board.isValidPosition(any(), any())).thenReturn(false); // Simulate no valid position

        // When
        Game gameOverGame = new Game(board, pieceFactory);

        // Then
        assertTrue(gameOverGame.isGameOver());
    }
}
