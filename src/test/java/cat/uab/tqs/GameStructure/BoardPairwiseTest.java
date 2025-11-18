package cat.uab.tqs.GameStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.Color;
import java.awt.Point;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardPairwiseTest {

    private Board board;
    // Usaremos una pieza simple de 2x2 para facilitar el razonamiento sobre los límites
    private final Point[] shapeSquare = { new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) };

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    static Stream<Object[]> pairwiseProvider() {
        // Parámetros: x, y, collisionCellX, collisionCellY, expectedResult
        // collisionCellX/Y = -1 significa sin colisión
        return Stream.of(
            // Basado en una tabla de cobertura de pares generada (simplificada para el ejemplo)
            // Columna 1: Posición X (Izquierda, Centro, Derecha)
            // Columna 2: Posición Y (Arriba, Centro, Abajo)
            // Columna 3: Colisión (Sí, No)
            new Object[]{-1, 5, -1, -1, false},   // Izquierda, Centro, Sin Colisión -> Inválido (Fuera de límites)
            new Object[]{4, 19, -1, -1, false},  // Centro, Abajo, Sin Colisión -> Inválido (Fuera de límites)
            new Object[]{9, 10, 9, 10, false},   // Derecha, Centro, Con Colisión -> Inválido (Colisión)
            new Object[]{0, 0, -1, -1, true},    // Izquierda, Arriba, Sin Colisión -> Válido
            new Object[]{4, 18, 5, 18, false},   // Centro, Abajo, Con Colisión -> Inválido (Colisión)
            new Object[]{8, 0, -1, -1, true},    // Derecha, Arriba, Sin Colisión -> Válido
            new Object[]{9, 5, -1, -1, false},   // Derecha, Centro, Sin Colisión -> Inválido (Fuera de límites)
            new Object[]{0, 19, -1, -1, false},  // Izquierda, Abajo, Sin Colisión -> Inválido (Fuera de límites)
            new Object[]{4, 0, 4, 1, false}     // Centro, Arriba, Con Colisión -> Inválido (Colisión)
        );
    }

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
