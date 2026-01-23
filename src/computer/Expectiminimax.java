package computer;

import models.Player;
import models.GameState;
import models.Move;
import game.GameRules;
import game.SticksManager;
import java.util.List;

public class Expectiminimax {

    private final Player computerPlayer;
    private final GameStats stats;

    public Expectiminimax(Player computerPlayer) {
        this.computerPlayer = computerPlayer;
        this.stats = new GameStats();
    }

    /**
     * Find best move for given state and roll
     */
    public Move getBestMove(GameState state, int roll, int maxDepth) {
        stats.reset();

        List<Move> legalMoves = GameRules.getLegalMoves(state, roll);

        if (legalMoves.isEmpty()) return null;

        if (legalMoves.size() == 1) return legalMoves.get(0);

        Move bestMove = null;
        double bestValue = Double.NEGATIVE_INFINITY;

        for (Move move : legalMoves) {
            GameState nextState = GameRules.applyMove(state, move);
            nextState.switchPlayer();

            double value = chanceNode(nextState, maxDepth - 1,
                                     Double.NEGATIVE_INFINITY,
                                     Double.POSITIVE_INFINITY, false);

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        stats.endSearch();

        return bestMove;
    }

    /**
     * CHANCE node - calculate expected value over all dice rolls
     */
    private double chanceNode(GameState state, int depth,
                             double alpha, double beta, boolean isMaxPlayer) {
        stats.incrementNode(TurnType.CHANCE);
        stats.updateMaxDepth(depth);

        if (depth == 0 || GameRules.isTerminalState(state)) {
            return Heuristic.evaluate(state, computerPlayer);
        }

        double expectedValue = 0;
        int[] possibleRolls = SticksManager.getAllPossibleRolls();

        for (int roll : possibleRolls) {
            double probability = SticksManager.getProbability(roll);
            double value;

            if (isMaxPlayer) {
                value = maxNode(state, depth, alpha, beta, roll);
            } else {
                value = minNode(state, depth, alpha, beta, roll);
            }

            expectedValue += probability * value;
        }

        return expectedValue;
    }

    private double maxNode(GameState state, int depth,
                          double alpha, double beta, int roll) {
        stats.incrementNode(TurnType.MAX);

        List<Move> legalMoves = GameRules.getLegalMoves(state, roll);

        if (legalMoves.isEmpty()) {
            GameState nextState = state.clone();
            nextState.switchPlayer();
            return chanceNode(nextState, depth - 1, alpha, beta, false);
        }

        double maxValue = Double.NEGATIVE_INFINITY;

        for (Move move : legalMoves) {
            GameState nextState = GameRules.applyMove(state, move);

            if (GameRules.isTerminalState(nextState)) {
                return Heuristic.evaluate(nextState, computerPlayer);
            }

            nextState.switchPlayer();
            double value = chanceNode(nextState, depth - 1, alpha, beta, false);

            maxValue = Math.max(maxValue, value);
            alpha = Math.max(alpha, value);

            if (beta <= alpha) {
                break;
            }
        }

        return maxValue;
    }

    private double minNode(GameState state, int depth,
                          double alpha, double beta, int roll) {
        stats.incrementNode(TurnType.MIN);

        List<Move> legalMoves = GameRules.getLegalMoves(state, roll);

        if (legalMoves.isEmpty()) {
            GameState nextState = state.clone();
            nextState.switchPlayer();
            return chanceNode(nextState, depth - 1, alpha, beta, true);
        }

        double minValue = Double.POSITIVE_INFINITY;

        for (Move move : legalMoves) {
            GameState nextState = GameRules.applyMove(state, move);

            if (GameRules.isTerminalState(nextState)) {
                return Heuristic.evaluate(nextState, computerPlayer);
            }

            nextState.switchPlayer();
            double value = chanceNode(nextState, depth - 1, alpha, beta, true);

            minValue = Math.min(minValue, value);
            beta = Math.min(beta, value);

            if (beta <= alpha) {
                break;
            }
        }

        return minValue;
    }

    public GameStats getStats() {
        return stats;
    }
}
