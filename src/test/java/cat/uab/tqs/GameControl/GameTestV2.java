package cat.uab.tqs.GameControl;

import cat.uab.tqs.GameStructure.Board;
import cat.uab.tqs.GameStructure.Piece;
import cat.uab.tqs.GameStructure.PieceFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameTestV2 {

    @Mock
    private Board board;

    @Mock
    private PieceFactory pieceFactory;

    @Mock
    private Piece currentPiece;

    // @InjectMocks is removed to allow manual instantiation and control over constructor testing.
    private Game game;

    private void setupGameForMethodTests() {
        when(pieceFactory.getNewPiece()).thenReturn(currentPiece);
        when(board.isValidPosition(any(), any())).thenReturn(true);
        game = new Game(board, pieceFactory);
    }

    @Test
    void testGameInitialization_Success() {
        // Arrange
        when(pieceFactory.getNewPiece()).thenReturn(currentPiece);
        when(currentPiece.getShape()).thenReturn(new Point[]{new Point(0, 0)});
        when(currentPiece.getPosition()).thenReturn(new Point(0, 0));
        when(board.isValidPosition(any(), any())).thenReturn(true);

        // Act
        game = new Game(board, pieceFactory);

        // Assert
        assertNotNull(game.getBoard(), "Board should be initialized");
        assertEquals(currentPiece, game.getCurrentPiece(), "A new piece should be set as current");
        assertFalse(game.isGameOver(), "Game should not be over on successful initialization");
        verify(pieceFactory).getNewPiece();
        verify(board).isValidPosition(currentPiece.getShape(), currentPiece.getPosition());
    }

    @Test
    void testGameInitialization_GameOver() {
        // Arrange
        when(pieceFactory.getNewPiece()).thenReturn(currentPiece);
        when(currentPiece.getShape()).thenReturn(new Point[]{new Point(0, 0)});
        when(currentPiece.getPosition()).thenReturn(new Point(0, 0));
        when(board.isValidPosition(any(), any())).thenReturn(false);

        // Act
        game = new Game(board, pieceFactory);

        // Assert
        assertTrue(game.isGameOver(), "Game should be over if the new piece has no valid position");
    }

    @Test
    void testMoveLeft_WhenNotGameOver() {
        setupGameForMethodTests();
        game.setGameOverForTest(false);
        game.moveLeft();
        verify(currentPiece).moveLeft();
    }

    @Test
    void testMoveLeft_WhenGameOver() {
        setupGameForMethodTests();
        game.setGameOverForTest(true);
        game.moveLeft();
        verify(currentPiece, never()).moveLeft();
    }

    @Test
    void testMoveRight_WhenNotGameOver() {
        setupGameForMethodTests();
        game.setGameOverForTest(false);
        game.moveRight();
        verify(currentPiece).moveRight();
    }

    @Test
    void testMoveRight_WhenGameOver() {
        setupGameForMethodTests();
        game.setGameOverForTest(true);
        game.moveRight();
        verify(currentPiece, never()).moveRight();
    }

    @Test
    void testRotatePiece_WhenNotGameOver() {
        setupGameForMethodTests();
        game.setGameOverForTest(false);
        game.rotatePiece();
        verify(currentPiece).rotate();
    }

    @Test
    void testRotatePiece_WhenGameOver() {
        setupGameForMethodTests();
        game.setGameOverForTest(true);
        game.rotatePiece();
        verify(currentPiece, never()).rotate();
    }

    @Test
    void testMoveDown_WhenCanMoveDown() {
        setupGameForMethodTests();
        game.setGameOverForTest(false);
        when(currentPiece.canMoveDown()).thenReturn(true);

        game.moveDown();

        verify(currentPiece).moveDown();
        verify(board, never()).placePiece(any(), any(), any());
    }

    @Test
    void testMoveDown_WhenCannotMoveDown() {
        // Arrange: Mock behavior for constructor call
        Piece dummyPiece = mock(Piece.class);
        when(dummyPiece.getShape()).thenReturn(new Point[]{});
        when(dummyPiece.getPosition()).thenReturn(new Point());
        when(board.isValidPosition(any(), any())).thenReturn(true);
        when(pieceFactory.getNewPiece()).thenReturn(dummyPiece);

        // Create game instance and set the piece for the test
        game = new Game(board, pieceFactory);
        game.setCurrentPieceForTest(currentPiece);

        // Clear invocations from constructor to isolate the test
        clearInvocations(pieceFactory, board);

        // Arrange: Mock behavior for the moveDown method logic
        game.setGameOverForTest(false);
        when(currentPiece.canMoveDown()).thenReturn(false);
        when(currentPiece.getShape()).thenReturn(new Point[]{new Point(0, 0)});
        when(currentPiece.getPosition()).thenReturn(new Point(0, 0));
        when(currentPiece.getColor()).thenReturn(Color.BLUE);

        Piece newPiece = mock(Piece.class);
        when(pieceFactory.getNewPiece()).thenReturn(newPiece); // Re-stub for the actual call
        when(newPiece.getShape()).thenReturn(new Point[]{new Point(0, 0)});
        when(newPiece.getPosition()).thenReturn(new Point(0,0));
        when(board.isValidPosition(any(), any())).thenReturn(true); // Re-stub for the actual call

        // Act
        game.moveDown();

        // Assert
        verify(board).placePiece(currentPiece.getShape(), currentPiece.getPosition(), currentPiece.getColor());
        verify(board).clearLines();
        verify(pieceFactory).getNewPiece(); // Should verify only one call
        assertEquals(newPiece, game.getCurrentPiece(), "A new piece should have been spawned");
    }

    @Test
    void testMoveDown_WhenGameOver() {
        setupGameForMethodTests();
        game.setGameOverForTest(true);
        game.moveDown();
        verify(currentPiece, never()).canMoveDown();
        verify(currentPiece, never()).moveDown();
        verify(board, never()).placePiece(any(), any(), any());
    }
}
