package cat.uab.tqs.GraficInterface;

import cat.uab.tqs.GameControl.Game;


import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del juego Tetris.
 */
public class GameWindow extends JFrame {

    private final GamePanel gamePanel;
    private final Game game;
    private Timer timer;

    public GameWindow() {
        setTitle("Tetris - Proyecto UAB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Crear el juego y panel gráfico
        game = new Game();
        gamePanel = new GamePanel(game);

        // Configurar panel
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Temporizador del bucle principal (baja piezas cada 500 ms)
        timer = new Timer(500, e -> {
            if (!game.isGameOver()) {
                game.moveDown();
                gamePanel.repaint();
            } else {
                timer.stop();
                showGameOverDialog();
            }
        });
        timer.start();
        SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);
    }

    private void showGameOverDialog() {
        JOptionPane.showMessageDialog(this, "¡Game Over!", "Fin del juego", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Configura el look & feel
        SwingUtilities.invokeLater(GameWindow::new);
    }
}
