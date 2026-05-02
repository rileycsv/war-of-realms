# War of Realms

A turn-based strategy game built with Java and JavaFX. Two players take turns moving units across an isometric battlefield, choosing their kingdom and map before the battle begins.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Running the Project](#running-the-project)
- [How to Play](#how-to-play)
- [Project Structure](#project-structure)

---

## Prerequisites

You need three things installed before you can run the project.

### 1. JDK 25.0.2

Download from: https://www.oracle.com/java/technologies/javase/jdk25-archive-downloads.html

Install it to the default location. On Windows this will be something like:
```
C:\Program Files\Java\jdk-25.0.2\
```

---

## Running the Project

1. Open the `war-of-realms` folder in Windows Explorer
2. Locate `start.bat` in the project root folder
3. Double the file to run `start.bat`

The script will automatically compile all source files and start the game's main method.

---

# How to play
**Camera controls:**

| Action | Key / Input |
|--------|-------------|
| Pan camera | `W A S D` or arrow keys |
| Zoom in/out | Scroll wheel or `+` / `-` |
| Reset camera | `Space` |
| End your turn | "End Turn" button (bottom-right) |

**Debug mode:**
- Press `ESC` to exit the game (only works while debug mode is on)

---

## Project Structure

```
war-of-realms/
├── src/                              # All Java source files
│   ├── battlefieldController.java    # FXML controller for the battlefield/difficulty selection screen
│   ├── playerOneController.java      # FXML controller for player 1's empire selection screen
│   ├── playerTwoController.java      # FXML controller for player 2's empire selection screen
│   ├── startGameController.java      # FXML controller for the start game screen
│   ├── core/                         # Game state management
│   │   ├── GameManager.java          # Tracks turns, active player, unit selection
│   │   └── Player.java               # Stores a player's ID and chosen kingdom
│   ├── entities/                     # Units and kingdom definitions
│   │   ├── Unit.java                 # Abstract base class for all units
│   │   ├── Infantry.java             # Infantry unit type
│   │   ├── Archer.java               # Archer unit type
│   │   ├── Cavalry.java              # Cavalry unit type
│   │   ├── Pikeman.java              # Pikeman unit type
│   │   └── Kingdoms.java             # Factory that builds unit arrays per kingdom
│   ├── environment/                  # Board and tile rendering
│   │   ├── Board.java                # Isometric rendering, input handling, camera
│   │   ├── Boards.java               # Stores predefined map layouts as 2D char arrays
│   │   └── tile/                     # Terrain tile types
│   │       ├── Tile.java             # Abstract base class; handles sprite loading
│   │       ├── GrassTile.java        # Grass (movement cost: 1)
│   │       ├── ForestTile.java       # Forest (movement cost: 1)
│   │       ├── RiverTile.java        # River (movement cost: 2)
│   │       ├── MountainTile.java     # Mountain (movement cost: 3)
│   │       └── VoidTile.java         # Void / impassable (movement cost: 100)
│   ├── main/
│   │   └── Main.java                 # JavaFX entry point; manages screens
│   ├── ui/                           # UI helpers and screen navigation
│   │   ├── FxStages.java             # Extracts the Stage from a button click event
│   │   ├── MenuSelections.java       # Stores empire and difficulty choices
│   │   └── ScreenFlows.java          # Loads FXML screens by ID
│   └── utils/                        # Shared utilities
│       ├── Debug.java                # Priority-level debug logging
│       └── DatabaseIO.java           # SQLite reader for unit stat values
│
├── assets/                           # Game assets
│   ├── UnitValues.db                 # SQLite database containing unit stat values
│   ├── styles.css                    # Global JavaFX stylesheet
│   ├── kingdoms/                     # Unit sprites organised by kingdom folder
│   │   ├── aztec/
│   │   ├── english/
│   │   ├── hussites/
│   │   ├── mongolians/
│   │   └── portuguese/
│   ├── tiles/                        # Tile sprites organised by terrain type
│   │   ├── forest/
│   │   ├── grass/
│   │   ├── mountain/
│   │   ├── river/
│   │   └── void/
│   └── scenes/                       # FXML UI layout files
│       ├── chooseBattlefield.fxml
│       ├── playerOneEmpire.fxml
│       ├── playerTwoEmpire.fxml
│       ├── startGame.fxml
│       └── sceneImages/              # Background images used by UI screens
│
├── sources.txt                       # Auto-generated list of source files for compilation
└── start.bat                         # Build and launch script
```
