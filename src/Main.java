import game.GameController;

public class Main {

    public static void main(String[] args) {
        // Default values
        int depth = 3;
        boolean verbose = false;
        boolean aiFirst = false;

        // Parse command line argumentsi
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--depth":
                case "-d":
                    if (i + 1 < args.length) {
                        depth = Integer.parseInt(args[++i]);
                    }
                    break;

                case "--verbose":
                case "-v":
                    verbose = true;
                    break;

                case "--ai-first":
                case "-a":
                    aiFirst = true;
                    break;

                case "--help":
                case "-h":
                    printHelp();
                    return;
            }
        }

        // Validate depth
        if (depth < 1 || depth > 10) {
            System.out.println("Error: Depth must be between 1 and 10");
            return;
        }

        // Start game
        GameController game = new GameController(depth, verbose, aiFirst);
        game.playGame();
    }

    private static void printHelp() {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║          Senet Game - Boot Search Project               ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("\nUsage: java Main [options]");
        System.out.println("\nOptions:");
        System.out.println("  -d, --depth <n>      Set search depth (default: 3)");
        System.out.println("  -v, --verbose        Show detailed algorithm output");
        System.out.println("  -a, --ai-first       Boot plays first (default: human first)");
        System.out.println("  -h, --help           Show this help message");
        System.out.println("\nExamples:");
        System.out.println("  java Main --depth 4 --verbose");
        System.out.println("  java Main -d 5 -v -a");
        System.out.println("\n" +
            "╔════════════════════════════════════════════════════════╗\n" +
            "║  Special Squares:                                      ║\n" +
            "║    15 (☥) - House of Rebirth                           ║\n" +
            "║    26 (⚮) - House of Happiness (must land exactly)     ║\n" +
            "║    27 (≈) - House of Water (return to rebirth)         ║\n" +
            "║    28 (⚶) - House of Three Truths (need 3 to exit)     ║\n" +
            "║    29 (☉) - House of Re-Atoum (need 2 to exit)         ║\n" +
            "║    30 (⊙) - House of Horus (any roll exits)            ║\n" +
            "╚════════════════════════════════════════════════════════╝");
    }
}
