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

            if (fromPos >= 26) {
                Move exitMove = checkSpecialSquareExit(state, fromPos, roll);
                if (exitMove != null) {
                    legalMoves.add(exitMove);
                    continue;
                }
            }

            if (toPos == 26) {
                if(board.isEmpty(toPos))
                legalMoves.add(new Move(fromPos, toPos, currentPlayer, false, false));
                else
                    legalMoves.add(new Move(fromPos, toPos, currentPlayer, true, false));
            } else if (toPos > 26 && fromPos < 26) {
            } else if (toPos > 30) {
                legalMoves.add(new Move(fromPos, toPos, currentPlayer, false, true));
            } else if (fromPos == 26 && toPos > 26) {
                if (board.isEmpty(toPos)) {
                    legalMoves.add(new Move(fromPos, toPos, currentPlayer, false, false));
                } else if (board.getPieceAt(toPos) == currentPlayer.opponent()) {
                    legalMoves.add(new Move(fromPos, toPos, currentPlayer, true, false));
                }
            } else if (board.isEmpty(toPos)) {
                legalMoves.add(new Move(fromPos, toPos, currentPlayer, false, false));
            } else if (board.getPieceAt(toPos) == currentPlayer.opponent()) {
                legalMoves.add(new Move(fromPos, toPos, currentPlayer, true, false));
            }
        }

        return legalMoves;
    }

    /**
     * Check special square exit conditions (squares 26, 28, 29, 30)
     */
    private static Move checkSpecialSquareExit(GameState state, int position, int roll) {
        Player player = state.getCurrentPlayer();

        switch (position) {
            case 26:
                int targetPos = position + roll;
                if (targetPos > 30) {

                    return new Move(position, targetPos, player, false, true);
                } else {
                    return null;
                }

            case 28:
                if (roll == 3) {
                    return new Move(position, 31, player, false, true);
                } else {
                    return new Move(position, 15, player, false, false);
                }

            case 29:
                if (roll == 2) {
                    return new Move(position, 31, player, false, true);
                } else {
                    return new Move(position, 15, player, false, false);
                }

            case 30:
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
            board.removePieceAt(fromPos);
            newState.incrementPiecesExited(player);

            if (newState.getPiecesExited(player) == 7) {
                newState.setGameOver(player);
            }
        } else if (move.isSwap()) {
            Player opponent = player.opponent();
            board.setPieceAt(fromPos, opponent);
            board.setPieceAt(toPos, player);
        } else {
            board.removePieceAt(fromPos);
            board.setPieceAt(toPos, player);

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
            case 27:
                board.removePieceAt(27);
                int rebirthPos = findRebirthPosition(board);
                board.setPieceAt(rebirthPos, player);
                break;
        }
    }

    /**
     * Find rebirth position (square 15 or first empty before it)
     */
    private static int findRebirthPosition(Board board) {
        if (board.isEmpty(15))
            return 15;
        for (int i = 14; i >= 1; i--) {
            if (board.isEmpty(i))
                return i;
        }
        return 1;
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
