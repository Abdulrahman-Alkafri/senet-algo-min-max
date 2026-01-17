package game;

import models.*;
import java.util.ArrayList;
import java.util.List;

public class GameRules {

    /**
     * Get all legal moves for current player given a roll
     */
    public static List<Move> getLegalMoves(GameState state, int roll) {
        List<Move> legalMoves = new ArrayList<>();
        Player currentPlayer = state.getCurrentPlayer();
        Board board = state.getBoard();

        int[] piecePositions = board.getPiecePositions(currentPlayer);

        for (int fromPos : piecePositions) {

            int toPos = fromPos + roll;

            // Special handling for last 5 squares
            if (fromPos >= 26) {
                Move exitMove = checkSpecialSquareExit(state, fromPos, roll);
                if (exitMove != null) {
                    legalMoves.add(exitMove);
                    continue;
                }
            }

            // Special handling for moves involving square 26 (House of Happiness)
            if (toPos == 26) {
                // House of Happiness - must land exactly
                if(board.isEmpty(toPos))
                legalMoves.add(new Move(fromPos, toPos, currentPlayer, false, false));
                else
                    legalMoves.add(new Move(fromPos, toPos, currentPlayer, true, false));
            } else if (toPos > 26 && fromPos < 26) {
                // Cannot move past square 26 from positions before 26
                // Must land exactly on 26 first
                // This move is illegal
            } else if (toPos > 30) {
                // Exit the board
                legalMoves.add(new Move(fromPos, toPos, currentPlayer, false, true));
            } else if (fromPos == 26 && toPos > 26) {
                // From square 26, can move forward (exit or to empty square)
                if (board.isEmpty(toPos)) {
                    legalMoves.add(new Move(fromPos, toPos, currentPlayer, false, false));
                } else if (board.getPieceAt(toPos) == currentPlayer.opponent()) {
                    // Swap with opponent piece
                    legalMoves.add(new Move(fromPos, toPos, currentPlayer, true, false));
                }
            } else if (board.isEmpty(toPos)) {
                // Move to empty square
                legalMoves.add(new Move(fromPos, toPos, currentPlayer, false, false));
            } else if (board.getPieceAt(toPos) == currentPlayer.opponent()) {
                // Swap with opponent piece
                legalMoves.add(new Move(fromPos, toPos, currentPlayer, true, false));
            }
            // If occupied by own piece, move is illegal (not added)
        }

        return legalMoves;
    }

    /**
     * Check special square exit conditions (squares 26, 28, 29, 30)
     */
    private static Move checkSpecialSquareExit(GameState state, int position, int roll) {
        Player player = state.getCurrentPlayer();

        switch (position) {
            case 26: // House of Happiness - must land exactly, but can exit if roll takes beyond 30
                int targetPos = position + roll;
                if (targetPos > 30) {

                    return new Move(position, targetPos, player, false, true); // Exit
                } else {
                    // If moving within bounds, it's a normal move handled elsewhere
                    return null;
                }

            case 28: // House of Three Truths - need exactly 3 to exit
                if (roll == 3) {
                    return new Move(position, 31, player, false, true);
                } else {
                    // If roll is not 3, return to House of Rebirth (square 15)
                    return new Move(position, 15, player, false, false);
                }

            case 29: // House of Re-Atoum - need exactly 2 to exit
                if (roll == 2) {
                    return new Move(position, 31, player, false, true);
                } else {
                    // If roll is not 2, return to House of Rebirth (square 15)
                    return new Move(position, 15, player, false, false);
                }

            case 30: // House of Horus - any roll exits
                return new Move(position, 31, player, false, true);
        }

        return null;
    }

    /**
     * Apply a move to the game state (returns new state)
     */
    public static GameState applyMove(GameState state, Move move) {
        GameState newState = state.clone();
        Board board = newState.getBoard();

        int fromPos = move.getFromPosition();
        int toPos = move.getToPosition();
        Player player = move.getPlayer();

        if (move.isExit()) {
            // Remove piece from board (exit)
            board.removePieceAt(fromPos);
            newState.incrementPiecesExited(player);

            // Check win condition
            if (newState.getPiecesExited(player) == 7) {
                newState.setGameOver(player);
            }
        } else if (move.isSwap()) {
            // Swap pieces
            Player opponent = player.opponent();
            board.setPieceAt(fromPos, opponent);
            board.setPieceAt(toPos, player);
        } else {
            // Normal move
            board.removePieceAt(fromPos);
            board.setPieceAt(toPos, player);

            // Check special square effects
            applySpecialSquareEffects(newState, toPos, player);
        }

        return newState;
    }

    /**
     * Apply effects of special squares
     */
    private static void applySpecialSquareEffects(GameState state, int position, Player player) {
        Board board = state.getBoard();

        switch (position) {
            case 27: // House of Water - return to House of Rebirth
                board.removePieceAt(27);
                // Send to square 15 (Rebirth) or first empty before it
                int rebirthPos = findRebirthPosition(board);
                board.setPieceAt(rebirthPos, player);
                break;

            // Square 15 (Rebirth) is destination, not effect trigger
            // Square 26 (Happiness) requires exact landing, handled in getLegalMoves
        }
    }

    /**
     * Find rebirth position (square 15 or first empty before it)
     */
    private static int findRebirthPosition(Board board) {
        if (board.isEmpty(15)) {
            return 15;
        }
        // Find first empty square before 15
        for (int i = 14; i >= 1; i--) {
            if (board.isEmpty(i)) {
                return i;
            }
        }
        return 1; // Fallback (shouldn't happen in normal game)
    }

    /**
     * Check if state is terminal
     */
    public static boolean isTerminalState(GameState state) {
        return state.isGameOver();
    }

    /**
     * Check if a player can make any move with given roll
     */
    public static boolean hasLegalMoves(GameState state, int roll) {
        return !getLegalMoves(state, roll).isEmpty();
    }
}
