package models;

public class Move {
    private final int fromPosition;
    private final int toPosition;
    private final Player player;
    private final boolean isSwap;
    private final boolean isExit;

    public Move(int fromPosition, int toPosition, Player player, boolean isSwap, boolean isExit) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
        this.player = player;
        this.isSwap = isSwap;
        this.isExit = isExit;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public int getToPosition() {
        return toPosition;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isSwap() {
        return isSwap;
    }

    public boolean isExit() {
        return isExit;
    }

    @Override
    public String toString() {
        String action = isExit ? "EXIT" : (isSwap ? "SWAP" : "MOVE");
        return String.format("%s: %s from %d to %d", action, player.getSymbol(), fromPosition, toPosition);
    }
}
