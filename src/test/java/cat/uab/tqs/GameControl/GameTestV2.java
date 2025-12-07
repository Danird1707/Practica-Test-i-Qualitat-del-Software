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

/**
 * Clase de prueba unitaria (Versión 2) para la clase {@link Game}.
 *
 * Esta versión extiende las pruebas para cubrir casos de borde adicionales,
 * específicamente el comportamiento de los controles cuando el juego ha
 * terminado (Game Over) y la lógica compleja de "aterrizaje" de una pieza.
 */
@ExtendWith(MockitoExtension.class)
class GameTestV2 {

  /** Mock del tablero de juego. */
  @Mock
  private Board board;

  /** Mock de la fábrica encargada de generar nuevas piezas. */
  @Mock
  private PieceFactory pieceFactory;

  /** Mock de la pieza activa actual. */
  @Mock
  private Piece currentPiece;

  /** La instancia del juego bajo prueba (SUT - System Under Test). */
  private Game game;

  /**
   * Prueba el constructor por defecto (sin parámetros).
   *
   * Verifica que el juego se inicializa con componentes internos
   * (Tablero y Pieza) no nulos.
   */
  @Test
  void testConstructorGameNoParemeters() {
    game = new Game();
    assertNotNull(game.getBoard());
    assertNotNull(game.getCurrentPiece());
  }

  /**
   * Método auxiliar de configuración para pruebas de métodos de movimiento.
   *
   * Configura los stubs básicos necesarios para instanciar un juego válido:
   * la fábrica devuelve una pieza y el tablero acepta su posición.
   * Esto evita duplicar código en tests como moveLeft, moveRight, etc.
   */
  private void setupGameForMethodTests() {
    when(pieceFactory.getNewPiece()).thenReturn(currentPiece);
    when(board.isValidPosition(any(), any())).thenReturn(true);
    game = new Game(board, pieceFactory);
  }

  /**
   * Prueba una inicialización exitosa del juego.
   *
   * Verifica que:
   * 1. Se obtiene una nueva pieza.
   * 2. La posición inicial es válida.
   * 3. El juego NO está en estado de "Game Over".
   */
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
    assertNotNull(game.getBoard(), "El tablero debe estar inicializado");
    assertEquals(currentPiece, game.getCurrentPiece(), "Debe establecerse una nueva pieza como actual");
    assertFalse(game.isGameOver(), "El juego no debería terminar en una inicialización exitosa");
    verify(pieceFactory).getNewPiece();
    verify(board).isValidPosition(currentPiece.getShape(), currentPiece.getPosition());
  }

  /**
   * Prueba la inicialización del juego cuando la primera pieza no cabe.
   *
   * Simula que {@code board.isValidPosition} devuelve {@code false}.
   * Verifica que el juego entra inmediatamente en estado "Game Over".
   */
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
    assertTrue(game.isGameOver(), "El juego debe terminar si la nueva pieza no tiene una posición válida");
  }

  /**
   * Prueba {@code moveLeft} cuando el juego está activo.
   *
   * Verifica que la orden se delega a la pieza actual.
   */
  @Test
  void testMoveLeft_WhenNotGameOver() {
    setupGameForMethodTests();
    game.setGameOverForTest(false);
    game.moveLeft();
    verify(currentPiece).moveLeft();
  }

  /**
   * Prueba {@code moveLeft} cuando el juego ha terminado.
   *
   * Verifica que NUNCA se llama a {@code moveLeft} en la pieza,
   * ya que los controles deben estar deshabilitados.
   */
  @Test
  void testMoveLeft_WhenGameOver() {
    setupGameForMethodTests();
    game.setGameOverForTest(true);
    game.moveLeft();
    verify(currentPiece, never()).moveLeft();
  }

  /**
   * Prueba {@code moveRight} cuando el juego está activo.
   *
   * Verifica que la orden se delega a la pieza actual.
   */
  @Test
  void testMoveRight_WhenNotGameOver() {
    setupGameForMethodTests();
    game.setGameOverForTest(false);
    game.moveRight();
    verify(currentPiece).moveRight();
  }

  /**
   * Prueba {@code moveRight} cuando el juego ha terminado.
   *
   * Verifica que NUNCA se llama a {@code moveRight} en la pieza.
   */
  @Test
  void testMoveRight_WhenGameOver() {
    setupGameForMethodTests();
    game.setGameOverForTest(true);
    game.moveRight();
    verify(currentPiece, never()).moveRight();
  }

  /**
   * Prueba {@code rotatePiece} cuando el juego está activo.
   *
   * Verifica que la orden de rotación se delega a la pieza actual.
   */
  @Test
  void testRotatePiece_WhenNotGameOver() {
    setupGameForMethodTests();
    game.setGameOverForTest(false);
    game.rotatePiece();
    verify(currentPiece).rotate();
  }

  /**
   * Prueba {@code rotatePiece} cuando el juego ha terminado.
   *
   * Verifica que NUNCA se llama a {@code rotate} en la pieza.
   */
  @Test
  void testRotatePiece_WhenGameOver() {
    setupGameForMethodTests();
    game.setGameOverForTest(true);
    game.rotatePiece();
    verify(currentPiece, never()).rotate();
  }

  /**
   * Prueba {@code moveDown} cuando la pieza tiene espacio para caer.
   *
   * Verifica que la pieza se mueve hacia abajo y NO se intenta
   * colocar en el tablero (no se bloquea).
   */
  @Test
  void testMoveDown_WhenCanMoveDown() {
    setupGameForMethodTests();
    game.setGameOverForTest(false);
    when(currentPiece.canMoveDown()).thenReturn(true);

    game.moveDown();

    verify(currentPiece).moveDown();
    verify(board, never()).placePiece(any(), any(), any());
  }

  /**
   * Prueba compleja de {@code moveDown} cuando la pieza NO puede bajar más (aterriza).
   *
   * Este test verifica la secuencia completa de aterrizaje:
   * 1. La pieza actual se bloquea/coloca en el tablero.
   * 2. Se limpian las líneas completas.
   * 3. Se genera una nueva pieza.
   *
   * Nota: Utiliza {@code clearInvocations} para ignorar las llamadas hechas
   * durante el constructor y centrarse solo en las llamadas dentro de {@code moveDown}.
   */
  @Test
  void testMoveDown_WhenCannotMoveDown() {
    // Arrange: Mock behavior for constructor call (Setup inicial)
    Piece dummyPiece = mock(Piece.class);
    when(dummyPiece.getShape()).thenReturn(new Point[]{});
    when(dummyPiece.getPosition()).thenReturn(new Point());
    when(board.isValidPosition(any(), any())).thenReturn(true);
    when(pieceFactory.getNewPiece()).thenReturn(dummyPiece);

    // Create game instance and set the piece for the test
    game = new Game(board, pieceFactory);
    game.setCurrentPieceForTest(currentPiece);

    // Clear invocations from constructor to isolate the test
    // (Limpia el historial de llamadas de la fase de construcción)
    clearInvocations(pieceFactory, board);

    // Arrange: Mock behavior for the moveDown method logic
    game.setGameOverForTest(false);
    when(currentPiece.canMoveDown()).thenReturn(false); // La pieza toca suelo
    when(currentPiece.getShape()).thenReturn(new Point[]{new Point(0, 0)});
    when(currentPiece.getPosition()).thenReturn(new Point(0, 0));
    when(currentPiece.getColor()).thenReturn(Color.BLUE);

    Piece newPiece = mock(Piece.class);
    when(pieceFactory.getNewPiece()).thenReturn(newPiece); // Re-configura para la llamada real
    when(newPiece.getShape()).thenReturn(new Point[]{new Point(0, 0)});
    when(newPiece.getPosition()).thenReturn(new Point(0,0));
    when(board.isValidPosition(any(), any())).thenReturn(true); // Re-configura para la llamada real

    // Act
    game.moveDown();

    // Assert
    verify(board).placePiece(currentPiece.getShape(), currentPiece.getPosition(), currentPiece.getColor());
    verify(board).clearLines();
    verify(pieceFactory).getNewPiece(); // Verifica que se llamó una vez (dentro de moveDown)
    assertEquals(newPiece, game.getCurrentPiece(), "Se debería haber generado una nueva pieza");
  }

  /**
   * Prueba {@code moveDown} cuando el juego ha terminado.
   *
   * Verifica que no ocurre ninguna lógica de movimiento ni colocación.
   */
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