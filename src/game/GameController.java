package game;

import models.Player;
import models.GameState;
import models.Move;
import ai.Expectiminimax;
import java.util.List;
import java.util.Scanner;

public class GameController {
    private GameState state;
    private final int searchDepth;
    private final boolean verbose;
    private final Player humanPlayer;
    private final Player aiPlayer;
    private final Scanner scanner;

    public GameController(int searchDepth, boolean verbose, boolean aiFirst) {
        this.state = new GameState();
        this.searchDepth = searchDepth;
        this.verbose = verbose;
        this.scanner = new Scanner(System.in);

        if (aiFirst) {
            this.aiPlayer = Player.WHITE;
            this.humanPlayer = Player.BLACK;
        } else {
            this.humanPlayer = Player.WHITE;
            this.aiPlayer = Player.BLACK;
        }
    }

    public void playGame() {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           WELCOME TO SENET - THE ANCIENT GAME          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("\nHuman: " + humanPlayer.getSymbol() +
                          " | AI: " + aiPlayer.getSymbol());
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
                aiTurn(roll);
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

        System.out.print("\nSelect move (1-" + legalMoves.size() + "): ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > legalMoves.size()) {
            System.out.println("Invalid choice. Turn skipped.");
            state.switchPlayer();
            return;
        }

        Move selectedMove = legalMoves.get(choice - 1);
        BoardDisplay.printMove(selectedMove, roll);

        state = GameRules.applyMove(state, selectedMove);
        state.switchPlayer();
    }

    private void aiTurn(int roll) {
        System.out.println("\n>>> AI is thinking...");

        Expectiminimax ai = new Expectiminimax(aiPlayer, verbose);
        Move bestMove = ai.getBestMove(state, roll, searchDepth);

        if (bestMove == null) {
            System.out.println("\n>>> AI has no legal moves. Turn skipped.");
            state.switchPlayer();
            pause();
            return;
        }

        BoardDisplay.printMove(bestMove, roll);

        if (verbose) {
            ai.getStats().printStats();
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
