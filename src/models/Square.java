package models;

public enum Square {
    NORMAL(0, " "),
    REBIRTH(15, "☥"),        // House of Rebirth
    HAPPINESS(26, "⚮"),      // House of Happiness
    WATER(27, "≈"),          // House of Water
    THREE_TRUTHS(28, "⚶"),   // House of Three Truths
    RE_ATOUM(29, "☉"),       // House of Re-Atoum
    HORUS(30, "⊙");          // House of Horus

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
            if (sq.position == position) {
                return sq;
            }
        }
        return NORMAL;
    }

    public static boolean isSpecialSquare(int position) {
        return position >= 15 && (position == 15 || position >= 26);
    }
}
