package computer;

import models.*;
import game.GameRules;

public class Heuristic {

    private static final double PIECE_EXITED_WEIGHT = 50;
    private static final double PIECE_ADVANCEMENT_WEIGHT = 5;
    private static final double PIECE_SAFETY_WEIGHT = 2;
    private static final double SPECIAL_SQUARE_WEIGHT = 10;

    public static double evaluate(GameState state, Player computerPlayer) {
        if (GameRules.isTerminalState(state)) {
            if (state.getWinner() == computerPlayer) {
                return Double.POSITIVE_INFINITY;
            } else {
                return Double.NEGATIVE_INFINITY;
            }
        }

        double score = 0;

        score += (state.getPiecesExited(computerPlayer) -
                  state.getPiecesExited(computerPlayer.opponent()))
                  * PIECE_EXITED_WEIGHT;

        score += calculateAdvancementScore(state, computerPlayer);

        score += calculateSafetyScore(state, computerPlayer);

        score += calculateSpecialSquareScore(state, computerPlayer);

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
            playerSum += pos; // Linear advancement score
        }

        double opponentSum = 0;
        for (int pos : opponentPositions) {
            opponentSum += pos;
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
            if (pos >= 26) {
                safetyScore += 2;
            }
            else if (pos > 15) {
                safetyScore += 0.5;
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
            score -= 5; // Penalty for being on water
        }

        // Reward for being close to exit (28, 29, 30)
        if (board.getPieceAt(28) == player) score += 3;
        if (board.getPieceAt(29) == player) score += 4;
        if (board.getPieceAt(30) == player) score += 5;

        if (board.getPieceAt(26) == player) score += 4;

        return score * SPECIAL_SQUARE_WEIGHT;
    }
   }
