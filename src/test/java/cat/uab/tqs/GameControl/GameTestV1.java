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

/**
 * Clase de prueba unitaria para la clase {@link Game}.
 *
 * Utiliza Mockito para simular el comportamiento de las dependencias
 * {@link Board} y {@link PieceFactory}, permitiendo probar la lógica de la
 * clase {@link Game} de forma aislada.
 *
 * La implementación sugiere un juego tipo Tetris, donde las piezas se mueven y
 * colocan en un tablero.
 */
@ExtendWith(MockitoExtension.class)
class GameTestV1 {

  /** Mock para simular la interacción con el tablero de juego. */
  @Mock
  private Board board;

  /** Mock para simular la creación de nuevas piezas. */
  @Mock
  private PieceFactory pieceFactory;

  /** Mock para simular la pieza actualmente en juego. */
  @Mock
  private Piece currentPiece;

  /** Instancia de la clase {@link Game} a probar. */
  private Game game;

  /**
   * Configuración inicial que se ejecuta antes de cada método de prueba.
   *
   * Inicializa la instancia de {@link Game} inyectando los mocks. Se
   * configura el comportamiento inicial para que la fábrica devuelva
   * `currentPiece` y el tablero siempre considere válida la posición inicial,
   * simulando un inicio de juego normal.
   */
  @BeforeEach
  void setUp() {
    // Manually initialize Game with mocks to ensure correct injection
    // We mock the behavior of spawnNewPiece for a controlled setup
    when(pieceFactory.getNewPiece()).thenReturn(currentPiece);
    when(board.isValidPosition(any(), any())).thenReturn(true);
    game = new Game(board, pieceFactory);
  }

  /**
   * Prueba el constructor sin parámetros de {@link Game}.
   *
   * Verifica que tanto el tablero como la pieza actual se inicialicen
   * correctamente (no sean nulos) cuando se usa el constructor por defecto.
   */
  @Test
  void testConstructorGameNoParemeters() {
    game = new Game();
    assertNotNull(game.getBoard());
    assertNotNull(game.getCurrentPiece());
  }

  /**
   * Prueba la inicialización del juego al usar el constructor con parámetros.
   *
   * Verifica que:
   * 1. El tablero no es nulo.
   * 2. La pieza actual es la pieza mockeada (`currentPiece`).
   * 3. El juego no está en estado "Game Over".
   * 4. Se llamó a {@code pieceFactory.getNewPiece()} una vez para
   * generar la pieza inicial.
   * 5. Se llamó a {@code board.isValidPosition()} una vez para
   * verificar la posición inicial de la pieza.
   */
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

  /**
   * Prueba el método {@link Game#moveLeft()}.
   *
   * Verifica que al llamar a `moveLeft()`, se invoca el método
   * {@link Piece#moveLeft()} exactamente una vez en la pieza actual mockeada.
   */
  @Test
  void testMoveLeft() {
    // Given
    game.setCurrentPieceForTest(currentPiece); // Helper method to set piece after construction

    // When
    game.moveLeft();

    // Then
    verify(currentPiece, times(1)).moveLeft();
  }

  /**
   * Prueba el método {@link Game#moveRight()}.
   *
   * Verifica que al llamar a `moveRight()`, se invoca el método
   * {@link Piece#moveRight()} exactamente una vez en la pieza actual mockeada.
   */
  @Test
  void testMoveRight() {
    // Given
    game.setCurrentPieceForTest(currentPiece);

    // When
    game.moveRight();

    // Then
    verify(currentPiece, times(1)).moveRight();
  }

  /**
   * Prueba el método {@link Game#rotatePiece()}.
   *
   * Verifica que al llamar a `rotatePiece()`, se invoca el método
   * {@link Piece#rotate()} exactamente una vez en la pieza actual mockeada.
   */
  @Test
  void testRotatePiece() {
    // Given
    game.setCurrentPieceForTest(currentPiece);

    // When
    game.rotatePiece();

    // Then
    verify(currentPiece, times(1)).rotate();
  }

  /**
   * Prueba el método {@link Game#moveDown()} cuando la pieza *puede* moverse.
   *
   * Se configura el mock de la pieza para devolver `true` en {@link Piece#canMoveDown()}.
   * Verifica que:
   * 1. Se llama a {@link Piece#moveDown()} una vez.
   * 2. NO se llama a {@link Board#placePiece(Point[], Point, Color)}
   * (la pieza no se asienta).
   */
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

  /**
   * Prueba el método {@link Game#moveDown()} cuando la pieza *no puede* moverse (se asienta).
   *
   * Se configura el mock de la pieza para devolver `false` en {@link Piece#canMoveDown()}.
   * Verifica que:
   * 1. Se llama a {@link Board#placePiece(Point[], Point, Color)} una vez (la pieza se asienta).
   * 2. Se llama a {@link Board#clearLines()} una vez.
   * 3. Se llama a {@code pieceFactory.getNewPiece()} una segunda vez (una en `setUp`, otra para la nueva pieza).
   * 4. La pieza actual del juego se actualiza a la nueva pieza mockeada.
   */
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

  /**
   * Prueba la inicialización del juego cuando resulta en un estado "Game Over".
   *
   * Se configura el mock de {@link Board#isValidPosition(Point[], Point)}
   * para que devuelva `false` *antes* de la construcción de `Game`, simulando
   * que la nueva pieza no puede colocarse en el tablero.
   * Verifica que:
   * 1. El estado del juego es "Game Over" (`isGameOver()` devuelve `true`).
   */
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
