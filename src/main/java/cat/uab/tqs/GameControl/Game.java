package cat.uab.tqs.GameControl;

import cat.uab.tqs.GameStructure.Board;
import cat.uab.tqs.GameStructure.Piece;
import cat.uab.tqs.GameStructure.PieceFactory;

import java.awt.*;

/**
 * Clase que controla la lógica principal del juego Tetris.
 */
public class Game {
    private final Board board;
    private final PieceFactory pieceFactory;
    private Piece currentPiece;
    private boolean gameOver = false;

    public Game() {
        this.board = new Board();
        this.pieceFactory = new PieceFactory(board);
        spawnNewPiece();
    }

    /**
     * Constructor for testing purposes, allowing dependency injection.
     */
    Game(Board board, PieceFactory pieceFactory) {
        this.board = board;
        this.pieceFactory = pieceFactory;
        if (this.pieceFactory != null) {
            spawnNewPiece();
        }
    }

    /**
     * Genera una nueva pieza y comprueba si hay espacio para colocarla.
     */
    private void spawnNewPiece() {
        currentPiece = pieceFactory.getNewPiece();
        if (!board.isValidPosition(currentPiece.getShape(), currentPiece.getPosition())) {
            gameOver = true;
        }
    }

    public Board getBoard() {
        return board;
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Mueve la pieza actual hacia la izquierda.
     */
    public void moveLeft() {
        if (!gameOver) {
            currentPiece.moveLeft();
        }
    }

    /**
     * Mueve la pieza actual hacia la derecha.
     */
    public void moveRight() {
        if (!gameOver) {
            currentPiece.moveRight();
        }
    }

    /**
     * Mueve la pieza actual hacia abajo. Si no puede, la fija al tablero.
     */
    public void moveDown() {
        if (gameOver) return;

        if (currentPiece.canMoveDown()) {
            currentPiece.moveDown();
        } else {
            // Colocar pieza en el tablero
            board.placePiece(currentPiece.getShape(), currentPiece.getPosition(), currentPiece.getColor());
            // Borrar líneas completas
            board.clearLines();
            // Crear nueva pieza
            spawnNewPiece();
        }
    }

    /**
     * Rota la pieza actual.
     */
    public void rotatePiece() {
        if (!gameOver) {
            currentPiece.rotate();
        }
    }


    /**
     * Sets the current piece for testing purposes.
     * @param piece The piece to set.
     */
    public void setCurrentPieceForTest(Piece piece) {
        this.currentPiece = piece;
    }

    /**
     * Sets the game over flag for testing purposes.
     * @param gameOver The value to set.
     */
    public void setGameOverForTest(boolean gameOver) {
        this.gameOver = gameOver;
    }

}
