package game;

import models.GameState;
import models.Board;
import models.Player;
import models.Square;
import models.Move;
import java.util.List;

public class BoardDisplay {

    /**
     * Print the board in S-pattern
     */
    public static void printBoard(GameState state) {
        Board board = state.getBoard();

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    SENET GAME BOARD                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        // Row 1: Squares 1-10
        printRow(board, 1, 10, false);

        System.out.println("╟────────────────────────────────────────────────────────────╢");

        // Row 2: Squares 11-20 (reversed)
        printRow(board, 20, 11, true);

        System.out.println("╟────────────────────────────────────────────────────────────╢");

        // Row 3: Squares 21-30
        printRow(board, 21, 30, false);

        System.out.println("╚════════════════════════════════════════════════════════════╝");

        // Game info
        printGameInfo(state);
    }

    private static void printRow(Board board, int start, int end, boolean reverse) {
        System.out.print("║ ");

        if (reverse) {
            for (int i = start; i >= end; i--) {
                printSquare(board, i);
            }
        } else {
            for (int i = start; i <= end; i++) {
                printSquare(board, i);
            }
        }

        System.out.println(" ║");
    }

    private static void printSquare(Board board, int position) {
        Player piece = board.getPieceAt(position);
        String special = getSpecialSymbol(position);

        if (piece != null) {
            System.out.printf(" %2d:%s ", position, piece.getSymbol());
        } else if (!special.isEmpty()) {
            System.out.printf(" %2d:%s ", position, special);
        } else {
            System.out.printf(" %2d:  ", position);
        }
    }

    private static String getSpecialSymbol(int position) {
        Square square = Square.getSquareType(position);
        return square == Square.NORMAL ? "" : square.getSymbol();
    }

    private static void printGameInfo(GameState state) {
        System.out.println("\n┌─────────────────────────────────────────────────────────┐");
        System.out.printf("│ Current Player: %s (%s)                             │\n",
            state.getCurrentPlayer(), state.getCurrentPlayer().getSymbol());
        System.out.printf("│ WHITE pieces: On board: %d | Exited: %d                │\n",
            state.getBoard().countPieces(Player.WHITE),
            state.getPiecesExited(Player.WHITE));
        System.out.printf("│ BLACK pieces: On board: %d | Exited: %d                │\n",
            state.getBoard().countPieces(Player.BLACK),
            state.getPiecesExited(Player.BLACK));
        System.out.println("└─────────────────────────────────────────────────────────┘");
    }

    /**
     * Print move details
     */
    public static void printMove(Move move, int roll) {
        System.out.println("\n>>> " + move);
        System.out.println("    Roll was: " + roll);
    }

    /**
     * Print legal moves
     */
    public static void printLegalMoves(List<Move> moves) {
        System.out.println("\n--- Legal Moves ---");
        for (int i = 0; i < moves.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, moves.get(i));
        }
    }
}
