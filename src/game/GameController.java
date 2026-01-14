package game;

import models.Player;
import models.GameState;
import models.Move;
import boot.Expectiminimax;
import java.util.List;
import java.util.Scanner;

public class GameController {
    private GameState state;
    private final int searchDepth;
    private final boolean verbose;
    private final Player humanPlayer;
    private final Player bootPlayer;
    private final Scanner scanner;

    public GameController(int searchDepth, boolean verbose, boolean aiFirst) {
        this.state = new GameState();
        this.searchDepth = searchDepth;
        this.verbose = verbose;
        this.scanner = new Scanner(System.in);

        if (aiFirst) {
            this.bootPlayer = Player.WHITE;
            this.humanPlayer = Player.BLACK;
        } else {
            this.humanPlayer = Player.WHITE;
            this.bootPlayer = Player.BLACK;
        }
    }

    public void playGame() {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           WELCOME TO SENET - THE ANCIENT GAME          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("\nHuman: " + humanPlayer.getSymbol() +
                          " | Boot: " + bootPlayer.getSymbol());
        System.out.println("Search Depth: " + searchDepth);

        while (!state.isGameOver()) {
            BoardDisplay.printBoard(state);

            // Throw sticks
            SticksManager.ThrowResult throwResult = SticksManager.throwSticksWithDisplay();
            int roll = throwResult.getValue();
            System.out.println("\n" + throwResult);

            if (state.getCurrentPlayer() == humanPlayer) {
                humanTurn(roll);
            } else {
                bootTurn(roll);
            }
        }

        // Game over
        gameOver();
    }

    private void humanTurn(int roll) {
        List<Move> legalMoves = GameRules.getLegalMoves(state, roll);

        if (legalMoves.isEmpty()) {
            System.out.println("\n>>> No legal moves available. Turn skipped.");
            state.switchPlayer();
            pause();
            return;
        }

        BoardDisplay.printLegalMoves(legalMoves);

        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("\nSelect move (1-" + legalMoves.size() + "): ");
            try {
                choice = scanner.nextInt();

                if (choice < 1 || choice > legalMoves.size()) {
                    System.out.println("Invalid input, please re-enter a number from the upper list of moves");
                } else {
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please re-enter a number from the upper list of moves");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        Move selectedMove = legalMoves.get(choice - 1);
        BoardDisplay.printMove(selectedMove, roll);

        state = GameRules.applyMove(state, selectedMove);
        state.switchPlayer();
    }

    private void bootTurn(int roll) {
        System.out.println("\n>>> Boot is thinking...");

        Expectiminimax boot = new Expectiminimax(bootPlayer, verbose);
        Move bestMove = boot.getBestMove(state, roll, searchDepth);

        if (bestMove == null) {
            System.out.println("\n>>> Boot has no legal moves. Turn skipped.");
            state.switchPlayer();
            pause();
            return;
        }

        BoardDisplay.printMove(bestMove, roll);

        if (verbose) {
            boot.getStats().printStats();
        }

        state = GameRules.applyMove(state, bestMove);
        state.switchPlayer();

        pause();
    }

    private void gameOver() {
        BoardDisplay.printBoard(state);

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                    GAME OVER!                          ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║  Winner: %-45s ║\n",
            state.getWinner() + " " + state.getWinner().getSymbol());
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }

    private void pause() {
        System.out.print("\nPress Enter to continue...");
        try {
            scanner.nextLine();
        } catch (Exception e) {
            // Handle exception
        }
    }
}
