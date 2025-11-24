package cat.uab.tqs.GameControl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Canvas;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

public class InputsTest {

    private Game game;
    private Inputs inputs;
    private Canvas canvas;

    @BeforeEach
    void setUp() {
        game = Mockito.mock(Game.class);
        inputs = new Inputs(game);
        canvas = new Canvas(); // requerido para construir KeyEvent
    }

    @Test
    void testKeyPressedLeft() {
        KeyEvent event = new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        inputs.keyPressed(event);
        verify(game).moveLeft();
        verify(game, never()).moveRight();
        verify(game, never()).moveDown();
        verify(game, never()).rotatePiece();
    }

    @Test
    void testKeyPressedRight() {
        KeyEvent event = new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        inputs.keyPressed(event);
        verify(game).moveRight();
        verify(game, never()).moveLeft();
        verify(game, never()).moveDown();
        verify(game, never()).rotatePiece();
    }

    @Test
    void testKeyPressedDown() {
        KeyEvent event = new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
        inputs.keyPressed(event);
        verify(game).moveDown();
        verify(game, never()).moveLeft();
        verify(game, never()).moveRight();
        verify(game, never()).rotatePiece();
    }

    @Test
    void testKeyPressedSpaceRotate() {
        KeyEvent event = new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        inputs.keyPressed(event);
        verify(game).rotatePiece();
        verify(game, never()).moveLeft();
        verify(game, never()).moveRight();
        verify(game, never()).moveDown();
    }

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
