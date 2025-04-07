package mansionescape;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GameKeyListener extends KeyAdapter {
    private GamePanel gamePanel;
    private GameWindow gameWindow;

    public GameKeyListener(GamePanel panel, GameWindow window) {
        gamePanel = panel;
        gameWindow = window;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Player player = gamePanel.getGameWorld().getPlayer();
        int key = e.getKeyCode();

        // Movement
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            player.move(Direction.UP, gamePanel.getGameWorld().getCurrentMap());
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            player.move(Direction.DOWN, gamePanel.getGameWorld().getCurrentMap());
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            player.move(Direction.LEFT, gamePanel.getGameWorld().getCurrentMap());
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            player.move(Direction.RIGHT, gamePanel.getGameWorld().getCurrentMap());
        }

        // Interaction
        if (key == KeyEvent.VK_E) {
            player.interact(gamePanel.getGameWorld(), gameWindow);
        }

        // Attack/Use item
        if (key == KeyEvent.VK_SPACE) {
            player.attack(gamePanel.getGameWorld(), gameWindow);
        }

        // Show inventory
        if (key == KeyEvent.VK_I) {
            gameWindow.showInventory();
        }

        // Toggle minimap
        if (key == KeyEvent.VK_M) {
            gamePanel.toggleMinimap();
        }

        // Toggle grid
        if (key == KeyEvent.VK_G) {
            gamePanel.toggleGrid();
        }

        // Show help
        if (key == KeyEvent.VK_H) {
            gameWindow.showHelp();
        }

        // Pause game
        if (key == KeyEvent.VK_ESCAPE) {
            gameWindow.pauseGame();
        }

        gamePanel.repaint();
    }
}