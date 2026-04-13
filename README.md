# War of Realms

A turn-based strategy game built with Java and JavaFX, featuring isometric grid-based gameplay, multiple tile types, and player-controlled units.

## Overview

War of Realms is a tactical strategy game where players control units on a dynamically rendered isometric game board. The game features different terrain types, player management, and a modular design for easy expansion with new game mechanics and content.

## Features

- **Isometric Grid Rendering** – Dynamic 2D isometric projection using JavaFX Canvas for smooth visuals
- **Multi-Tile Support** – Various terrain types including:
  - Grass tiles
  - Mountain tiles (higher traverse cost)
  - River tiles
  - Void/empty tiles
- **Turn-Based Gameplay** – Game state management with active player tracking
- **Player System** – Support for multiple players with kingdom assignments and unit control
- **Unit Management** – Individual units with stats (health, movement, attack damage/range)
- **Sprite Variants** – Procedural Voronoi noise-based tile sprite caching for visual variety
- **Debug System** – Built-in debug logging and escape-key exit for development

## Project Structure

```
war-of-realms/
├── src/                         # Source code
│   ├── core/                    # Game logic and state management
│   │   ├── GameManager.java     # Global game state, active player tracking
│   │   └── Player.java          # Player definition and kingdom management
│   ├── entities/                # Game entities
│   │   └── Unit.java            # Controllable game units with stats
│   ├── environment/             # Board and rendering
│   │   ├── Board.java           # Isometric grid rendering and camera management
│   │   └── tile/                # Tile type definitions
│   │       ├── Tile.java        # Base tile class with sprite management
│   │       ├── GrassTile.java   # Grass terrain
│   │       ├── MountainTile.java # Mountain terrain (higher movement cost)
│   │       ├── RiverTile.java   # River terrain
│   │       └── VoidTile.java    # Empty/void terrain
│   ├── main/
│   │   └── Main.java            # JavaFX application entry point
│   └── utils/
│       └── Debug.java           # Debug logging utilities
│
├── assets/                      # Game assets
│   ├── kingdoms/                # Kingdom-related assets
│   ├── tiles/                   # Tile sprite sheets
│   │   ├── grass/
│   │   ├── mountain/
│   │   ├── river/
│   │   └── void/
│   └── scenes/                  # JavaFX FXML UI scenes
│       ├── startGame.fxml
│       ├── chooseBattlefield.fxml
│       ├── playerOneEmpire.fxml
│       ├── playerTwoEmpire.fxml
│       └── Scene Images/
│
├── docs/                        # JavaDoc HTML documentation
│   ├── core/
│   ├── entities/
│   ├── environment/
│   ├── main/
│   ├── utils/
│   └── index.html              # Documentation entry point
│
└── README.md                    # This file
```

## Getting Started

### Prerequisites

Before opening the project, make sure the following are installed and placed in the correct locations:

1. **JDK 25.0.2**
   - Download from [https://jdk.java.net/25/](https://jdk.java.net/25/)
   - Install to the default location: `C:\Program Files\Java\jdk-25.0.2\`

2. **JavaFX SDK 25.0.2**
   - Download from [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)
   - Unzip directly to `C:\` so that the result is `C:\javafx-sdk-25.0.2\`
   - ⚠️ The path must be exactly `C:\javafx-sdk-25.0.2\` — the build tasks depend on this location.

3. **Visual Studio Code**
   - Download from [https://code.visualstudio.com/](https://code.visualstudio.com/)

4. **Extension Pack for Java** (VS Code extension)
   - When you open the project in VS Code, you should see a popup:
     > *"This workspace has extension recommendations. Would you like to install them?"*
   - Click **Install** to install the Extension Pack for Java automatically.
   - If you dismissed the popup, you can install it manually:
     1. Open the Extensions sidebar (`Ctrl+Shift+X`)
     2. Search for **Extension Pack for Java**
     3. Click **Install** on the result by Microsoft
   - ⚠️ This extension is required to run the project with `F5`.

### Running the Project

1. Open the `war-of-realms` folder in VS Code (`File → Open Folder`)
2. Install the recommended extensions when prompted (see above)
3. Press **`F5`** to compile and launch the game

The build task will automatically:
- Create the `bin/` output directory if it doesn't exist
- Compile all Java source files
- Launch the application with JavaFX

Alternatively, you can run the build and run tasks separately:
- **`Ctrl+Shift+B`** — Compile only
- **`Ctrl+Shift+P` → "Tasks: Run Task" → "Run War of Realms"** — Compile and run

### Debug Mode

- Press **ESC** to exit the game when debug mode is enabled
- Debug output is logged to the console during gameplay

## Game Architecture

### Game Loop

1. **Main.java** initializes the JavaFX window and loads the game board
2. **Board.java** handles rendering on a Canvas with isometric projection
3. **GameManager** tracks active player and manages game state
4. **Unit** instances represent controllable entities with movement and combat stats

### Coordinate System

The board uses an isometric projection:
- **TILE_W**: 38 pixels (tile width)
- **FACE_H**: 22 pixels (face height)
- **SPRITE_H**: 36 pixels (sprite height)
- **Origin**: Positioned from the top vertex of tile (0,0)

### Tile System

Tiles are abstract with subclasses for each terrain type:
- Each tile has a movement cost
- Tiles load multiple sprite variants using sprite sheets
- Variants are cached using coordinate-based Voronoi noise for deterministic variation

## Development Status

- ✅ Basic isometric rendering
- ✅ Tile system with multiple terrain types
- ✅ Player and GameManager framework
- ✅ Unit entity structure
- 🔄 Unit movement mechanics (in progress)
- 🔄 Combat system (planned)
- 🔄 Turn-based gameplay loop (planned)
- 🔄 UI menus and HUD (planned)

## Contributing

When adding new features:

1. **New Tile Types** – Extend the `Tile` abstract class and add a corresponding constructor in the Board
2. **New Units** – Extend the `Unit` abstract class with stat definitions
3. **New Game Mechanics** – Add methods to `GameManager` and update the game loop in `Main`

## Documentation

Full JavaDoc HTML documentation is available in the `docs/` directory. Open `index.html` in a browser to view the complete API reference.