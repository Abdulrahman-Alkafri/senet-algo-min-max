package computer;

import models.Move;

public class GameResult {
    private final double value;
    private final Move bestMove;

    public GameResult(double value, Move bestMove) {
        this.value = value;
        this.bestMove = bestMove;
    }

    public double getValue() {
        return value;
    }

    public Move getBestMove() {
        return bestMove;
    }

    @Override
    public String toString() {
        return String.format("GameResult[value=%.3f, move=%s]",
            value, bestMove);
    }
}
