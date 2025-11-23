package cat.uab.tqs.GameControl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Clase que maneja las entradas de teclado del jugador.
 */
public class Inputs implements KeyListener {

    private final Game game;

    public Inputs(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                game.moveLeft();
                break;

            case KeyEvent.VK_RIGHT:
                game.moveRight();
                break;

            case KeyEvent.VK_DOWN:
                game.moveDown();
                break;

            case KeyEvent.VK_SPACE:
                // 🔁 Cada vez que se pulsa la barra espaciadora, gira la pieza
                game.rotatePiece();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No hace falta implementar nada aquí
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Tampoco hace falta implementar nada aquí
    }
}