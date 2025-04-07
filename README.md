# Mansion Escape

A thrilling Java-based escape room adventure game where players must solve puzzles to escape a mysterious mansion.

## Description

Mansion Escape is an immersive first-person adventure game where players find themselves trapped in an abandoned mansion filled with secrets, puzzles, and hidden passages. Navigate through intricately designed rooms, collect items, solve challenging puzzles, and uncover the dark history of the mansion to find your way to freedom.

## Features

- Atmospheric first-person exploration
- Complex interconnected puzzle systems
- Interactive environment with collectible items
- Inventory management system
- Immersive sound design and background music
- Multiple endings based on discovered secrets
- Save/load game functionality
- Hint system for puzzle assistance

## Requirements

- Java Development Kit (JDK) 11 or newer
- 4GB RAM minimum
- 500MB available storage
- Graphics card with OpenGL 3.3 support or higher
- Windows, macOS, or Linux operating system

## Installation

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/mansion-escape.git
   cd mansion-escape
   ```

2. Compile the project:
   ```
   javac -d bin -cp libs/* src/*.java
   ```

3. Run the game:
   ```
   java -cp bin:libs/* MansionEscape
   ```

For IDE users (Eclipse, IntelliJ IDEA, NetBeans), import the project and ensure all dependencies in the `libs` folder are included in your build path.

## How to Play

- Use WASD keys to move through the mansion
- Mouse to look around
- Left-click to interact with objects or use items
- E key to open/close inventory
- F key to examine objects closely
- Tab key to access the hints menu
- Escape key to access the game menu

## Game Mechanics

1. **Exploration**: Carefully search each room for clues and items
2. **Inventory**: Collect and combine items to solve puzzles
3. **Puzzles**: Solve various types of puzzles including:
   - Logic puzzles
   - Pattern recognition
   - Lock combinations
   - Environmental manipulation
   - Symbol interpretation
4. **Journal**: Discover journal entries that reveal the mansion's story and provide subtle hints

## Project Structure

```
mansion-escape/
├── src/
│   ├── MansionEscape.java           # Main application entry point
│   ├── game/
│   │   ├── GameEngine.java          # Core game loop and mechanics
│   │   ├── GameState.java           # Game state management
│   │   └── SaveManager.java         # Save/load functionality
│   ├── ui/
│   │   ├── GameUI.java              # User interface elements
│   │   ├── InventoryUI.java         # Inventory management screen
│   │   └── MenuUI.java              # Main and pause menus
│   ├── world/
│   │   ├── Room.java                # Room environment management
│   │   ├── GameObject.java          # Interactive object base class
│   │   └── RoomManager.java         # Room transitions and states
│   ├── puzzle/
│   │   ├── Puzzle.java              # Base puzzle class
│   │   ├── LockPuzzle.java          # Combination locks
│   │   └── SequencePuzzle.java      # Sequential activation puzzles
│   ├── entity/
│   │   ├── Player.java              # Player properties and movement
│   │   ├── Item.java                # Collectible items
│   │   └── Inventory.java           # Inventory management
│   └── util/
│       ├── ResourceLoader.java      # Asset loading utilities
│       └── AudioManager.java        # Sound management
├── resources/
│   ├── textures/                    # Game textures and images
│   ├── models/                      # 3D models
│   ├── audio/                       # Sound effects and music
│   ├── puzzles/                     # Puzzle configurations
│   └── story/                       # Story text and journal entries
├── libs/                            # External libraries
├── build.xml                        # Ant build file
├── .gitignore
└── README.md                        # This file
```

## Building an Executable JAR

To create a distributable JAR file:

```
ant build-jar
```

The executable JAR will be created in the `dist` directory.

## Development

### Adding New Puzzles

Create a new class that extends the `Puzzle` base class:

```java
public class MyCustomPuzzle extends Puzzle {
    @Override
    public void initialize() {
        // Set up puzzle components
    }
    
    @Override
    public boolean checkSolution() {
        // Verify if puzzle is solved
        return solutionCorrect;
    }
}
```

### Adding New Rooms

Create a new room configuration file in `resources/rooms/` and register it in `RoomManager.java`.

## Troubleshooting

- **Game crashes on startup**: Verify your Java version and graphics card compatibility
- **Missing textures**: Check that all resources were properly downloaded during installation
- **Cannot interact with objects**: Ensure you're close enough and the interaction prompt appears

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Inspired by classic escape room games and point-and-click adventures
- Uses the [LightWeight Java Game Library (LWJGL)](https://www.lwjgl.org/) for rendering
- Custom 3D models created with Blender
