package models;

public enum Square {
    NORMAL(0, " "),
    REBIRTH(15, "☥"),
    HAPPINESS(26, "⚮"),
    WATER(27, "≈"),
    THREE_TRUTHS(28, "⚶"),
    RE_ATOUM(29, "☉"),
    HORUS(30, "⊙");

    private final int position;
    private final String symbol;

    Square(int position, String symbol) {
        this.position = position;
        this.symbol = symbol;
    }

    public int getPosition() {
        return position;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Square getSquareType(int position) {
        for (Square sq : values()) {
            if (sq.position == position)
                return sq;
        }
        return NORMAL;
    }

    public static boolean isSpecialSquare(int position) {
        return (position == 15 || position >= 26);
    }
}
