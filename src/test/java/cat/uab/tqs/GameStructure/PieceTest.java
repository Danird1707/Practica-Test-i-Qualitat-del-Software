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

/**
 * Clase de pruebas unitarias para la clase {@link Piece}.
 *
 * Verifica la manipulación del estado de una pieza individual, incluyendo:
 * <ul>
 * <li>Inicialización de propiedades.</li>
 * <li>Movimientos traslacionales (izquierda, derecha, abajo).</li>
 * <li>Lógica de rotación.</li>
 * <li>Integración con {@link Board} para validar si un movimiento es legal.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class PieceTest {

  /**
   * Mock del tablero. La pieza delega en el tablero la responsabilidad
   * de saber si una coordenada está libre o dentro de los límites.
   */
  @Mock
  private Board board;

  /** La instancia de la pieza bajo prueba (SUT). */
  private Piece piece;

  /**
   * Definición de la forma 'L' usada para las pruebas.
   * Se clona en el setup para evitar mutaciones entre tests.
   */
  private final Point[] shapeL = { new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(1, 2) };

  /**
   * Configuración inicial antes de cada prueba.
   * Crea una nueva pieza 'L' de color naranja en la posición inicial por defecto.
   */
  @BeforeEach
  void setUp() {
    piece = new Piece(shapeL.clone(), Color.ORANGE, board);
  }

  /**
   * Verifica que el constructor inicialice correctamente los atributos.
   * Comprueba:
   * 1. La forma (array de puntos).
   * 2. El color.
   * 3. La posición inicial (centrada horizontalmente en la parte superior).
   */
  @Test
  void testPieceInitialization() {
    assertArrayEquals(shapeL, piece.getShape());
    assertEquals(Color.ORANGE, piece.getColor());
    // Asume que la posición inicial es x=(Ancho/2)-1, y=0
    assertEquals(new Point(Board.BOARD_WIDTH / 2 - 1, 0), piece.getPosition());
  }

  // --- Tests para movimientos (Data-Driven) ---

  /**
   * Prueba parametrizada (Data-Driven) que cubre la lógica de movimiento.
   *
   * Permite probar múltiples direcciones y escenarios de validez en un solo método.
   * Configura el comportamiento del mock {@code board.isValidPosition} según el parámetro {@code isValid}.
   *
   * @param direction Dirección del movimiento ("DOWN", "LEFT", "RIGHT").
   * @param isValid Define si el tablero (mock) permitirá el movimiento.
   * @param expectedDx Cambio esperado en la coordenada X.
   * @param expectedDy Cambio esperado en la coordenada Y.
   */
  @ParameterizedTest
  @CsvSource({
      "DOWN,  true,  0,  1",  // Bajar permitido: Y incrementa en 1
      "DOWN,  false, 0,  0",  // Bajar bloqueado: Posición no cambia
      "LEFT,  true,  -1, 0",  // Izquierda permitida: X decrementa en 1
      "LEFT,  false, 0,  0",  // Izquierda bloqueada: Posición no cambia
      "RIGHT, true,  1,  0",  // Derecha permitida: X incrementa en 1
      "RIGHT, false, 0,  0"   // Derecha bloqueada: Posición no cambia
  })
  void testPieceMovement_DataDriven(String direction, boolean isValid, int expectedDx, int expectedDy) {
    // Given: Configuramos el mock para aceptar o rechazar el movimiento
    when(board.isValidPosition(any(), any(Point.class))).thenReturn(isValid);
    Point initialPosition = new Point(piece.getPosition());

    // When: Ejecutamos el movimiento correspondiente
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

    // Then: Verificamos que la posición ha cambiado (o no) según lo esperado
    assertEquals(initialPosition.x + expectedDx, piece.getPosition().x);
    assertEquals(initialPosition.y + expectedDy, piece.getPosition().y);
  }

  // --- Tests para rotación ---

  /**
   * Verifica que la pieza rota su forma geométrica cuando el tablero lo permite.
   *
   * Se compara la forma (los puntos relativos) antes y después de llamar a {@code rotate()}.
   * Al menos un punto debe haber cambiado de posición relativa.
   */
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

    assertTrue(changed, "La forma de la pieza debería cambiar tras una rotación válida");
  }

  /**
   * Verifica que la rotación se cancela (rollback) si el movimiento es inválido.
   *
   * Si {@code board.isValidPosition} devuelve {@code false} (por colisión o límites),
   * la forma de la pieza debe permanecer exactamente igual que al inicio.
   */
  @Test
  void testRotate_Invalid() {
    when(board.isValidPosition(any(), any(Point.class))).thenReturn(false);
    Point[] initialShape = piece.getShape();
    piece.rotate();
    assertArrayEquals(initialShape, piece.getShape());
  }

  // --- Tests para canMoveDown ---

  /**
   * Verifica el método auxiliar {@code canMoveDown} cuando hay espacio libre.
   *
   * Simula que la posición inmediatamente inferior (y+1) es válida.
   */
  @Test
  void testCanMoveDown_True() {
    when(board.isValidPosition(piece.getShape(), new Point(piece.getPosition().x, piece.getPosition().y + 1))).thenReturn(true);
    assertTrue(piece.canMoveDown());
  }

  /**
   * Verifica el método auxiliar {@code canMoveDown} cuando hay un obstáculo.
   *
   * Simula que la posición inmediatamente inferior (y+1) es inválida.
   */
  @Test
  void testCanMoveDown_False() {
    when(board.isValidPosition(piece.getShape(), new Point(piece.getPosition().x, piece.getPosition().y + 1))).thenReturn(false);
    assertFalse(piece.canMoveDown());
  }
}