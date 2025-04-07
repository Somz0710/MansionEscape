package mansionescape;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class GamePanel extends JPanel {
    private GameWorld gameWorld;
    private String currentMapName;
    private static final int TILE_SIZE = 50;
    private boolean showMinimap = true;
    private boolean showGrid = true;

    // Enhanced graphics
    private HashMap<String, Image> images;
    private Animation playerAnimation;
    private HashMap<Enemy, Animation> enemyAnimations;
    private Image backgroundImage;
    private ArrayList<ParticleEffect> particles;

    public GamePanel() {
        gameWorld = new GameWorld();
        currentMapName = "Dungeon Entrance";
        particles = new ArrayList<>();
        enemyAnimations = new HashMap<>();

        // Load images and animations
        loadImages();

        // Initialize the world
        gameWorld.initWorld();

        // Animation timer
        new Timer(50, e -> {
            playerAnimation.update();
            for (Animation anim : enemyAnimations.values()) {
                anim.update();
            }
            updateParticles();
            repaint();
        }).start();

        // Add floating tooltips for items when player is nearby
        new Timer(500, e -> {
            Player player = gameWorld.getPlayer();
            Map currentMap = gameWorld.getCurrentMap();
            checkForNearbyItems(player, currentMap);
        }).start();
    }

    private void loadImages() {
        images = new HashMap<>();

        // Initialize with colored images for demonstration
        images.put("wall", createColoredImage(Color.DARK_GRAY));
        images.put("floor", createColoredImage(Color.LIGHT_GRAY));
        images.put("door", createColoredImage(Color.GREEN));
        images.put("lockedDoor", createColoredImage(Color.RED));
        images.put("key", createColoredImage(Color.YELLOW));
        images.put("potion", createColoredImage(Color.PINK));
        images.put("treasure", createColoredImage(Color.ORANGE));
        images.put("exit", createColoredImage(Color.CYAN));
        images.put("enemy", createColoredImage(Color.RED));
        images.put("trap", createColoredImage(Color.MAGENTA));

        // Create player animation
        Image[] playerFrames = new Image[4];
        playerFrames[0] = createColoredImage(Color.BLUE);
        playerFrames[1] = createColoredImage(new Color(0, 0, 200));
        playerFrames[2] = createColoredImage(Color.BLUE);
        playerFrames[3] = createColoredImage(new Color(0, 0, 220));
        playerAnimation = new Animation(playerFrames, 200);

        // Create background image
        backgroundImage = createBackgroundImage();
    }

    private Image createColoredImage(Color color) {
        // Create simple colored squares for demonstration
        BufferedImage img = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, TILE_SIZE-1, TILE_SIZE-1);
        g.dispose();
        return img;
    }

    private Image createBackgroundImage() {
        // Create a tiled background pattern
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(50, 50, 70));
        g.fillRect(0, 0, 100, 100);

        // Add some texture
        g.setColor(new Color(40, 40, 60));
        for (int i = 0; i < 50; i++) {
            int x = new Random().nextInt(100);
            int y = new Random().nextInt(100);
            int size = 1 + new Random().nextInt(4);
            g.fillOval(x, y, size, size);
        }

        g.dispose();
        return img;
    }

    private void checkForNearbyItems(Player player, Map currentMap) {
        // Check for nearby items to display tooltips
        int playerRow = player.getRow();
        int playerCol = player.getCol();

        for (int r = playerRow - 1; r <= playerRow + 1; r++) {
            for (int c = playerCol - 1; c <= playerCol + 1; c++) {
                if (r >= 0 && r < currentMap.getHeight() &&
                        c >= 0 && c < currentMap.getWidth()) {

                    GameObject obj = currentMap.getObjectAt(r, c);
                    if (obj instanceof Collectible) {
                        // Add particles around items for visual effect
                        addParticle(c * TILE_SIZE + TILE_SIZE/2,
                                r * TILE_SIZE + TILE_SIZE/2,
                                new Color(255, 255, 100, 150));
                    }
                }
            }
        }
    }

    public void addParticle(int x, int y, Color color) {
        particles.add(new ParticleEffect(x, y, color));
    }

    private void updateParticles() {
        // Update and remove dead particles
        for (int i = particles.size() - 1; i >= 0; i--) {
            ParticleEffect p = particles.get(i);
            p.update();
            if (!p.isAlive()) {
                particles.remove(i);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill the background with a repeated pattern
        for (int x = 0; x < getWidth(); x += 100) {
            for (int y = 0; y < getHeight(); y += 100) {
                g.drawImage(backgroundImage, x, y, this);
            }
        }

        Map currentMap = gameWorld.getCurrentMap();
        GameObject[][] grid = currentMap.getGrid();

        // Draw the grid
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                int x = col * TILE_SIZE;
                int y = row * TILE_SIZE;

                // Draw floor everywhere first
                g.drawImage(images.get("floor"), x, y, this);

                // Draw the game object
                GameObject obj = grid[row][col];
                if (obj instanceof Wall) {
                    g.drawImage(images.get("wall"), x, y, this);
                } else if (obj instanceof Door) {
                    Door door = (Door) obj;
                    if (door.isLocked()) {
                        g.drawImage(images.get("lockedDoor"), x, y, this);
                    } else {
                        g.drawImage(images.get("door"), x, y, this);
                    }
                } else if (obj instanceof Collectible) {
                    Collectible collectible = (Collectible) obj;
                    Item item = collectible.getItem();
                    if (item instanceof Key) {
                        g.drawImage(images.get("key"), x, y, this);
                    } else if (item instanceof Potion) {
                        g.drawImage(images.get("potion"), x, y, this);
                    } else if (item instanceof Treasure) {
                        g.drawImage(images.get("treasure"), x, y, this);
                    }
                } else if (obj instanceof Exit) {
                    g.drawImage(images.get("exit"), x, y, this);
                } else if (obj instanceof Enemy) {
                    g.drawImage(images.get("enemy"), x, y, this);
                } else if (obj instanceof Trap) {
                    g.drawImage(images.get("trap"), x, y, this);
                }
            }
        }

        // Draw particles
        for (ParticleEffect p : particles) {
            p.draw(g2d);
        }

        // Draw the player with animation
        Player player = gameWorld.getPlayer();
        int playerX = player.getCol() * TILE_SIZE;
        int playerY = player.getRow() * TILE_SIZE;
        g.drawImage(playerAnimation.getCurrentFrame(), playerX, playerY, this);

        // Draw visual indicator for player direction
        Direction playerDir = player.getDirection();
        g2d.setColor(Color.WHITE);
        int centerX = playerX + TILE_SIZE / 2;
        int centerY = playerY + TILE_SIZE / 2;
        int indicatorSize = TILE_SIZE / 5;

        switch (playerDir) {
            case UP:
                g2d.fillOval(centerX - indicatorSize/2, playerY + 5, indicatorSize, indicatorSize);
                break;
            case DOWN:
                g2d.fillOval(centerX - indicatorSize/2, playerY + TILE_SIZE - 5 - indicatorSize, indicatorSize, indicatorSize);
                break;
            case LEFT:
                g2d.fillOval(playerX + 5, centerY - indicatorSize/2, indicatorSize, indicatorSize);
                break;
            case RIGHT:
                g2d.fillOval(playerX + TILE_SIZE - 5 - indicatorSize, centerY - indicatorSize/2, indicatorSize, indicatorSize);
                break;
        }

        // Draw fog of war (hide unexplored areas)
        drawFogOfWar(g2d, player, currentMap);

        // Draw grid lines if enabled
        if (showGrid) {
            g2d.setColor(new Color(0, 0, 0, 50));
            for (int row = 0; row <= grid.length; row++) {
                g2d.drawLine(0, row * TILE_SIZE, grid[0].length * TILE_SIZE, row * TILE_SIZE);
            }
            for (int col = 0; col <= grid[0].length; col++) {
                g2d.drawLine(col * TILE_SIZE, 0, col * TILE_SIZE, grid.length * TILE_SIZE);
            }
        }

        // Draw minimap if enabled
        if (showMinimap) {
            drawMinimap(g2d);
        }

        // Draw current objective text
        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String objective = "Objective: " + gameWorld.getCurrentObjective();
        g2d.drawString(objective, 10, 20);

        // Draw item tooltips
        drawItemTooltips(g2d, player, currentMap);
    }

    private void drawFogOfWar(Graphics2D g, Player player, Map currentMap) {
        // Implement fog of war - only show areas the player has explored
        boolean[][] explored = player.getExploredTiles();
        int visionRadius = player.getVisionRadius();
        int playerRow = player.getRow();
        int playerCol = player.getCol();

        // Mark current visible tiles as explored
        for (int r = Math.max(0, playerRow - visionRadius);
             r <= Math.min(currentMap.getHeight() - 1, playerRow + visionRadius);
             r++) {
            for (int c = Math.max(0, playerCol - visionRadius);
                 c <= Math.min(currentMap.getWidth() - 1, playerCol + visionRadius);
                 c++) {

                // Check if the tile is visible (simple distance check)
                double distance = Math.sqrt(Math.pow(r - playerRow, 2) + Math.pow(c - playerCol, 2));
                if (distance <= visionRadius) {
                    explored[r][c] = true;
                }
            }
        }

        // Draw dark overlay for unexplored areas
        g.setColor(new Color(0, 0, 0, 200));
        for (int r = 0; r < currentMap.getHeight(); r++) {
            for (int c = 0; c < currentMap.getWidth(); c++) {
                if (!explored[r][c]) {
                    g.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // Draw semi-transparent overlay for explored but not currently visible areas
        g.setColor(new Color(0, 0, 0, 100));
        for (int r = 0; r < currentMap.getHeight(); r++) {
            for (int c = 0; c < currentMap.getWidth(); c++) {
                if (explored[r][c]) {
                    double distance = Math.sqrt(Math.pow(r - playerRow, 2) + Math.pow(c - playerCol, 2));
                    if (distance > visionRadius) {
                        g.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }
    }

    private void drawMinimap(Graphics2D g) {
        Map currentMap = gameWorld.getCurrentMap();
        GameObject[][] grid = currentMap.getGrid();
        Player player = gameWorld.getPlayer();
        boolean[][] explored = player.getExploredTiles();

        // Calculate minimap dimensions and position
        int mapWidth = grid[0].length;
        int mapHeight = grid.length;
        int minimapSize = 120;
        int tileSize = minimapSize / Math.max(mapWidth, mapHeight);
        int miniX = getWidth() - minimapSize - 10;
        int miniY = 10;

        // Draw minimap background
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(miniX - 5, miniY - 5, minimapSize + 10, minimapSize + 10);
        g.setColor(new Color(50, 50, 50));
        g.fillRect(miniX, miniY, mapWidth * tileSize, mapHeight * tileSize);

        // Draw explored areas
        for (int r = 0; r < mapHeight; r++) {
            for (int c = 0; c < mapWidth; c++) {
                if (explored[r][c]) {
                    int x = miniX + c * tileSize;
                    int y = miniY + r * tileSize;

                    // Draw different colored squares based on object type
                    GameObject obj = grid[r][c];
                    if (obj instanceof Wall) {
                        g.setColor(Color.DARK_GRAY);
                    } else if (obj instanceof Door) {
                        Door door = (Door) obj;
                        g.setColor(door.isLocked() ? Color.RED : Color.GREEN);
                    } else if (obj instanceof Collectible) {
                        g.setColor(Color.YELLOW);
                    } else if (obj instanceof Exit) {
                        g.setColor(Color.CYAN);
                    } else if (obj instanceof Enemy) {
                        g.setColor(Color.RED);
                    } else if (obj instanceof Trap) {
                        g.setColor(Color.MAGENTA);
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                    }

                    g.fillRect(x, y, tileSize, tileSize);
                }
            }
        }

        // Draw player position on minimap
        g.setColor(Color.BLUE);
        g.fillRect(miniX + player.getCol() * tileSize, miniY + player.getRow() * tileSize,
                tileSize, tileSize);

        // Draw minimap border
        g.setColor(Color.WHITE);
        g.drawRect(miniX - 5, miniY - 5, minimapSize + 10, minimapSize + 10);

        // Draw minimap label
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("MINIMAP", miniX + 5, miniY - 10);
    }

    private void drawItemTooltips(Graphics2D g, Player player, Map currentMap) {
        int playerRow = player.getRow();
        int playerCol = player.getCol();

        // Check nearby cells for items
        for (int r = playerRow - 1; r <= playerRow + 1; r++) {
            for (int c = playerCol - 1; c <= playerCol + 1; c++) {
                if (r >= 0 && r < currentMap.getHeight() &&
                        c >= 0 && c < currentMap.getWidth()) {

                    GameObject obj = currentMap.getObjectAt(r, c);
                    if (obj instanceof Collectible) {
                        Collectible collectible = (Collectible) obj;
                        Item item = collectible.getItem();

                        // Draw tooltip above the item
                        g.setColor(new Color(0, 0, 0, 180));
                        String tooltip = item.getName();
                        FontMetrics fm = g.getFontMetrics();
                        int textWidth = fm.stringWidth(tooltip);

                        int x = c * TILE_SIZE + TILE_SIZE/2 - textWidth/2;
                        int y = r * TILE_SIZE - 5;

                        g.fillRoundRect(x - 5, y - 15, textWidth + 10, 20, 5, 5);
                        g.setColor(Color.WHITE);
                        g.drawString(tooltip, x, y);
                    }
                }
            }
        }
    }

    public void toggleMinimap() {
        showMinimap = !showMinimap;
        repaint();
    }

    public void toggleGrid() {
        showGrid = !showGrid;
        repaint();
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public String getCurrentMapName() {
        return currentMapName;
    }

    public void setCurrentMapName(String name) {
        currentMapName = name;
    }
}
