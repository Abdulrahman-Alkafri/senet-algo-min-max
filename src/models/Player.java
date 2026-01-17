package models;

public enum Player {
    WHITE(0, "●"),
    BLACK(1, "○");

    private final int id;
    private final String symbol;

    Player(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }

    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public Player opponent() {
        return this == WHITE ? BLACK : WHITE;
    }
}
