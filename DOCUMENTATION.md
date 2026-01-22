# Senet Game - Complete Code Documentation
## مشروع خوارزميات البحث الذكية (Intelligent Search Algorithms Project)

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Project Hierarchy](#project-hierarchy)
3. [Package Structure](#package-structure)
4. [Models Package](#models-package)
5. [Game Package](#game-package)
6. [Computer Package (AI Algorithm)](#computer-package-ai-algorithm)
7. [Utils Package](#utils-package)
8. [Main Entry Point](#main-entry-point)
9. [Execution Flow](#execution-flow)
10. [Expectiminimax Algorithm Detailed Explanation](#expectiminimax-algorithm-detailed-explanation)
11. [Heuristic Function Analysis](#heuristic-function-analysis)

---

## Project Overview

This project implements the ancient Egyptian board game **Senet** with an AI opponent using the **Expectiminimax Algorithm**. The game features:

- A 30-square board with special squares
- Two players (Human vs Computer)
- Dice rolling mechanism using 4 sticks
- AI that uses Expectiminimax with Alpha-Beta pruning

### What is Senet?
Senet is one of the oldest known board games, dating back to ancient Egypt (around 3100 BCE). The goal is to move all 7 pieces off the board before your opponent.

---

## Project Hierarchy

```
senet/
├── src/                              # Source code
│   ├── Main.java                     # Entry point
│   │
│   ├── models/                       # Data models
│   │   ├── Board.java               # Game board (30 squares)
│   │   ├── GameState.java           # Current game state
│   │   ├── Move.java                # Move representation
│   │   ├── Player.java              # Player enum (WHITE/BLACK)
│   │   └── Square.java              # Special squares enum
│   │
│   ├── game/                         # Game logic & UI
│   │   ├── GameController.java      # Main game loop
│   │   ├── GameRules.java           # Rules and move validation
│   │   ├── BoardDisplay.java        # Console UI
│   │   └── SticksManager.java       # Dice/sticks simulation
│   │
│   ├── computer/                     # AI Algorithm
│   │   ├── Expectiminimax.java      # Main AI algorithm
│   │   ├── Heuristic.java           # Board evaluation
│   │   ├── GameStats.java           # Search statistics
│   │   ├── GameResult.java          # Search result container
│   │   └── TurnType.java            # Node type enum
│   │
│   └── utils/                        # Utilities
│       └── Constants.java           # Game constants
│
├── bin/                              # Compiled classes
├── SenetGame.jar                     # Executable JAR
└── run.sh / run.bat                  # Run scripts
```

---

## Package Structure

| Package | Purpose | Files Count |
|---------|---------|-------------|
| `models` | Data structures representing game entities | 5 files |
| `game` | Game mechanics, rules, and user interface | 4 files |
| `computer` | AI algorithm implementation | 5 files |
| `utils` | Shared constants | 1 file |
| (default) | Application entry point | 1 file |

---

## Models Package

### 1. Player.java (Enum)
**Purpose:** Represents the two players in the game.

```java
public enum Player {
    WHITE(0, "●"),  // First player (Human by default)
    BLACK(1, "○");  // Second player (Computer by default)
}
```

**Attributes:**
| Attribute | Type | Description |
|-----------|------|-------------|
| `id` | int | Player identifier (0 or 1) |
| `symbol` | String | Display character (● or ○) |

**Methods:**
| Method | Returns | Description |
|--------|---------|-------------|
| `getId()` | int | Returns player ID |
| `getSymbol()` | String | Returns display symbol |
| `opponent()` | Player | Returns the opposite player |

**Usage Example:**
```java
Player current = Player.WHITE;
Player enemy = current.opponent(); // Returns BLACK
```

---

### 2. Board.java (Class)
**Purpose:** Represents the 30-square game board and manages piece positions.

**Attributes:**
| Attribute | Type | Description |
|-----------|------|-------------|
| `BOARD_SIZE` | int (static) | Board size = 30 |
| `squares` | Player[] | Array of 31 elements (index 0 unused, 1-30 for squares) |

**Constructor:**
```java
public Board()           // Creates new board with initial setup
public Board(Board other) // Copy constructor for cloning
```

**Initial Board Setup:**
- Squares 1-14: Alternating WHITE and BLACK pieces
  - Odd positions (1,3,5,7,9,11,13): WHITE ●
  - Even positions (2,4,6,8,10,12,14): BLACK ○
- Squares 15-30: Empty

**Methods:**
| Method | Parameters | Returns | Description |
|--------|------------|---------|-------------|
| `getPieceAt(int position)` | position (1-30) | Player or null | Gets piece at position |
| `setPieceAt(int position, Player player)` | position, player | void | Places piece at position |
| `removePieceAt(int position)` | position | void | Removes piece from position |
| `isEmpty(int position)` | position | boolean | Checks if square is empty |
| `getPiecePositions(Player player)` | player | int[] | Returns all positions of player's pieces |
| `countPieces(Player player)` | player | int | Counts player's pieces on board |
| `clone()` | - | Board | Creates deep copy of board |

---

### 3. GameState.java (Class)
**Purpose:** Encapsulates the complete state of the game at any moment.

**Attributes:**
| Attribute | Type | Description |
|-----------|------|-------------|
| `board` | Board | Current board configuration |
| `currentPlayer` | Player | Whose turn it is |
| `whitePiecesExited` | int | WHITE pieces that left the board |
| `blackPiecesExited` | int | BLACK pieces that left the board |
| `gameOver` | boolean | Is game finished |
| `winner` | Player | Who won (null if ongoing) |

**Constructor:**
```java
public GameState()            // New game state (WHITE starts)
public GameState(GameState other) // Copy constructor
```

**Methods:**
| Method | Parameters | Returns | Description |
|--------|------------|---------|-------------|
| `getBoard()` | - | Board | Returns current board |
| `getCurrentPlayer()` | - | Player | Returns current player |
| `switchPlayer()` | - | void | Switches to opponent's turn |
| `getPiecesExited(Player)` | player | int | Returns exited pieces count |
| `incrementPiecesExited(Player)` | player | void | Increases exit count |
| `isGameOver()` | - | boolean | Checks if game ended |
| `getWinner()` | - | Player | Returns winner |
| `setGameOver(Player)` | winner | void | Marks game as ended |
| `clone()` | - | GameState | Deep copy of state |
| `getPiecesRemaining(Player)` | player | int | Returns 7 - exited pieces |

---

### 4. Move.java (Class)
**Purpose:** Immutable representation of a single move.

**Attributes (all final):**
| Attribute | Type | Description |
|-----------|------|-------------|
| `fromPosition` | int | Starting square (1-30) |
| `toPosition` | int | Destination square (1-31+) |
| `player` | Player | Who makes the move |
| `isSwap` | boolean | Is this a swap with opponent |
| `isExit` | boolean | Does piece exit the board |

**Move Types:**
1. **MOVE**: Normal movement to empty square
2. **SWAP**: Exchange positions with opponent's piece
3. **EXIT**: Piece leaves the board (toPosition > 30)

**Constructor:**
```java
public Move(int fromPosition, int toPosition, Player player, boolean isSwap, boolean isExit)
```

**Methods:**
| Method | Returns | Description |
|--------|---------|-------------|
| `getFromPosition()` | int | Starting position |
| `getToPosition()` | int | Ending position |
| `getPlayer()` | Player | Moving player |
| `isSwap()` | boolean | Is swap move |
| `isExit()` | boolean | Is exit move |
| `toString()` | String | Human-readable format: "ACTION: symbol from X to Y" |

---

### 5. Square.java (Enum)
**Purpose:** Defines special squares on the board with their effects.

```java
public enum Square {
    NORMAL(0, " "),           // Regular square
    REBIRTH(15, "☥"),         // House of Rebirth
    HAPPINESS(26, "⚮"),       // House of Happiness
    WATER(27, "≈"),           // House of Water
    THREE_TRUTHS(28, "⚶"),    // House of Three Truths
    RE_ATOUM(29, "☉"),        // House of Re-Atoum
    HORUS(30, "⊙")            // House of Horus
}
```

**Special Squares Effects:**

| Square | Position | Symbol | Effect |
|--------|----------|--------|--------|
| House of Rebirth | 15 | ☥ | Destination when sent back from Water |
| House of Happiness | 26 | ⚮ | Must land exactly to pass |
| House of Water | 27 | ≈ | Piece sent back to Rebirth (15) |
| House of Three Truths | 28 | ⚶ | Need roll of 3 to exit, else return to 15 |
| House of Re-Atoum | 29 | ☉ | Need roll of 2 to exit, else return to 15 |
| House of Horus | 30 | ⊙ | Any roll allows exit |

**Methods:**
| Method | Returns | Description |
|--------|---------|-------------|
| `getPosition()` | int | Square position |
| `getSymbol()` | String | Unicode symbol |
| `getSquareType(int position)` | Square | Get square type for position |
| `isSpecialSquare(int position)` | boolean | Is position special |

---

## Game Package

### 1. GameController.java (Class)
**Purpose:** Main controller that orchestrates the game flow.

**Attributes:**
| Attribute | Type | Description |
|-----------|------|-------------|
| `state` | GameState | Current game state |
| `searchDepth` | int (final) | AI search depth |
| `verbose` | boolean (final) | Show detailed output |
| `humanPlayer` | Player (final) | Human's color |
| `computerPlayer` | Player (final) | Computer's color |
| `scanner` | Scanner (final) | Input reader |

**Constructor:**
```java
public GameController(int searchDepth, boolean verbose, boolean aiFirst)
```
- `searchDepth`: How deep the AI searches (default: 6)
- `verbose`: Show algorithm details
- `aiFirst`: If true, computer plays WHITE (first)

**Methods:**

#### `playGame()`
Main game loop that runs until game ends.

```
Algorithm:
1. Display welcome message
2. WHILE game not over:
   a. Display board
   b. Roll sticks (get random 1-5)
   c. IF current player is human:
      - Call humanTurn(roll)
   d. ELSE:
      - Call computerTurn(roll)
3. Call gameOver() to display results
```

#### `humanTurn(int roll)`
Handles human player's turn.

```
Algorithm:
1. Get legal moves for current roll
2. IF no legal moves:
   a. Print "No legal moves"
   b. Switch player
   c. Return
3. Display legal moves list
4. WHILE input not valid:
   a. Prompt for move selection (1-N)
   b. Validate input
5. Apply selected move
6. Check if game over
7. Switch player
```

#### `computerTurn(int roll)`
Handles AI player's turn.

```
Algorithm:
1. Print "Computer is thinking..."
2. Create Expectiminimax instance
3. Get best move from AI
4. IF no legal move:
   a. Print "No legal moves"
   b. Switch player
   c. Return
5. Display chosen move
6. IF verbose: print statistics
7. Apply move
8. Check if game over
9. Switch player
```

#### `gameOver()`
Displays game over message and winner.

#### `pause()`
Waits for Enter key press.

---

### 2. GameRules.java (Static Utility Class)
**Purpose:** Contains all game rules and move validation logic.

**Methods:**

#### `getLegalMoves(GameState state, int roll) → List<Move>`
**The most important method** - generates all valid moves.

```
Algorithm:
1. Get current player's piece positions
2. FOR each piece position:
   a. Calculate target = position + roll

   b. IF piece on special exit squares (26-30):
      - Check special exit conditions
      - Add exit move if valid
      - Continue to next piece

   c. IF target == 26 (House of Happiness):
      - Must land exactly
      - IF empty: add normal move
      - ELSE IF opponent: add swap move

   d. ELSE IF target > 26 AND from < 26:
      - Cannot pass 26 without landing on it
      - Move is ILLEGAL

   e. ELSE IF target > 30:
      - Add EXIT move

   f. ELSE IF from == 26 AND target > 26:
      - Can move forward from Happiness
      - Check if empty or swap possible

   g. ELSE IF target is empty:
      - Add normal MOVE

   h. ELSE IF target has opponent:
      - Add SWAP move

   i. IF target has own piece:
      - Move is ILLEGAL (blocked)

3. Return list of legal moves
```

#### `checkSpecialSquareExit(GameState state, int position, int roll) → Move`
Handles exit logic for last 5 squares.

| From Position | Condition | Result |
|---------------|-----------|--------|
| 26 (Happiness) | roll takes past 30 | EXIT |
| 28 (Three Truths) | roll == 3 | EXIT |
| 28 (Three Truths) | roll != 3 | Return to position 15 |
| 29 (Re-Atoum) | roll == 2 | EXIT |
| 29 (Re-Atoum) | roll != 2 | Return to position 15 |
| 30 (Horus) | any roll | EXIT |

#### `applyMove(GameState state, Move move) → GameState`
Applies a move and returns NEW state (immutable pattern).

```
Algorithm:
1. Clone current state
2. IF move is EXIT:
   a. Remove piece from board
   b. Increment pieces exited
   c. IF 7 pieces exited: game over, set winner
3. ELSE IF move is SWAP:
   a. Put opponent at fromPosition
   b. Put player at toPosition
4. ELSE (normal move):
   a. Remove from fromPosition
   b. Place at toPosition
   c. Apply special square effects
5. Return new state
```

#### `applySpecialSquareEffects(GameState state, int position, Player player)`
Handles Water square effect.

```
IF position == 27 (House of Water):
   1. Remove piece from 27
   2. Find rebirth position (15 or first empty before it)
   3. Place piece at rebirth position
```

#### `findRebirthPosition(Board board) → int`
Finds where to place piece returning from Water.

```
IF position 15 is empty:
   Return 15
ELSE:
   FOR i = 14 down to 1:
      IF position i is empty:
         Return i
   Return 1 (fallback)
```

#### `isTerminalState(GameState state) → boolean`
Returns `state.isGameOver()`

#### `hasLegalMoves(GameState state, int roll) → boolean`
Returns `!getLegalMoves(state, roll).isEmpty()`

---

### 3. BoardDisplay.java (Static Utility Class)
**Purpose:** Handles console visualization of the game.

**Board Layout (S-Pattern):**
```
╔════════════════════════════════════════════════════════════╗
║                    SENET GAME BOARD                        ║
╠════════════════════════════════════════════════════════════╣
║  1:●   2:○   3:●   4:○   5:●   6:○   7:●   8:○   9:●  10:○ ║  ← Row 1 (1→10)
╟────────────────────────────────────────────────────────────╢
║ 20:   19:   18:   17:   16:   15:☥  14:○  13:●  12:○  11:● ║  ← Row 2 (20→11, reversed)
╟────────────────────────────────────────────────────────────╢
║ 21:   22:   23:   24:   25:   26:⚮  27:≈  28:⚶  29:☉  30:⊙ ║  ← Row 3 (21→30)
╚════════════════════════════════════════════════════════════╝
```

**Methods:**

| Method | Description |
|--------|-------------|
| `printBoard(GameState state)` | Prints complete board with game info |
| `printRow(Board board, int start, int end, boolean reverse)` | Prints one row of squares |
| `printSquare(Board board, int position)` | Prints single square with piece/symbol |
| `getSpecialSymbol(int position)` | Gets symbol for special squares |
| `printGameInfo(GameState state)` | Prints current player and piece counts |
| `printMove(Move move, int roll)` | Prints move details |
| `printLegalMoves(List<Move> moves)` | Prints numbered list of legal moves |

---

### 4. SticksManager.java (Static Utility Class)
**Purpose:** Simulates the ancient stick-throwing dice mechanism.

**How Sticks Work:**
- 4 sticks are thrown
- Each stick lands either dark (■) or light (□)
- Roll value = number of dark sticks
- **Exception:** 0 dark sticks = roll of 5

**Probability Distribution:**

| Dark Sticks | Roll Value | Combinations | Probability |
|-------------|------------|--------------|-------------|
| 1 | 1 | C(4,1) = 4 | 4/16 = 0.25 |
| 2 | 2 | C(4,2) = 6 | 6/16 = 0.375 |
| 3 | 3 | C(4,3) = 4 | 4/16 = 0.25 |
| 4 | 4 | C(4,4) = 1 | 1/16 = 0.0625 |
| 0 | 5 | C(4,0) = 1 | 1/16 = 0.0625 |

**Methods:**

| Method | Returns | Description |
|--------|---------|-------------|
| `getProbability(int roll)` | double | Gets probability for roll value |
| `getAllPossibleRolls()` | int[] | Returns {1, 2, 3, 4, 5} |
| `throwSticks()` | int | Simulates throw, returns 1-5 |
| `throwSticksWithDisplay()` | ThrowResult | Returns roll with visual representation |
| `getProbabilityDistribution()` | Map<Integer, Double> | Returns all probabilities |

**Inner Class: ThrowResult**
```java
public static class ThrowResult {
    private final int value;      // Roll value (1-5)
    private final boolean[] sticks; // true=dark, false=light

    // toString() returns: "Sticks: [□ ■ ■ □] → Roll: 2"
}
```

---

## Computer Package (AI Algorithm)

### 1. TurnType.java (Enum)
**Purpose:** Defines the three types of nodes in the game tree.

```java
public enum TurnType {
    MAX,    // Computer's turn - wants to maximize score
    MIN,    // Opponent's turn - wants to minimize score
    CHANCE  // Dice roll - calculates expected value
}
```

---

### 2. GameStats.java (Class)
**Purpose:** Tracks statistics during AI search.

**Attributes:**
| Attribute | Type | Description |
|-----------|------|-------------|
| `nodesExplored` | int | Total nodes visited |
| `maxNodes` | int | MAX nodes visited |
| `minNodes` | int | MIN nodes visited |
| `chanceNodes` | int | CHANCE nodes visited |
| `maxDepthReached` | int | Deepest level reached |
| `startTime` | long | Search start timestamp |
| `endTime` | long | Search end timestamp |

**Methods:**
| Method | Description |
|--------|-------------|
| `reset()` | Resets all counters and starts timer |
| `incrementNode(TurnType)` | Increments appropriate counter |
| `updateMaxDepth(int depth)` | Updates max depth if deeper |
| `endSearch()` | Records end time |
| `printStats()` | Prints formatted statistics |
| `getNodesExplored()` | Returns total nodes |
| `getTimeTaken()` | Returns search duration in ms |

---

### 3. GameResult.java (Class)
**Purpose:** Container for search results.

**Attributes:**
| Attribute | Type | Description |
|-----------|------|-------------|
| `value` | double (final) | Evaluation score |
| `bestMove` | Move (final) | Best move found |
| `nodeType` | TurnType (final) | Type of node that produced result |

---

### 4. Expectiminimax.java (Class) - **CORE AI ALGORITHM**
**Purpose:** Implements the Expectiminimax algorithm with Alpha-Beta pruning.

#### What is Expectiminimax?

Expectiminimax is an extension of the Minimax algorithm that handles **games with chance elements** (like dice). It combines:
- **MAX nodes**: Computer tries to maximize score
- **MIN nodes**: Opponent tries to minimize score
- **CHANCE nodes**: Calculate expected value over all possible dice outcomes

**Attributes:**
| Attribute | Type | Description |
|-----------|------|-------------|
| `computerPlayer` | Player (final) | Which player the AI controls |
| `stats` | GameStats (final) | Search statistics tracker |
| `verbose` | boolean (final) | Print detailed output |

**Constructor:**
```java
public Expectiminimax(Player computerPlayer, boolean verbose)
```

---

#### Method: `getBestMove(GameState state, int roll, int maxDepth) → Move`

**Entry point** for the AI. Finds the best move for given state and roll.

```
Algorithm:
1. Reset statistics
2. Get all legal moves for current roll
3. IF no legal moves: return null
4. IF only one move: return that move (no search needed)
5. bestValue = -∞, bestMove = null

6. FOR each legal move:
   a. Apply move to get next state
   b. Switch player (opponent's turn)
   c. value = chanceNode(nextState, depth-1, -∞, +∞, false)
      ↑ Opponent will roll dice next
   d. IF value > bestValue:
      - bestValue = value
      - bestMove = move

7. End search (record time)
8. Return bestMove
```

---

#### Method: `chanceNode(state, depth, alpha, beta, isMaxPlayer) → double`

**Calculates expected value** over all possible dice outcomes.

```
Algorithm:
1. Increment CHANCE node counter
2. Update max depth statistic

3. IF depth == 0 OR game over:
   Return Heuristic.evaluate(state, computerPlayer)

4. expectedValue = 0.0
5. possibleRolls = [1, 2, 3, 4, 5]

6. FOR each roll in possibleRolls:
   probability = getProbability(roll)

   IF isMaxPlayer (computer's turn):
      value = maxNode(state, depth, alpha, beta, roll)
   ELSE (opponent's turn):
      value = minNode(state, depth, alpha, beta, roll)

   expectedValue += probability × value

7. Return expectedValue
```

**Mathematical Formula:**
```
E[V] = Σ P(roll) × Value(roll)
     = 0.25×V(1) + 0.375×V(2) + 0.25×V(3) + 0.0625×V(4) + 0.0625×V(5)
```

---

#### Method: `maxNode(state, depth, alpha, beta, roll) → double`

**Computer's turn** - tries to MAXIMIZE the score.

```
Algorithm:
1. Increment MAX node counter
2. Get legal moves for given roll

3. IF no legal moves (turn skipped):
   a. Clone state, switch player
   b. Return chanceNode(nextState, depth-1, alpha, beta, false)
      ↑ Opponent will roll next

4. maxValue = -∞

5. FOR each legal move:
   a. Apply move to get next state
   b. IF terminal state: return Heuristic.evaluate()
   c. Switch player
   d. value = chanceNode(nextState, depth-1, alpha, beta, false)
   e. maxValue = max(maxValue, value)
   f. alpha = max(alpha, value)

   g. IF beta <= alpha: BREAK (Alpha-Beta Pruning)
      ↑ MIN already has a better option, skip remaining

6. Return maxValue
```

---

#### Method: `minNode(state, depth, alpha, beta, roll) → double`

**Opponent's turn** - tries to MINIMIZE the score.

```
Algorithm:
1. Increment MIN node counter
2. Get legal moves for given roll

3. IF no legal moves (turn skipped):
   a. Clone state, switch player
   b. Return chanceNode(nextState, depth-1, alpha, beta, true)
      ↑ Computer will roll next

4. minValue = +∞

5. FOR each legal move:
   a. Apply move to get next state
   b. IF terminal state: return Heuristic.evaluate()
   c. Switch player
   d. value = chanceNode(nextState, depth-1, alpha, beta, true)
   e. minValue = min(minValue, value)
   f. beta = min(beta, value)

   g. IF beta <= alpha: BREAK (Alpha-Beta Pruning)
      ↑ MAX already has a better option, skip remaining

6. Return minValue
```

---

#### Visual Representation of Game Tree

```
                    [ROOT - Current State]
                           |
            ┌──────────────┼──────────────┐
            ↓              ↓              ↓
        [Move 1]       [Move 2]       [Move 3]
            |              |              |
            ↓              ↓              ↓
      [CHANCE Node]  [CHANCE Node]  [CHANCE Node]
      (Opponent rolls dice)
            |
    ┌───┬───┬───┬───┬───┐
    ↓   ↓   ↓   ↓   ↓
  R=1 R=2 R=3 R=4 R=5  (Weighted by probability)
  .25 .375 .25 .0625 .0625
    |   |   |   |   |
    ↓   ↓   ↓   ↓   ↓
  [MIN] [MIN] ...      (Opponent chooses minimum)
    |
    ↓
  [CHANCE Node]        (Computer rolls dice)
    |
  [MAX]                (Computer chooses maximum)
    |
   ...                 (Continue until depth=0 or terminal)
```

---

#### Alpha-Beta Pruning Explanation

Alpha-Beta pruning optimizes the search by eliminating branches that cannot affect the final decision.

- **Alpha (α)**: Best value MAX can guarantee (starts at -∞)
- **Beta (β)**: Best value MIN can guarantee (starts at +∞)

**Pruning Condition:** `β ≤ α`

```
Example:
                MAX
               /   \
             MIN    MIN
            /   \     \
           3     5    ?

1. Left MIN evaluates: min(3,5) = 3
2. MAX knows it can get at least 3 (α = 3)
3. Right MIN starts evaluating...
   - If right MIN finds value ≤ 3, MAX won't choose it
   - We can skip remaining children of right MIN
```

---

### 5. Heuristic.java (Static Utility Class) - **EVALUATION FUNCTION**
**Purpose:** Evaluates board positions to guide AI decisions.

#### Evaluation Formula:
```
Score = (PiecesExited × 1000) + (Advancement × 10) + (Safety × 5)
      + (SpecialSquares × 20) + (Blocking × 15)
```

**Weight Constants:**
| Weight | Value | Description |
|--------|-------|-------------|
| `PIECE_EXITED_WEIGHT` | 1000.0 | Most important - winning condition |
| `PIECE_ADVANCEMENT_WEIGHT` | 10.0 | Reward forward progress |
| `PIECE_SAFETY_WEIGHT` | 5.0 | Reward safe positions |
| `SPECIAL_SQUARE_WEIGHT` | 20.0 | Control special squares |
| `OPPONENT_BLOCKING_WEIGHT` | 15.0 | Block opponent pieces |

---

#### Method: `evaluate(GameState state, Player computerPlayer) → double`

```
Algorithm:
1. IF terminal state:
   - IF computer wins: return +∞
   - ELSE: return -∞

2. score = 0.0

3. // Pieces Exited (MOST IMPORTANT)
   score += (myExited - opponentExited) × 1000

4. // Advancement Score
   score += calculateAdvancementScore()

5. // Safety Score
   score += calculateSafetyScore()

6. // Special Square Control
   score += calculateSpecialSquareScore()

7. // Blocking Score
   score += calculateBlockingScore()

8. Return score
```

---

#### Method: `calculateAdvancementScore(state, player) → double`

Rewards pieces that are closer to exiting.

```
Algorithm:
playerSum = 0
FOR each player piece at position pos:
   playerSum += pos² (quadratic to emphasize advancement)

opponentSum = 0
FOR each opponent piece at position pos:
   opponentSum += pos²

Return (playerSum - opponentSum) × ADVANCEMENT_WEIGHT
```

**Why Quadratic?**
- Position 10: contributes 100
- Position 20: contributes 400
- Position 30: contributes 900

This strongly favors advanced pieces!

---

#### Method: `calculateSafetyScore(state, player) → double`

Rewards pieces in safe zones.

```
Algorithm:
safetyScore = 0
FOR each player piece at position pos:
   IF pos >= 26: safetyScore += 3.0  (very safe)
   ELSE IF pos > 15: safetyScore += 1.0 (safer)

Return safetyScore × SAFETY_WEIGHT
```

---

#### Method: `calculateSpecialSquareScore(state, player) → double`

Evaluates special square occupancy.

| Position | Score | Reason |
|----------|-------|--------|
| 27 (Water) | -50 | PENALTY - piece will return to 15 |
| 26 (Happiness) | +25 | Good - gateway to exit |
| 28 (Three Truths) | +30 | Close to exit |
| 29 (Re-Atoum) | +35 | Very close to exit |
| 30 (Horus) | +40 | Best - any roll exits |

---

#### Method: `calculateBlockingScore(state, player) → double`

Rewards blocking opponent pieces.

```
Algorithm:
blockingScore = 0
FOR each opponent piece at oppPos:
   FOR each player piece at myPos:
      IF myPos > oppPos AND myPos - oppPos <= 5:
         blockingScore += 1.0
         (We're within striking distance ahead of opponent)

Return blockingScore × BLOCKING_WEIGHT
```

---

## Utils Package

### Constants.java
**Purpose:** Centralizes game constants.

```java
// Board constants
BOARD_SIZE = 30
PIECES_PER_PLAYER = 7

// Special square positions
HOUSE_OF_REBIRTH = 15
HOUSE_OF_HAPPINESS = 26
HOUSE_OF_WATER = 27
HOUSE_OF_THREE_TRUTHS = 28
HOUSE_OF_RE_ATOUM = 29
HOUSE_OF_HORUS = 30

// Dice constants
NUM_STICKS = 4
MIN_ROLL = 1
MAX_ROLL = 5
```

---

## Main Entry Point

### Main.java
**Purpose:** Application entry point with command-line argument parsing.

**Command Line Options:**
| Option | Short | Description | Default |
|--------|-------|-------------|---------|
| `--depth` | `-d` | AI search depth (1-10) | 6 |
| `--verbose` | `-v` | Show algorithm details | false |
| `--ai-first` | `-a` | Computer plays first | false |
| `--help` | `-h` | Show help message | - |

**Usage Examples:**
```bash
java Main                    # Default settings
java Main -d 4 -v            # Depth 4, verbose mode
java Main --depth 5 --ai-first  # Depth 5, AI goes first
```

---

## Execution Flow

### Complete Game Execution Flow:

```
┌─────────────────────────────────────────────────────────────┐
│                    PROGRAM START                            │
│                    Main.main(args)                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│               PARSE COMMAND LINE ARGUMENTS                  │
│    depth = 6, verbose = false, aiFirst = false (defaults)   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│              CREATE GAME CONTROLLER                         │
│    new GameController(depth, verbose, aiFirst)              │
│    - Creates GameState with initial board                   │
│    - Sets humanPlayer = WHITE, computerPlayer = BLACK       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   GAME LOOP STARTS                          │
│                   game.playGame()                           │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
         ┌────────────────────────────────────────┐
         │         WHILE (!state.isGameOver())    │
         └────────────────────────────────────────┘
                              │
              ┌───────────────┴───────────────┐
              ▼                               │
┌─────────────────────────┐                   │
│   DISPLAY BOARD         │                   │
│   BoardDisplay.         │                   │
│   printBoard(state)     │                   │
└─────────────────────────┘                   │
              │                               │
              ▼                               │
┌─────────────────────────┐                   │
│   ROLL DICE             │                   │
│   SticksManager.        │                   │
│   throwSticksWithDisplay│                   │
│   → returns 1-5         │                   │
└─────────────────────────┘                   │
              │                               │
              ▼                               │
     ┌────────┴────────┐                      │
     │  CURRENT PLAYER │                      │
     └─────────────────┘                      │
     /                  \                     │
    ▼                    ▼                    │
┌────────┐          ┌─────────┐               │
│ HUMAN  │          │COMPUTER │               │
└────────┘          └─────────┘               │
    │                    │                    │
    ▼                    ▼                    │
┌────────────┐    ┌─────────────────┐         │
│humanTurn() │    │computerTurn()   │         │
└────────────┘    └─────────────────┘         │
    │                    │                    │
    ▼                    ▼                    │
┌────────────┐    ┌─────────────────┐         │
│Get Legal   │    │Create           │         │
│Moves       │    │Expectiminimax   │         │
└────────────┘    └─────────────────┘         │
    │                    │                    │
    ▼                    ▼                    │
┌────────────┐    ┌─────────────────┐         │
│Show Moves  │    │getBestMove()    │         │
│Get Input   │    │ ┌─────────────┐ │         │
└────────────┘    │ │FOR each move│ │         │
    │             │ │  →chanceNode│ │         │
    │             │ │    →minNode │ │         │
    │             │ │    →maxNode │ │         │
    │             │ │  →Heuristic │ │         │
    │             │ └─────────────┘ │         │
    │             └─────────────────┘         │
    │                    │                    │
    ▼                    ▼                    │
┌─────────────────────────────────────┐       │
│          APPLY MOVE                  │       │
│   GameRules.applyMove(state, move)   │       │
│   - Update board                     │       │
│   - Handle special squares           │       │
│   - Check win condition              │       │
└─────────────────────────────────────┘       │
              │                               │
              ▼                               │
┌─────────────────────────────────────┐       │
│        SWITCH PLAYER                 │       │
│        state.switchPlayer()          │       │
└─────────────────────────────────────┘       │
              │                               │
              └───────────────────────────────┘
                              │
              (Loop continues until gameOver)
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      GAME OVER                              │
│              Display winner: gameOver()                     │
└─────────────────────────────────────────────────────────────┘
```

---

## Expectiminimax Algorithm Detailed Explanation

### Why Expectiminimax?

In games with **randomness** (dice, cards), we cannot use standard Minimax because:
- We don't know what dice will roll
- Different rolls lead to different legal moves
- We must consider **all possible outcomes**

**Solution:** Insert CHANCE nodes that calculate **expected values**.

### Three Node Types:

```
┌──────────────────────────────────────────────────────────┐
│  MAX Node (Computer's Turn)                              │
│  ─────────────────────────                               │
│  • Computer just rolled dice                             │
│  • Knows the roll value                                  │
│  • Picks move that MAXIMIZES score                       │
│  • Returns: max(child values)                            │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│  MIN Node (Opponent's Turn)                              │
│  ─────────────────────────                               │
│  • Opponent just rolled dice                             │
│  • Knows the roll value                                  │
│  • Picks move that MINIMIZES score (for computer)        │
│  • Returns: min(child values)                            │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│  CHANCE Node (Dice Roll)                                 │
│  ─────────────────────────                               │
│  • Player is about to roll dice                          │
│  • Don't know outcome yet                                │
│  • Calculate EXPECTED value over all possible rolls      │
│  • Returns: Σ P(roll) × Value(roll)                      │
└──────────────────────────────────────────────────────────┘
```

### Search Pattern:

```
getBestMove (entry point)
    │
    ├─► Apply Move 1 → chanceNode (opponent rolls)
    │                       │
    │                       ├─► roll=1 (25%) → minNode → chanceNode → maxNode → ...
    │                       ├─► roll=2 (37.5%) → minNode → ...
    │                       ├─► roll=3 (25%) → minNode → ...
    │                       ├─► roll=4 (6.25%) → minNode → ...
    │                       └─► roll=5 (6.25%) → minNode → ...
    │
    │                   Expected Value = Σ(probability × value)
    │
    ├─► Apply Move 2 → chanceNode → ...
    │
    └─► Apply Move 3 → chanceNode → ...

    Return move with HIGHEST expected value
```

### Depth Explanation:

- **Depth** = number of "moves" to look ahead
- Each depth level has: CHANCE → MAX/MIN cycle
- Higher depth = better decisions but exponentially more computation

**Example at Depth 3:**
```
Turn 1: Computer rolls → MAX picks move
Turn 2: Opponent rolls → MIN picks move
Turn 3: Computer rolls → MAX picks move
Then → Evaluate with Heuristic
```

---

## Heuristic Function Analysis

### Why is Heuristic Important?

The heuristic function determines HOW GOOD a board position is. It guides the AI to:
- Prefer winning positions
- Avoid losing positions
- Make progress toward the goal

### Weight Justification:

1. **PIECES_EXITED (1000)** - Highest weight
   - Directly related to winning condition
   - One exited piece is worth 100× advancement progress

2. **SPECIAL_SQUARES (20)** - Second highest
   - Critical for strategy
   - Being on Horus (30) almost guarantees exit
   - Being on Water (27) sets you back

3. **BLOCKING (15)** - Third highest
   - Tactical advantage
   - Prevents opponent progress

4. **ADVANCEMENT (10)** - Fourth
   - General progress metric
   - Quadratic formula emphasizes advanced pieces

5. **SAFETY (5)** - Lowest
   - Defensive consideration
   - Pieces past 26 can't be easily attacked

### Heuristic Example Calculation:

```
Board State:
- Computer (BLACK): pieces at 22, 26, 28 + 2 exited
- Opponent (WHITE): pieces at 5, 10, 15, 27 + 0 exited

Score Calculation:

1. Pieces Exited: (2 - 0) × 1000 = 2000

2. Advancement:
   Computer: 22² + 26² + 28² = 484 + 676 + 784 = 1944
   Opponent: 5² + 10² + 15² + 27² = 25 + 100 + 225 + 729 = 1079
   Score: (1944 - 1079) × 10 = 8650

3. Safety:
   Position 22: +1 (> 15)
   Position 26: +3 (>= 26)
   Position 28: +3 (>= 26)
   Score: 7 × 5 = 35

4. Special Squares:
   Position 26: +25 (Happiness)
   Position 28: +30 (Three Truths)
   Opponent at 27: (handled for opponent, not us)
   Score: 55 × 20 = 1100

5. Blocking:
   Our 26 blocks their 5, 10, 15 (distance ≤ 5? Only 15→26=11, no)
   Our 22 blocks their 15 (22-15=7 > 5, no)
   etc... Let's say blocking score = 2
   Score: 2 × 15 = 30

TOTAL: 2000 + 8650 + 35 + 1100 + 30 = 11,815
```

---

## Summary

This Senet game project demonstrates:

1. **Clean Architecture**: Separation of concerns (models, game logic, AI)
2. **Immutable Design**: States are cloned, not modified
3. **Expectiminimax Algorithm**: Handling randomness in game AI
4. **Alpha-Beta Pruning**: Optimizing search efficiency
5. **Heuristic Design**: Multi-factor evaluation function
6. **Object-Oriented Design**: Enums, classes, static utilities

The AI's strength depends on:
- Search depth (higher = stronger but slower)
- Heuristic accuracy (weights and factors)
- Probability handling (dice expectations)

---

## Quick Reference

### Run the Game:
```bash
java -jar SenetGame.jar
java -jar SenetGame.jar -d 4 -v -a
```

### Key Files:
- **Start here**: `Main.java` → `GameController.java`
- **AI Logic**: `Expectiminimax.java` → `Heuristic.java`
- **Rules**: `GameRules.java`
- **Data**: `Board.java`, `GameState.java`, `Move.java`

### Algorithm Complexity:
- **Branching Factor**: ~5-15 moves per turn × 5 dice outcomes
- **Time Complexity**: O(b^d) where b=branch factor, d=depth
- **Space Complexity**: O(d) for recursion stack
