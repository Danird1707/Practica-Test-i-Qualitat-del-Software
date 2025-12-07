package cat.uab.tqs.GameStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.Color;
import java.awt.Point;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Clase de prueba para {@link Board} utilizando la técnica de **Pairwise Testing** (Pruebas por Pares).
 *
 * El objetivo es verificar el método {@link Board#isValidPosition(Point[], Point)} probando
 * combinaciones estratégicas de parámetros (Posición X, Posición Y, Presencia de Colisiones)
 * para maximizar la cobertura con un número reducido de casos de prueba, en lugar de probar
 * todas las combinaciones posibles (fuerza bruta).
 */
class BoardPairwiseTest {

  /** Instancia del tablero bajo prueba. */
  private Board board;

  /**
   * Definición de una pieza cuadrada de 2x2.
   * <p>
   * Se utiliza una forma constante y simple para facilitar el razonamiento sobre los
   * límites del tablero y las colisiones en las pruebas parametrizadas.
   * </p>
   */
  private final Point[] shapeSquare = { new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) };

  /**
   * Configuración inicial antes de cada prueba.
   * Inicializa un tablero vacío.
   */
  @BeforeEach
  void setUp() {
    board = new Board();
  }

  /**
   * Proveedor de datos para la prueba parametrizada de pares.
   *
   * Genera un flujo de argumentos que representan diferentes escenarios combinatorios.
   * Los parámetros definidos en el array de objetos son:
   * <ol>
   * <li><b>x:</b> Coordenada X objetivo de la pieza.</li>
   * <li><b>y:</b> Coordenada Y objetivo de la pieza.</li>
   * <li><b>collisionCellX:</b> Coordenada X de un obstáculo (-1 si no hay obstáculo).</li>
   * <li><b>collisionCellY:</b> Coordenada Y de un obstáculo (-1 si no hay obstáculo).</li>
   * <li><b>expectedResult:</b> Resultado esperado (true = posición válida, false = inválida).</li>
   * </ol>
   *
   * @return Stream de argumentos para inyectar en el método de prueba.
   */
  static Stream<Object[]> pairwiseProvider() {
    // Parámetros: x, y, collisionCellX, collisionCellY, expectedResult
    // collisionCellX/Y = -1 significa sin colisión
    return Stream.of(
        // Basado en una tabla de cobertura de pares generada (simplificada para el ejemplo)
        // Columna 1: Posición X (Izquierda, Centro, Derecha)
        // Columna 2: Posición Y (Arriba, Centro, Abajo)
        // Columna 3: Colisión (Sí, No)

        // Caso 1: Intento mover a la izquierda (negativo), Centro Y, Sin obstáculos -> Error de límites.
        new Object[]{-1, 5, -1, -1, false},

        // Caso 2: Centro X, Muy abajo (fuera del grid), Sin obstáculos -> Error de límites inferiores.
        new Object[]{4, 19, -1, -1, false},

        // Caso 3: Derecha X, Centro Y, Con obstáculo justo en esa posición -> Error de colisión.
        new Object[]{9, 10, 9, 10, false},

        // Caso 4: Izquierda (borde 0), Arriba (0), Sin obstáculos -> Válido (Inicio del juego normal).
        new Object[]{0, 0, -1, -1, true},

        // Caso 5: Centro X, Abajo, Con obstáculo en el camino -> Error de colisión.
        new Object[]{4, 18, 5, 18, false},

        // Caso 6: Derecha (borde válido), Arriba, Sin obstáculos -> Válido.
        new Object[]{8, 0, -1, -1, true},

        // Caso 7: Derecha (borde excedido), Centro Y, Sin obstáculos -> Error de límites derechos (Pieza 2x2 se sale en X=9+1).
        new Object[]{9, 5, -1, -1, false},

        // Caso 8: Izquierda, Muy abajo, Sin obstáculos -> Error de límites.
        new Object[]{0, 19, -1, -1, false},

        // Caso 9: Centro X, Arriba, Con obstáculo ocupando una celda de la pieza -> Error de colisión.
        new Object[]{4, 0, 4, 1, false}
    );
  }

  /**
   * Prueba parametrizada que ejecuta los casos de prueba definidos en {@link #pairwiseProvider()}.
   *
   * <p>Esta prueba realiza dos acciones principales:</p>
   * <ol>
   * <li><b>Arrange (Preparación):</b> Si se especifican coordenadas de colisión (distintas de -1),
   * coloca manualmente una celda ocupada (Color.RED) en el grid del tablero para simular un obstáculo.</li>
   * <li><b>Act & Assert (Ejecución y Verificación):</b> Llama a {@code isValidPosition} con la forma cuadrada
   * y las coordenadas dadas, y compara el resultado con el valor esperado.</li>
   * </ol>
   *
   * @param x Coordenada X de la posición a probar.
   * @param y Coordenada Y de la posición a probar.
   * @param collisionX Coordenada X donde colocar un obstáculo previo (o -1).
   * @param collisionY Coordenada Y donde colocar un obstáculo previo (o -1).
   * @param expectedResult {@code true} si la posición debería ser válida, {@code false} en caso contrario.
   */
  @ParameterizedTest
  @MethodSource("pairwiseProvider")
  void testIsValidPosition_Pairwise(int x, int y, int collisionX, int collisionY, boolean expectedResult) {
    // Arrange: Si es necesario, colocar una pieza para simular la colisión
    if (collisionX != -1 && collisionY != -1) {
      board.getGrid()[collisionY][collisionX] = Color.RED;
    }

    // Act & Assert
    assertEquals(expectedResult, board.isValidPosition(shapeSquare, new Point(x, y)));
  }
}