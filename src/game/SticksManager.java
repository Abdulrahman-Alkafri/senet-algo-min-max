package game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SticksManager {
    private static final Random random = new Random();
    private static final Map<Integer, Double> PROBABILITIES = new HashMap<>();

    static {
        PROBABILITIES.put(1, 0.25);
        PROBABILITIES.put(2, 0.375);
        PROBABILITIES.put(3, 0.25);
        PROBABILITIES.put(4, 0.0625);
        PROBABILITIES.put(5, 0.0625);
    }

    public static double getProbability(int roll) {
        return PROBABILITIES.getOrDefault(roll, 0.0);
    }

    public static int[] getAllPossibleRolls() {
        return new int[]{1, 2, 3, 4, 5};
    }

    public static int throwSticks() {
        int darkCount = 0;

        for (int i = 0; i < 4; i++) {
            if (random.nextBoolean()) {
                darkCount++;
            }
        }

        return darkCount == 0 ? 5 : darkCount;
    }

    public static ThrowResult throwSticksWithDisplay() {
        boolean[] sticks = new boolean[4];
        int darkCount = 0;

        for (int i = 0; i < 4; i++) {
            sticks[i] = random.nextBoolean();
            if (sticks[i]) darkCount++;
        }

        int rollValue = darkCount == 0 ? 5 : darkCount;
        return new ThrowResult(rollValue, sticks);
    }

    public static Map<Integer, Double> getProbabilityDistribution() {
        return new HashMap<>(PROBABILITIES);
    }

    public static class ThrowResult {
        private final int value;
        private final boolean[] sticks;

        public ThrowResult(int value, boolean[] sticks) {
            this.value = value;
            this.sticks = sticks;
        }

        public int getValue() {
            return value;
        }

        public boolean[] getSticks() {
            return sticks;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Sticks: [");
            for (int i = 0; i < sticks.length; i++) {
                sb.append(sticks[i] ? "□" :  "■");
                if (i < sticks.length - 1) sb.append(" ");
            }
            sb.append("] → Roll: ").append(value);
            return sb.toString();
        }
    }
}
