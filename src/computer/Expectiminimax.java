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
    private final boolean verbose;

    public Expectiminimax(Player computerPlayer, boolean verbose) {
        this.computerPlayer = computerPlayer;
        this.stats = new GameStats();
        this.verbose = verbose;
    }

    /**
     * Find best move for given state and roll
     */
    public Move getBestMove(GameState state, int roll, int maxDepth) {
        stats.reset();

        List<Move> legalMoves = GameRules.getLegalMoves(state, roll);

        if (legalMoves.isEmpty()) {
            return null;
        }

        if (legalMoves.size() == 1) {
            return legalMoves.get(0);
        }

        Move bestMove = null;
        double bestValue = Double.NEGATIVE_INFINITY;

        if (verbose) {
            System.out.println("\n=== Expectiminimax Search ===");
            System.out.println("Evaluating " + legalMoves.size() + " moves...");
        }

        for (Move move : legalMoves) {
            GameState nextState = GameRules.applyMove(state, move);
            nextState.switchPlayer();

            // Call chance node (opponent will roll dice)
            double value = chanceNode(nextState, maxDepth - 1,
                                     Double.NEGATIVE_INFINITY,
                                     Double.POSITIVE_INFINITY, false);

            if (verbose) {
                System.out.printf("  Move: %s → Value: %.3f\n", move, value);
            }

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        stats.endSearch();

        if (verbose) {
            System.out.printf("\nBest move selected: %s (value: %.3f)\n",
                             bestMove, bestValue);
            stats.printStats();
        }

        return bestMove;
    }

    /**
     * CHANCE node - calculate expected value over all dice rolls
     */
    private double chanceNode(GameState state, int depth,
                             double alpha, double beta, boolean isMaxPlayer) {
        stats.incrementNode(TurnType.CHANCE);
        stats.updateMaxDepth(depth);

        // Terminal check
        if (depth == 0 || GameRules.isTerminalState(state)) {
            return Heuristic.evaluate(state, computerPlayer);
        }

        // Calculate expected value over all possible rolls
        double expectedValue = 0.0;
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

    /**
     * MAX node - Computer player's turn (maximize)
     */
    private double maxNode(GameState state, int depth,
                          double alpha, double beta, int roll) {
        stats.incrementNode(TurnType.MAX);

        List<Move> legalMoves = GameRules.getLegalMoves(state, roll);

        // No legal moves - skip turn
        if (legalMoves.isEmpty()) {
            GameState nextState = state.clone();
            nextState.switchPlayer();
            return chanceNode(nextState, depth - 1, alpha, beta, false);
        }

        double maxValue = Double.NEGATIVE_INFINITY;

        for (Move move : legalMoves) {
            GameState nextState = GameRules.applyMove(state, move);

            // Terminal state check
            if (GameRules.isTerminalState(nextState)) {
                return Heuristic.evaluate(nextState, computerPlayer);
            }

            nextState.switchPlayer();
            double value = chanceNode(nextState, depth - 1, alpha, beta, false);

            maxValue = Math.max(maxValue, value);
            alpha = Math.max(alpha, value);

            // Alpha-beta pruning
            if (beta <= alpha) {
                break;
            }
        }

        return maxValue;
    }

    /**
     * MIN node - Opponent's turn (minimize)
     */
    private double minNode(GameState state, int depth,
                          double alpha, double beta, int roll) {
        stats.incrementNode(TurnType.MIN);

        List<Move> legalMoves = GameRules.getLegalMoves(state, roll);

        // No legal moves - skip turn
        if (legalMoves.isEmpty()) {
            GameState nextState = state.clone();
            nextState.switchPlayer();
            return chanceNode(nextState, depth - 1, alpha, beta, true);
        }

        double minValue = Double.POSITIVE_INFINITY;

        for (Move move : legalMoves) {
            GameState nextState = GameRules.applyMove(state, move);

            // Terminal state check
            if (GameRules.isTerminalState(nextState)) {
                return Heuristic.evaluate(nextState, computerPlayer);
            }

            nextState.switchPlayer();
            double value = chanceNode(nextState, depth - 1, alpha, beta, true);

            minValue = Math.min(minValue, value);
            beta = Math.min(beta, value);

            // Alpha-beta pruning
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
