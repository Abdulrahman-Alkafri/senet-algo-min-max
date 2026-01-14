# Senet Game - Boot Search Project

This is an implementation of the ancient Egyptian game **Senet** using the **Expectiminimax algorithm** with Alpha-Beta pruning for intelligent game boot.

## Project Overview

Senet is a non-deterministic two-player board game where players move pieces based on dice rolls (4 sticks). The Boot uses the Expectiminimax algorithm to handle the probabilistic nature of the game.

## Project Structure

```
senet/
├── src/
│   ├── models/              # Data models
│   │   ├── Player.java      # Player enum (WHITE/BLACK)
│   │   ├── Square.java      # Special square definitions
│   │   ├── Board.java       # Game board (30 squares)
│   │   ├── Move.java        # Move representation
│   │   └── GameState.java   # Complete game state
│   │
│   ├── game/                # Game logic
│   │   ├── SticksManager.java    # Probability calculations & dice
│   │   ├── GameRules.java        # Game rules & move validation
│   │   ├── BoardDisplay.java     # Board visualization
│   │   └── GameController.java   # Main game loop
│   │
│   ├── boot/                # Boot implementation
│   │   ├── NodeType.java         # MAX, MIN, CHANCE nodes
│   │   ├── SearchResult.java     # Search result wrapper
│   │   ├── SearchStats.java      # Performance statistics
│   │   ├── Heuristic.java        # State evaluation function
│   │   └── Expectiminimax.java   # Main Boot algorithm
│   │
│   ├── utils/
│   │   └── Constants.java        # Game constants
│   │
│   └── Main.java            # Entry point
│
├── bin/                     # Compiled classes
└── README.md
```

## Compilation

```bash
# Compile all Java files
mkdir -p bin
javac -d bin src/**/*.java src/*.java
```

## Running the Game

### Basic Usage

```bash
# Run with default settings (depth=3, human plays first)
java -cp bin Main

# Show help
java -cp bin Main --help
```

### Command-Line Options

| Option | Description | Default |
|--------|-------------|---------|
| `-d, --depth <n>` | Search depth (1-10) | 3 |
| `-v, --verbose` | Show detailed algorithm output | false |
| `-a, --ai-first` | Boot plays first | false (human first) |
| `-h, --help` | Show help message | - |

### Examples

```bash
# Play with depth 4 and verbose output
java -cp bin Main --depth 4 --verbose

# Boot plays first with depth 5
java -cp bin Main -d 5 -a

# Full verbose mode
java -cp bin Main -d 4 -v -a
```

## Game Rules Summary

### Board Layout
- 30 squares arranged in an S-pattern (3 rows × 10 columns)
- Path: 1→10, 20→11 (reversed), 21→30

### Initial Setup
- 7 pieces per player (WHITE: ○, BLACK: ●)
- Pieces alternately placed on squares 1-14

### Dice Mechanics (4 Sticks)
- 4 sticks: each has light (0) and dark (1) side
- Roll calculation:
  - Sum dark sides (0-4)
  - If sum = 0 → roll = 5
  - Otherwise → roll = sum

**Probabilities:**
| Roll | Probability |
|------|------------|
| 1 | 25% (4/16) |
| 2 | 37.5% (6/16) |
| 3 | 25% (4/16) |
| 4 | 6.25% (1/16) |
| 5 | 6.25% (1/16) |

### Movement Rules
- Move piece forward by roll amount
- Landing on opponent → swap positions
- Landing on own piece → illegal move
- No legal moves → skip turn

### Special Squares

| Square | Symbol | Name | Effect |
|--------|--------|------|--------|
| 15 | ☥ | House of Rebirth | Destination for sent-back pieces |
| 26 | ⚮ | House of Happiness | Must land exactly (no jumping) |
| 27 | ≈ | House of Water | Return to House of Rebirth |
| 28 | ⚶ | House of Three Truths | Need roll=3 to exit |
| 29 | ☉ | House of Re-Atoum | Need roll=2 to exit |
| 30 | ⊙ | House of Horus | Any roll exits |

### Winning Condition
First player to exit all 7 pieces wins.

## AI Implementation

### Expectiminimax Algorithm

The AI uses a 3-layer algorithm:

1. **MAX Node** (AI turn): Maximize evaluation
2. **MIN Node** (Opponent turn): Minimize evaluation
3. **CHANCE Node** (Dice roll): Calculate expected value

```
Expected Value = Σ(Probability[roll] × Value[roll])
```

### Heuristic Evaluation

The evaluation function considers:

1. **Pieces Exited** (weight: 1000): Most important factor
2. **Piece Advancement** (weight: 10): Position on board
3. **Piece Safety** (weight: 5): Safety from swaps
4. **Special Squares** (weight: 20): Control of key squares
5. **Opponent Blocking** (weight: 15): Hindering opponent

### Alpha-Beta Pruning

Optimization technique to reduce search space by pruning branches that won't affect the final decision.

### Statistics Tracking

When using `-v/--verbose`, the AI displays:
- Total nodes explored
- MAX/MIN/CHANCE node counts
- Maximum depth reached
- Search time (milliseconds)

## Code Organization

### Models Package
- **Player**: Enum for WHITE/BLACK with symbols
- **Square**: Enum for special squares with symbols
- **Board**: 30-square board with piece management
- **Move**: Represents a move (from→to, swap, exit)
- **GameState**: Complete game state with history

### Game Package
- **SticksManager**: Probability calculations and dice simulation
- **GameRules**: Move validation and special square logic
- **BoardDisplay**: ASCII art board visualization
- **GameController**: Game loop coordination

### AI Package
- **NodeType**: Enum for MAX/MIN/CHANCE
- **Expectiminimax**: Main algorithm implementation
- **Heuristic**: State evaluation function
- **SearchStats**: Performance tracking

## Example Output

```
╔════════════════════════════════════════════════════════╗
║           WELCOME TO SENET - THE ANCIENT GAME          ║
╚════════════════════════════════════════════════════════╝