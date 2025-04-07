package mansionescape;

import javax.swing.*;
import java.util.ArrayList;

class Player {
    private int row;
    private int col;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private int experience;
    private int level;
    private Direction direction;
    private ArrayList<Item> inventory;
    private boolean[][] exploredTiles;
    private int visionRadius;

    public Player(int row, int col) {
        this.row = row;
        this.col = col;
        this.health = 100;
        this.maxHealth = 100;
        this.attack = 10;
        this.defense = 5;
        this.experience = 0;
        this.level = 1;
        this.direction = Direction.DOWN;
        this.inventory = new ArrayList<>();
        this.visionRadius = 4;

        // Initialize explored tiles (assuming max map size)
        this.exploredTiles = new boolean[50][50];
    }

    public void move(Direction dir, Map currentMap) {
        this.direction = dir;

        int newRow = row;
        int newCol = col;

        switch (dir) {
            case UP:
                newRow--;
                break;
            case DOWN:
                newRow++;
                break;
            case LEFT:
                newCol--;
                break;
            case RIGHT:
                newCol++;
                break;
        }

        // Check if the move is valid
        if (newRow >= 0 && newRow < currentMap.getHeight() &&
                newCol >= 0 && newCol < currentMap.getWidth()) {

            GameObject obj = currentMap.getObjectAt(newRow, newCol);

            // Check if the space is empty or interactable
            if (obj == null) {
                row = newRow;
                col = newCol;
            } else if (obj instanceof Trap) {
                // Step on trap
                Trap trap = (Trap) obj;
                trap.trigger(this);

                // Remove the trap after triggering
                currentMap.removeObject(newRow, newCol);

                // Move to the new position
                row = newRow;
                col = newCol;

                SoundManager.playSound("hurt");
            }
        }
    }

    public void interact(GameWorld gameWorld, GameWindow window) {
        Map currentMap = gameWorld.getCurrentMap();

        // Check for interactable objects in front of the player
        int targetRow = row;
        int targetCol = col;

        switch (direction) {
            case UP:
                targetRow--;
                break;
            case DOWN:
                targetRow++;
                break;
            case LEFT:
                targetCol--;
                break;
            case RIGHT:
                targetCol++;
                break;
        }

        // Check if the target position is valid
        if (targetRow >= 0 && targetRow < currentMap.getHeight() &&
                targetCol >= 0 && targetCol < currentMap.getWidth()) {

            GameObject obj = currentMap.getObjectAt(targetRow, targetCol);

            if (obj != null) {
                obj.interact(this, gameWorld, window);
            }
        }
    }

    public void attack(GameWorld gameWorld, GameWindow window) {
        Map currentMap = gameWorld.getCurrentMap();

        // Check for enemies in front of the player
        int targetRow = row;
        int targetCol = col;

        switch (direction) {
            case UP:
                targetRow--;
                break;
            case DOWN:
                targetRow++;
                break;
            case LEFT:
                targetCol--;
                break;
            case RIGHT:
                targetCol++;
                break;
        }

        // Check if the target position is valid
        if (targetRow >= 0 && targetRow < currentMap.getHeight() &&
                targetCol >= 0 && targetCol < currentMap.getWidth()) {

            GameObject obj = currentMap.getObjectAt(targetRow, targetCol);

            if (obj instanceof Enemy) {
                Enemy enemy = (Enemy) obj;

                // Calculate damage
                int damage = attack - enemy.getDefense();
                if (damage < 1) damage = 1;

                // Apply damage to enemy
                enemy.takeDamage(damage);

                // Show attack message
                window.addScore(damage);

                // Check if enemy is defeated
                if (enemy.getHealth() <= 0) {
                    // Remove enemy
                    currentMap.removeObject(targetRow, targetCol);

                    // Gain experience
                    gainExperience(enemy.getExperienceValue());

                    // Update score
                    window.addScore(enemy.getExperienceValue() * 2);

                    // Show defeat message
                    JOptionPane.showMessageDialog(window,
                            "You defeated the " + enemy.getName() + "!\n" +
                                    "Gained " + enemy.getExperienceValue() + " experience.",
                            "Victory", JOptionPane.INFORMATION_MESSAGE);

                    // Special case for dragon in treasure room
                    if (enemy.getName().equals("Dragon Guardian")) {
                        gameWorld.setCurrentObjective("Find the Ancient Crown and exit the dungeon");
                    }
                }

                // Play sound
                SoundManager.playSound("attack");
            }
        }
    }

    public void gainExperience(int exp) {
        experience += exp;

        // Check for level up (every 100 exp)
        if (experience / 100 > level - 1) {
            levelUp();
        }
    }

    public void levelUp() {
        level++;
        maxHealth += 20;
        health = maxHealth;
        attack += 5;
        defense += 2;

        // Increase vision radius every few levels
        if (level % 3 == 0) {
            visionRadius++;
        }

        JOptionPane.showMessageDialog(null,
                "Level Up! You are now level " + level + "\n" +
                        "Max Health: " + maxHealth + "\n" +
                        "Attack: " + attack + "\n" +
                        "Defense: " + defense,
                "Level Up", JOptionPane.INFORMATION_MESSAGE);

        SoundManager.playSound("victory");
    }

    public void takeDamage(int damage) {
        // Apply defense
        int actualDamage = damage - defense;
        if (actualDamage < 1) actualDamage = 1;

        health -= actualDamage;

        // Check for game over
        if (health <= 0) {
            health = 0;
            JOptionPane.showMessageDialog(null,
                    "You have been defeated!\nGame Over",
                    "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        SoundManager.playSound("hurt");
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public void addItem(Item item) {
        inventory.add(item);
        SoundManager.playSound("pickup");
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public void useItem(Item item) {
        if (item instanceof Potion) {
            Potion potion = (Potion) item;
            heal(potion.getHealAmount());
            removeItem(item);

            JOptionPane.showMessageDialog(null,
                    "Used " + potion.getName() + " and restored " + potion.getHealAmount() + " health!",
                    "Item Used", JOptionPane.INFORMATION_MESSAGE);
        } else if (item instanceof Key) {
            JOptionPane.showMessageDialog(null,
                    "You need to use the key on a locked door.",
                    "Cannot Use Item", JOptionPane.INFORMATION_MESSAGE);
        } else if (item instanceof Treasure) {
            JOptionPane.showMessageDialog(null,
                    "You admire the " + item.getName() + ". It looks very valuable!",
                    "Treasure", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public boolean hasKey(String doorName) {
        for (Item item : inventory) {
            if (item instanceof Key) {
                Key key = (Key) item;
                if (key.getName().contains(doorName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Key getKey(String doorName) {
        for (Item item : inventory) {
            if (item instanceof Key) {
                Key key = (Key) item;
                if (key.getName().contains(doorName)) {
                    return key;
                }
            }
        }
        return null;
    }

    // Getters and setters
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public boolean[][] getExploredTiles() {
        return exploredTiles;
    }

    public int getVisionRadius() {
        return visionRadius;
    }
}
