package models;

public class GameState {
    private Board board;
    private Player currentPlayer;
    private int whitePiecesExited;
    private int blackPiecesExited;
    private boolean gameOver;
    private Player winner;

    public GameState() {
        this.board = new Board();
        this.currentPlayer = Player.WHITE;
        this.whitePiecesExited = 0;
        this.blackPiecesExited = 0;
        this.gameOver = false;
        this.winner = null;
    }

    // Copy constructor
    public GameState(GameState other) {
        this.board = other.board.clone();
        this.currentPlayer = other.currentPlayer;
        this.whitePiecesExited = other.whitePiecesExited;
        this.blackPiecesExited = other.blackPiecesExited;
        this.gameOver = other.gameOver;
        this.winner = other.winner;
    }

    // Getters and setters
    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getPiecesExited(Player player) {
        return player == Player.WHITE ? whitePiecesExited : blackPiecesExited;
    }

    public void incrementPiecesExited(Player player) {
        if (player == Player.WHITE) {
            whitePiecesExited++;
        } else {
            blackPiecesExited++;
        }
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer.opponent();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getWinner() {
        return winner;
    }

    public void setGameOver(Player winner) {
        this.gameOver = true;
        this.winner = winner;
    }

    public GameState clone() {
        return new GameState(this);
    }

    public int getPiecesRemaining(Player player) {
        return 7 - getPiecesExited(player);
    }
}
