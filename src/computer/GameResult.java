package computer;

import models.Move;

public class GameResult {
    private final double value;
    private final Move bestMove;
    private final TurnType nodeType;

    public GameResult(double value, Move bestMove, TurnType nodeType) {
        this.value = value;
        this.bestMove = bestMove;
        this.nodeType = nodeType;
    }

    public double getValue() {
        return value;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public TurnType getNodeType() {
        return nodeType;
    }

    @Override
    public String toString() {
        return String.format("GameResult[value=%.3f, move=%s, type=%s]",
            value, bestMove, nodeType);
    }
}
