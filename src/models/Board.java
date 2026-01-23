package models;

public class Board {
    private static final int BOARD_SIZE = 30;
    private Player[] squares;

    public Board() {
        squares = new Player[BOARD_SIZE + 1];
        initializeBoard();
    }

    public Board(Board other) {
        this.squares = other.squares.clone();
    }

    private void initializeBoard() {
        for (int i = 1; i <= 14; i++) {
            squares[i] = (i % 2 == 1) ? Player.WHITE : Player.BLACK;
        }
        for (int i = 15; i <= BOARD_SIZE; i++) {
            squares[i] = null;
        }
    }

    public Player getPieceAt(int position) {
        if (position < 1 || position > BOARD_SIZE) return null;
        return squares[position];
    }

    public void setPieceAt(int position, Player player) {
        if (position >= 1 && position <= BOARD_SIZE) {
            squares[position] = player;
        }
    }

    public void removePieceAt(int position) {
        setPieceAt(position, null);
    }

    public boolean isEmpty(int position) {
        return getPieceAt(position) == null;
    }

    public int[] getPiecePositions(Player player) {
        int[] positions = new int[7];
        int count = 0;
        for (int i = 1; i <= BOARD_SIZE && count < 7; i++) {
            if (squares[i] == player) {
                positions[count++] = i;
            }
        }
        int[] result = new int[count];
        System.arraycopy(positions, 0, result, 0, count);
        return result;
    }

    public int countPieces(Player player) {
        int count = 0;
        for (int i = 1; i <= BOARD_SIZE; i++) {
            if (squares[i] == player) count++;
        }
        return count;
    }

    public Board clone() {
        return new Board(this);
    }
}
