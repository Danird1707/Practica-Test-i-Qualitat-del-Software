package cat.uab.tqs.GameControl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Canvas;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

/**
 * Clase de prueba unitaria para la clase {@link Inputs}.
 *
 * Esta clase verifica que el controlador de entrada (Input Listener) capture
 * correctamente los eventos del teclado y ejecute los métodos correspondientes
 * en la instancia de {@link Game}.
 *
 * Se utiliza un {@link Canvas} ficticio para poder instanciar objetos {@link KeyEvent}.
 */
public class InputsTest {

  /** Mock de la clase Game para verificar que se llaman a sus métodos. */
  private Game game;

  /** La instancia de la clase de entradas bajo prueba (SUT). */
  private Inputs inputs;

  /** Componente AWT necesario como fuente (source) para construir objetos KeyEvent válidos. */
  private Canvas canvas;

  /**
   * Configuración inicial antes de cada prueba.
   *
   * Inicializa el mock del juego, la instancia del controlador de inputs
   * y un canvas vacío para soportar la creación de eventos.
   */
  @BeforeEach
  void setUp() {
    game = Mockito.mock(Game.class);
    inputs = new Inputs(game);
    canvas = new Canvas(); // requerido para construir KeyEvent
  }

  /**
   * Prueba que al presionar la tecla "Flecha Izquierda" se mueva el juego a la izquierda.
   *
   * Verifica que:
   * 1. Se llama a {@link Game#moveLeft()}.
   * 2. No se llaman a otros métodos de movimiento o rotación.
   */
  @Test
  void testKeyPressedLeft() {
    KeyEvent event = new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
    inputs.keyPressed(event);
    verify(game).moveLeft();
    verify(game, never()).moveRight();
    verify(game, never()).moveDown();
    verify(game, never()).rotatePiece();
  }

  /**
   * Prueba que al presionar la tecla "Flecha Derecha" se mueva el juego a la derecha.
   *
   * Verifica que:
   * 1. Se llama a {@link Game#moveRight()}.
   * 2. No se llaman a otros métodos de movimiento o rotación.
   */
  @Test
  void testKeyPressedRight() {
    KeyEvent event = new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
    inputs.keyPressed(event);
    verify(game).moveRight();
    verify(game, never()).moveLeft();
    verify(game, never()).moveDown();
    verify(game, never()).rotatePiece();
  }

  /**
   * Prueba que al presionar la tecla "Flecha Abajo" se mueva el juego hacia abajo.
   *
   * Verifica que:
   * 1. Se llama a {@link Game#moveDown()}.
   * 2. No se llaman a otros métodos de movimiento lateral o rotación.
   */
  @Test
  void testKeyPressedDown() {
    KeyEvent event = new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
    inputs.keyPressed(event);
    verify(game).moveDown();
    verify(game, never()).moveLeft();
    verify(game, never()).moveRight();
    verify(game, never()).rotatePiece();
  }

  /**
   * Prueba que al presionar la tecla "Espacio" se rote la pieza.
   *
   * Verifica que:
   * 1. Se llama a {@link Game#rotatePiece()}.
   * 2. No se ejecutan movimientos de traslación.
   */
  @Test
  void testKeyPressedSpaceRotate() {
    KeyEvent event = new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
    inputs.keyPressed(event);
    verify(game).rotatePiece();
    verify(game, never()).moveLeft();
    verify(game, never()).moveRight();
    verify(game, never()).moveDown();
  }

  /**
   * Prueba que presionar una tecla no mapeada (ej. letra 'A') sea ignorado.
   *
   * Verifica que no se invoca ningún método en el objeto {@link Game}.
   */
  @Test
  void testKeyPressedOtherDoesNothing() {
    KeyEvent event = new KeyEvent(canvas, 0, 0, 0, KeyEvent.VK_A, 'A');
    inputs.keyPressed(event);

    // verificamos que no se llamó ningún método
    verify(game, never()).moveLeft();
    verify(game, never()).moveRight();
    verify(game, never()).moveDown();
    verify(game, never()).rotatePiece();
  }

  /**
   * Prueba que el evento de soltar una tecla (keyReleased) sea ignorado.
   *
   * Este test asegura que la lógica del juego solo responde a presionar teclas
   * (keyPressed) y no duplica acciones al soltarlas.
   */
  @Test
  void testKeyReleasedDoesNothing() {
    KeyEvent event = new KeyEvent(canvas, 0, 0, 0, KeyEvent.VK_A, 'A');
    inputs.keyReleased(event); // solo para cubrir la línea
    // No se debería invocar ningún método
    verify(game, never()).moveLeft();
    verify(game, never()).moveRight();
    verify(game, never()).moveDown();
    verify(game, never()).rotatePiece();
  }

  /**
   * Prueba que el evento de escribir una tecla (keyTyped) sea ignorado.
   *
   * Esto es importante para evitar comportamientos inesperados si el sistema
   * genera eventos de tipo 'typed' además de los 'pressed'.
   */
  @Test
  void testKeyTypedDoesNothing() {
    KeyEvent event = new KeyEvent(canvas, 0, 0, 0, KeyEvent.VK_A, 'A');
    inputs.keyTyped(event); // solo para cobertura
    verify(game, never()).moveLeft();
    verify(game, never()).moveRight();
    verify(game, never()).moveDown();
    verify(game, never()).rotatePiece();
  }
}