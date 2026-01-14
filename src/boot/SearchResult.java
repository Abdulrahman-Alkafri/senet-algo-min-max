package boot;

import models.Move;

public class SearchResult {
    private final double value;
    private final Move bestMove;
    private final NodeType nodeType;

    public SearchResult(double value, Move bestMove, NodeType nodeType) {
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

    public NodeType getNodeType() {
        return nodeType;
    }

    @Override
    public String toString() {
        return String.format("SearchResult[value=%.3f, move=%s, type=%s]",
            value, bestMove, nodeType);
    }
}
