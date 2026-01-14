package game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SticksManager {
    private static final Random random = new Random();
    private static final Map<Integer, Double> PROBABILITIES = new HashMap<>();

    static {
        // Mathematical calculation:
        // 4 sticks, each: light (0) or dark (1)
        // Total combinations: 2^4 = 16
        //
        // Roll value | Dark sticks | Combinations | Probability
        // -----------|-------------|--------------|------------
        //     1      |      1      |      4       |   4/16 = 0.25
        //     2      |      2      |      6       |   6/16 = 0.375
        //     3      |      3      |      4       |   4/16 = 0.25
        //     4      |      4      |      1       |   1/16 = 0.0625
        //     5      |      0      |      1       |   1/16 = 0.0625

        PROBABILITIES.put(1, 0.25);
        PROBABILITIES.put(2, 0.375);
        PROBABILITIES.put(3, 0.25);
        PROBABILITIES.put(4, 0.0625);
        PROBABILITIES.put(5, 0.0625);
    }

    /**
     * Calculate probability for a specific roll value
     * @param roll Roll value (1-5)
     * @return Probability of this roll
     */
    public static double getProbability(int roll) {
        return PROBABILITIES.getOrDefault(roll, 0.0);
    }

    /**
     * Get all possible roll values
     * @return Array of possible rolls [1, 2, 3, 4, 5]
     */
    public static int[] getAllPossibleRolls() {
        return new int[]{1, 2, 3, 4, 5};
    }

    /**
     * Simulate throwing 4 sticks
     * @return Roll value (1-5)
     */
    public static int throwSticks() {
        int darkCount = 0;

        // Simulate 4 sticks
        for (int i = 0; i < 4; i++) {
            if (random.nextBoolean()) { // true = dark, false = light
                darkCount++;
            }
        }

        // Convert: 0 dark sticks → roll 5, otherwise roll = darkCount
        return darkCount == 0 ? 5 : darkCount;
    }

    /**
     * Throw sticks with visualization
     * @return Roll value and stick representation
     */
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

    /**
     * Get probability distribution as a map
     */
    public static Map<Integer, Double> getProbabilityDistribution() {
        return new HashMap<>(PROBABILITIES);
    }

    // Inner class for detailed throw result
    public static class ThrowResult {
        private final int value;
        private final boolean[] sticks; // true = dark, false = light

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
