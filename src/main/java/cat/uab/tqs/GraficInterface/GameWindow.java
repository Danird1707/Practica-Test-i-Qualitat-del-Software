package cat.uab.tqs.GraficInterface;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
    // Panel del juego
    private final GamePanel gamePanel;

    /**
     * Constructor de la clase GameWindow.
     * @param gamePanel El panel del juego.
     */
    public GameWindow(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(gamePanel);
        pack(); // Ajusta el tamaño de la ventana para que se ajuste al panel

        setLocationRelativeTo(null); // Centra la ventana
        setVisible(true);
    }

    /**
     * Devuelve el panel del juego.
     * @return El panel del juego.
     */
    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
