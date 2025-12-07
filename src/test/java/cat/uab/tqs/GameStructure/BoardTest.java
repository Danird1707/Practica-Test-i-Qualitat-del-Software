package cat.uab.tqs.GameStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.awt.Color;
import java.awt.Point;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para la clase {@link Board}.
 * * Verifica la lógica central del estado del juego, incluyendo:
 * <ul>
 * <li>Inicialización correcta de la matriz.</li>
 * <li>Validación de posiciones (límites y colisiones).</li>
 * <li>Colocación de piezas en el tablero.</li>
 * <li>Mecánica de limpieza de líneas y gravedad.</li>
 * </ul>
 */
class BoardTest {

  /** Instancia del tablero bajo prueba (SUT). */
  private Board board;

  /** * Pieza auxiliar en forma de 'I' horizontal (4 bloques de ancho).
   * Útil para probar límites laterales.
   */
  private final Point[] shapeI = { new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0) };

  /**
   * Configuración inicial antes de cada prueba.
   * Reinicia el tablero para asegurar un estado limpio y aislamiento entre tests.
   */
  @BeforeEach
  void setUp() {
    board = new Board();
  }

  /**
   * Verifica la correcta inicialización del tablero.
   * * Asegura que:
   * 1. La cuadrícula (grid) no es nula.
   * 2. Las dimensiones coinciden con las constantes definidas.
   * 3. Todas las celdas comienzan vacías (null).
   */
  @Test
  void testBoardInitialization() {
    Color[][] grid = board.getGrid();
    assertNotNull(grid);
    assertEquals(Board.BOARD_HEIGHT, grid.length);
    assertEquals(Board.BOARD_WIDTH, grid[0].length);
    for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
      for (int j = 0; j < Board.BOARD_WIDTH; j++) {
        assertNull(grid[i][j], "Todas las celdas deberían inicializarse a null");
      }
    }
  }

  // --- Tests para isValidPosition (Data-Driven) ---

  /**
   * Prueba parametrizada (Data-Driven) para verificar límites del tablero.
   * * Utiliza {@link CsvSource} para probar múltiples escenarios de coordenadas con la pieza 'I'.
   * * @param x Coordenada X a probar.
   * @param y Coordenada Y a probar.
   * @param expected Resultado esperado (true = válido, false = inválido).
   */
  @ParameterizedTest
  @CsvSource({
      "0, 0, true",      // Posición válida en la esquina superior izquierda
      "-1, 0, false",     // Fuera por la izquierda
      "7, 0, false",      // Fuera por la derecha (shapeI tiene 4 de ancho, en tablero ancho 10, x=7 ocupa 7,8,9,10 -> error)
      "0, 20, false",     // Fuera por abajo (asumiendo altura 20, índice 20 es out of bounds)
      "0, -1, true"       // Válido si está parcialmente arriba (zona de spawn)
  })
  void testIsValidPosition_DataDriven(int x, int y, boolean expected) {
    assertEquals(expected, board.isValidPosition(shapeI, new Point(x, y)));
  }

  /**
   * Verifica la detección de colisiones con bloques existentes.
   * * Escenario: Se coloca manualmente un bloque rojo en (1,1) y se intenta
   * validar si una nueva pieza puede ocupar esa misma posición.
   */
  @Test
  void testIsValidPosition_Collision() {
    board.getGrid()[1][1] = Color.RED;
    Point[] singlePoint = {new Point(0,0)};
    assertFalse(board.isValidPosition(singlePoint, new Point(1, 1)));
  }

  // --- Tests para placePiece ---

  /**
   * Verifica que {@code placePiece} escriba correctamente los colores en la matriz.
   * * Coloca una pieza 'I' en una posición específica y verifica que las 4 celdas
   * correspondientes en el grid tengan el color asignado.
   */
  @Test
  void testPlacePiece() {
    board.placePiece(shapeI, new Point(3, 5), Color.CYAN);
    assertEquals(Color.CYAN, board.getGrid()[5][3]);
    assertEquals(Color.CYAN, board.getGrid()[5][4]);
    assertEquals(Color.CYAN, board.getGrid()[5][5]);
    assertEquals(Color.CYAN, board.getGrid()[5][6]);
  }

  /**
   * Verifica el comportamiento al colocar una pieza parcialmente fuera del tablero (arriba).
   * * Importante para cuando las piezas se apilan hasta el techo o al hacer spawn.
   * Asegura que:
   * 1. No se lance una excepción (IndexOutOfBounds).
   * 2. Solo la parte visible (dentro del array) se guarde.
   */
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

  /**
   * Verifica que {@code clearLines} no elimine filas que no están completamente llenas.
   */
  @Test
  void testClearLines_NoLines() {
    board.getGrid()[Board.BOARD_HEIGHT - 1][0] = Color.RED; // Fila incompleta
    assertEquals(0, board.clearLines());
  }

  /**
   * Verifica la eliminación de una única línea completa y el efecto de gravedad.
   * * Escenario: Última fila llena, penúltima fila con un bloque.
   * Resultado esperado:
   * 1. Devuelve 1 línea borrada.
   * 2. La última fila queda vacía (o con los bloques que cayeron).
   * 3. El bloque de la penúltima fila baja a la última.
   */
  @Test
  void testClearLines_OneLine() {
    // Llenar la última fila
    for (int j = 0; j < Board.BOARD_WIDTH; j++) {
      board.getGrid()[Board.BOARD_HEIGHT - 1][j] = Color.BLUE;
    }
    board.getGrid()[Board.BOARD_HEIGHT - 2][5] = Color.RED; // Añadir un bloque en la fila de arriba

    assertEquals(1, board.clearLines());
    assertNull(board.getGrid()[Board.BOARD_HEIGHT - 1][0]); // La celda 0 ahora es null (la línea azul se fue)
    assertEquals(Color.RED, board.getGrid()[Board.BOARD_HEIGHT - 1][5]); // El bloque rojo bajó
  }

  /**
   * Verifica la eliminación simultánea de múltiples líneas.
   * * Comprueba que los bloques superiores caigan `n` posiciones, donde `n`
   * es el número de líneas eliminadas (en este caso, 2).
   */
  @Test
  void testClearLines_MultipleLines() {
    // Llenar las dos últimas filas
    for (int j = 0; j < Board.BOARD_WIDTH; j++) {
      board.getGrid()[Board.BOARD_HEIGHT - 1][j] = Color.BLUE;
      board.getGrid()[Board.BOARD_HEIGHT - 2][j] = Color.GREEN;
    }
    board.getGrid()[0][0] = Color.YELLOW; // Un bloque en la primera fila (techo)

    assertEquals(2, board.clearLines());
    assertNull(board.getGrid()[Board.BOARD_HEIGHT - 1][0]);
    assertNull(board.getGrid()[Board.BOARD_HEIGHT - 2][0]);
    assertEquals(Color.YELLOW, board.getGrid()[2][0]); // El bloque amarillo baja 2 posiciones (0 -> 2)
  }

  /**
   * Caso extremo: Tablero completamente lleno.
   * Debería limpiar todo el tablero.
   */
  @Test
  void testClearLines_FullBoard() {
    for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
      for (int j = 0; j < Board.BOARD_WIDTH; j++) {
        board.getGrid()[i][j] = Color.WHITE;
      }
    }
    assertEquals(Board.BOARD_HEIGHT, board.clearLines());
    // Verificar que todo es null
    for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
      for (int j = 0; j < Board.BOARD_WIDTH; j++) {
        assertNull(board.getGrid()[i][j]);
      }
    }
  }

  /**
   * Verifica que al borrar una línea intermedia, los bloques inferiores NO se muevan,
   * y solo los superiores caigan.
   */
  @Test
  void testClearLines_LineInTheMiddle() {
    int middleRow = Board.BOARD_HEIGHT / 2;
    // Llenar fila del medio
    for (int j = 0; j < Board.BOARD_WIDTH; j++) {
      board.getGrid()[middleRow][j] = Color.ORANGE;
    }
    board.getGrid()[0][0] = Color.RED; // Bloque arriba

    assertEquals(1, board.clearLines());
    assertNull(board.getGrid()[middleRow][0]); // La línea naranja desaparece
    assertEquals(Color.RED, board.getGrid()[1][0]); // El bloque rojo baja 1 posición
  }

  /**
   * Verifica que ejecutar la limpieza en un tablero vacío es seguro
   * y retorna 0.
   */
  @Test
  void testClearLines_EmptyBoard() {
    // El tablero se inicializa vacío en setUp()
    assertEquals(0, board.clearLines());
    // Verificamos que el tablero sigue vacío
    for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
      for (int j = 0; j < Board.BOARD_WIDTH; j++) {
        assertNull(board.getGrid()[i][j]);
      }
    }
  }
}