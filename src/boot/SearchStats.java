package boot;

public class SearchStats {
    private int nodesExplored;
    private int maxNodes;
    private int minNodes;
    private int chanceNodes;
    private int maxDepthReached;
    private long startTime;
    private long endTime;

    public SearchStats() {
        reset();
    }

    public void reset() {
        nodesExplored = 0;
        maxNodes = 0;
        minNodes = 0;
        chanceNodes = 0;
        maxDepthReached = 0;
        startTime = System.currentTimeMillis();
    }

    public void incrementNode(NodeType type) {
        nodesExplored++;
        switch (type) {
            case MAX: maxNodes++; break;
            case MIN: minNodes++; break;
            case CHANCE: chanceNodes++; break;
        }
    }

    public void updateMaxDepth(int depth) {
        if (depth > maxDepthReached) {
            maxDepthReached = depth;
        }
    }

    public void endSearch() {
        endTime = System.currentTimeMillis();
    }

    public void printStats() {
        System.out.println("\n╔════════════════ SEARCH STATISTICS ════════════════╗");
        System.out.printf("║ Total nodes explored: %-27d ║\n", nodesExplored);
        System.out.printf("║   - MAX nodes:        %-27d ║\n", maxNodes);
        System.out.printf("║   - MIN nodes:        %-27d ║\n", minNodes);
        System.out.printf("║   - CHANCE nodes:     %-27d ║\n", chanceNodes);
        System.out.printf("║ Max depth reached:    %-27d ║\n", maxDepthReached);
        System.out.printf("║ Time taken:           %-24d ms ║\n", (endTime - startTime));
        System.out.println("╚═══════════════════════════════════════════════════╝");
    }

    // Getters
    public int getNodesExplored() {
        return nodesExplored;
    }

    public long getTimeTaken() {
        return endTime - startTime;
    }
}
