package boot;

import models.*;
import game.GameRules;

public class Heuristic {

    // Weights for evaluation
    private static final double PIECE_EXITED_WEIGHT = 1000.0;
    private static final double PIECE_ADVANCEMENT_WEIGHT = 10.0;
    private static final double PIECE_SAFETY_WEIGHT = 5.0;
    private static final double SPECIAL_SQUARE_WEIGHT = 20.0;
    private static final double OPPONENT_BLOCKING_WEIGHT = 15.0;

    /**
     * Evaluate game state from Boot player's perspective
     * Higher value = better for Boot
     */
    public static double evaluate(GameState state, Player bootPlayer) {
        // Terminal state check
        if (GameRules.isTerminalState(state)) {
            if (state.getWinner() == bootPlayer) {
                return Double.POSITIVE_INFINITY;
            } else {
                return Double.NEGATIVE_INFINITY;
            }
        }

        double score = 0.0;

        // 1. Pieces exited (most important)
        score += (state.getPiecesExited(bootPlayer) -
                  state.getPiecesExited(bootPlayer.opponent()))
                  * PIECE_EXITED_WEIGHT;

        // 2. Piece advancement
        score += calculateAdvancementScore(state, bootPlayer);

        // 3. Piece safety
        score += calculateSafetyScore(state, bootPlayer);

        // 4. Special square control
        score += calculateSpecialSquareScore(state, bootPlayer);

        // 5. Opponent blocking
        score += calculateBlockingScore(state, bootPlayer);

        return score;
    }

    /**
     * Calculate score based on how far pieces have advanced
     */
    private static double calculateAdvancementScore(GameState state, Player player) {
        Board board = state.getBoard();
        int[] positions = board.getPiecePositions(player);
        int[] opponentPositions = board.getPiecePositions(player.opponent());

        double playerSum = 0;
        for (int pos : positions) {
            // Pieces closer to exit are more valuable
            playerSum += pos * pos; // Quadratic to emphasize advancement
        }

        double opponentSum = 0;
        for (int pos : opponentPositions) {
            opponentSum += pos * pos;
        }

        return (playerSum - opponentSum) * PIECE_ADVANCEMENT_WEIGHT;
    }

    /**
     * Calculate safety score (pieces past opponent pieces are safer)
     */
    private static double calculateSafetyScore(GameState state, Player player) {
        Board board = state.getBoard();
        int[] positions = board.getPiecePositions(player);

        double safetyScore = 0;
        for (int pos : positions) {
            // Pieces in last 5 squares are very safe (can't be swapped easily)
            if (pos >= 26) {
                safetyScore += 3.0;
            }
            // Pieces past square 15 are safer
            else if (pos > 15) {
                safetyScore += 1.0;
            }
        }

        return safetyScore * PIECE_SAFETY_WEIGHT;
    }

    /**
     * Score for controlling special squares
     */
    private static double calculateSpecialSquareScore(GameState state, Player player) {
        Board board = state.getBoard();
        double score = 0;

        // Avoid water (27)
        if (board.getPieceAt(27) == player) {
            score -= 50.0; // Penalty for being on water
        }

        // Reward for being close to exit (28, 29, 30)
        if (board.getPieceAt(28) == player) score += 30.0;
        if (board.getPieceAt(29) == player) score += 35.0;
        if (board.getPieceAt(30) == player) score += 40.0;

        // Reward for passing happiness (26)
        if (board.getPieceAt(26) == player) score += 25.0;

        return score * SPECIAL_SQUARE_WEIGHT;
    }

    /**
     * Score for blocking opponent
     */
    private static double calculateBlockingScore(GameState state, Player player) {
        Board board = state.getBoard();
        int[] opponentPositions = board.getPiecePositions(player.opponent());

        double blockingScore = 0;

        // Check if we're ahead of opponent pieces (blocking them)
        int[] playerPositions = board.getPiecePositions(player);
        for (int oppPos : opponentPositions) {
            for (int myPos : playerPositions) {
                if (myPos > oppPos && myPos - oppPos <= 5) {
                    blockingScore += 1.0;
                }
            }
        }

        return blockingScore * OPPONENT_BLOCKING_WEIGHT;
    }
}
