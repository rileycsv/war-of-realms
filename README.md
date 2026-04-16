# War of Realms

A turn-based strategy game built with Java and JavaFX. Two players take turns moving units across an isometric battlefield, choosing their kingdom and map before the battle begins.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Running the Project](#running-the-project)
- [How to Play](#how-to-play)
- [Project Structure](#project-structure)
- [Game Architecture](#game-architecture)
- [Development Status](#development-status)
- [Documentation](#documentation)

---

## Prerequisites

You need three things installed before you can run the project.

### 1. JDK 25.0.2

Download from: https://jdk.java.net/25/

Install it to the default location. On Windows this will be something like:
```
C:\Program Files\Java\jdk-25.0.2\
```

### 2. JavaFX SDK 25.0.2

Download from: https://gluonhq.com/products/javafx/

Unzip it directly to your `C:\` drive so the result is:
```
C:\javafx-sdk-25.0.2\
```

> **Important:** The path must be exactly `C:\javafx-sdk-25.0.2\`. The build tasks in this project depend on that exact location.

### 3. Visual Studio Code

Download from: https://code.visualstudio.com/

Once VS Code is installed, open the project folder. VS Code should show a popup:
> *"This workspace has extension recommendations. Would you like to install them?"*

Click **Install**. This installs the **Extension Pack for Java**, which is required to build and run the project.

If you missed the popup, you can install it manually:
1. Open the Extensions panel (`Ctrl+Shift+X`)
2. Search for **Extension Pack for Java**
3. Click **Install** on the Microsoft result

---

## Running the Project

1. Open the `war-of-realms` folder in VS Code (`File → Open Folder`)
2. Wait for the Java extension to finish loading (bottom-right status bar)
3. Press **`F5`** to compile and launch

The build will automatically compile all source files and start the game.

**Other build options:**
- `Ctrl+Shift+B` — Compile only (no launch)
- `Ctrl+Shift+P` → "Tasks: Run Task" → "Run War of Realms" — Compile and run

---

## How to Play

### Setup Phase

When the game starts, each player takes a turn **placing their army** on the board. Click any open tile to place your units there. The game prevents you from placing units too close to the opponent's starting area.

### Gameplay Phase

Once both players have placed their units, the main game begins.

**Selecting and moving a unit:**
1. Click one of your units to select it (it will highlight yellow)
2. Blue tiles show where it can move
3. Red tiles show what it can attack
4. Click a blue tile to move the unit there

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
├── src/                          # All Java source files
│   ├── core/                     # Game state management
│   │   ├── GameManager.java      # Tracks turns, active player, unit selection
│   │   └── Player.java           # Stores a player's ID and chosen kingdom
│   ├── entities/                 # Units and kingdom definitions
│   │   ├── Unit.java             # Abstract base class for all units
│   │   ├── Infantry.java         # Infantry unit type
│   │   ├── Archer.java           # Archer unit type
│   │   ├── Cavalry.java          # Cavalry unit type
│   │   ├── Pikeman.java          # Pikeman unit type
│   │   └── Kingdoms.java         # Factory that builds unit arrays per kingdom
│   ├── environment/              # Board and tile rendering
│   │   ├── Board.java            # Isometric rendering, input handling, camera
│   │   └── tile/                 # Terrain tile types
│   │       ├── Tile.java         # Abstract base class; handles sprite loading
│   │       ├── GrassTile.java    # Grass (movement cost: 1)
│   │       ├── ForestTile.java   # Forest (movement cost: 1)
│   │       ├── RiverTile.java    # River (movement cost: 2)
│   │       ├── MountainTile.java # Mountain (movement cost: 3)
│   │       └── VoidTile.java     # Void / impassable (movement cost: 100)
│   ├── main/
│   │   └── Main.java             # JavaFX entry point; manages screens
│   ├── ui/                       # UI helpers and screen navigation
│   │   ├── FxStages.java         # Extracts the Stage from a button click event
│   │   ├── MenuSelections.java   # Stores empire and difficulty choices
│   │   └── ScreenFlows.java      # Loads FXML screens by ID
│   └── utils/                    # Shared utilities
│       ├── Debug.java            # Priority-level debug logging
│       └── DatabaseIO.java       # SQLite reader for unit stat values
│
├── assets/                       # Game assets
│   ├── kingdoms/                 # Unit sprites organised by kingdom folder
│   ├── tiles/                    # Tile sprites (grass, mountain, river, etc.)
│   └── scenes/                   # FXML UI layout files + background images
│
└── docs/                         # Auto-generated JavaDoc HTML (open index.html)
```

## Documentation

Full JavaDoc API reference is in the `docs/` folder. Open `docs/index.html` in a browser to browse it.

To regenerate the docs after making changes, run (adjust the JavaFX path if needed):
```
"C:\Program Files\Java\jdk-25.0.2\bin\javadoc.exe" ^
  -d docs ^
  -sourcepath src ^
  -classpath "C:\javafx-sdk-25.0.2\lib\javafx.base.jar;C:\javafx-sdk-25.0.2\lib\javafx.controls.jar;C:\javafx-sdk-25.0.2\lib\javafx.fxml.jar;C:\javafx-sdk-25.0.2\lib\javafx.graphics.jar;C:\javafx-sdk-25.0.2\lib\javafx.media.jar;C:\javafx-sdk-25.0.2\lib\javafx.swing.jar;C:\javafx-sdk-25.0.2\lib\javafx.web.jar" ^
  -subpackages core:entities:environment:main:ui:utils ^
  src\battlefieldController.java src\playerOneController.java src\playerTwoController.java src\startGameController.java
```
