package cat.uab.tqs.GraficInterface;

import cat.uab.tqs.GameStructure.Board;
import cat.uab.tqs.GameStructure.Piece;
import cat.uab.tqs.GameStructure.PieceFactory;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Board board = new Board();
            PieceFactory pieceFactory = new PieceFactory(board);
            GamePanel gamePanel = new GamePanel(board);
            new GameWindow(gamePanel);

            // Crear la primera pieza
            Piece currentPiece = pieceFactory.getNewPiece();
            gamePanel.setCurrentPiece(currentPiece);
            gamePanel.repaint();

            /*// Lógica simple para que la pieza baje
            Timer timer = new Timer(500, e -> {
                if (currentPiece.canMoveDown()) {
                    currentPiece.moveDown();
                } else {
                    board.placePiece(currentPiece.getShape(), currentPiece.getPosition(), currentPiece.getColor());
                    board.clearLines();
                    Piece newPiece = pieceFactory.getNewPiece();
                    if (board.isValidPosition(newPiece.getShape(), newPiece.getPosition())) {
                        gamePanel.setCurrentPiece(newPiece);
                    } else {
                        // Game Over
                        ((Timer)e.getSource()).stop();
                        System.out.println("Game Over");
                    }
                }
                gamePanel.repaint();
            });
            timer.start();*/
        });
    }
}

