package cat.uab.tqs.GameStructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para {@link PieceFactory}.
 *
 * Verifica la lógica de generación de piezas. Dado que la fábrica utiliza
 * {@link java.util.Random}, estas pruebas emplean una técnica de
 * "subclase anónima" (stubbing manual) para forzar al generador aleatorio
 * a devolver valores predecibles y así poder testear formas específicas.
 */
@ExtendWith(MockitoExtension.class)
class PieceFactoryTest {

  /**
   * Mock del tablero. Aunque la fábrica no lo usa para *decidir* qué pieza crear,
   * las piezas generadas requieren una referencia al tablero para validar sus futuros movimientos.
   */
  @Mock
  private Board board; // ✅ solo mockeamos Board

  /**
   * Prueba la creación de la pieza en forma de 'I' (Línea).
   *
   * <p>Estrategia:</p>
   * Se inyecta una instancia de {@link Random} manipulada que siempre devuelve 0.
   * Según la implementación del juego, el índice 0 corresponde a la pieza 'I'.
   *
   * <p>Verificaciones:</p>
   * <ul>
   * <li>El color debe ser {@code Color.CYAN}.</li>
   * <li>La forma debe corresponder a una línea horizontal de 4 bloques.</li>
   * </ul>
   */
  @Test
  void testGetNewPiece_CreatesIShape() {
    // Given
    Random predictableRandom = new Random() {
      @Override
      public int nextInt(int bound) {
        return 0; // Fuerza la pieza I (índice 0)
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

  /**
   * Prueba la creación de la pieza en forma de 'O' (Cuadrado).
   *
   * <p>Estrategia:</p>
   * Se inyecta una instancia de {@link Random} manipulada que siempre devuelve 3.
   * Según la implementación, el índice 3 corresponde a la pieza cuadrada.
   *
   * <p>Verificaciones:</p>
   * <ul>
   * <li>El color debe ser {@code Color.YELLOW}.</li>
   * <li>La forma debe ser un bloque de 2x2.</li>
   * </ul>
   */
  @Test
  void testGetNewPiece_CreatesOShape() {
    // Given
    Random predictableRandom = new Random() {
      @Override
      public int nextInt(int bound) {
        return 3; // Fuerza la pieza O (índice 3)
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

  /**
   * Prueba de integridad básica (Smoke Test).
   *
   * Verifica que la fábrica funcione correctamente utilizando el generador
   * aleatorio real de Java. No comprueba qué pieza específica sale, sino que
   * el objeto resultante sea válido y tenga atributos consistentes.
   */
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