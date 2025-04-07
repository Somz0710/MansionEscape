package mansionescape;

import javax.swing.*;

class Exit extends GameObject {
    private String targetScreen;

    public Exit(String targetScreen) {
        super("Exit");
        this.targetScreen = targetScreen;
    }

    @Override
    public void interact(Player player, GameWorld gameWorld, GameWindow window) {
        // Check if player has the treasure
        boolean hasTreasure = false;
        for (Item item : player.getInventory()) {
            if (item instanceof Treasure) {
                hasTreasure = true;
                break;
            }
        }

        if (hasTreasure) {
            // Game completed!
            SoundManager.playSound("victory");

            // Calculate score
            int score = player.getExperience() * 10;
            for (Item item : player.getInventory()) {
                if (item instanceof Treasure) {
                    Treasure treasure = (Treasure) item;
                    score += treasure.getValue();
                }
            }

            // Show victory message with final score
            JOptionPane.showMessageDialog(window,
                    "Congratulations! You have escaped the dungeon with the treasure!\n\n" +
                            "Final Score: " + score + "\n" +
                            "Level Reached: " + player.getLevel() + "\n" +
                            "Treasures Found: Ancient Crown\n\n" +
                            "Thank you for playing Adventure Quest!",
                    "Victory", JOptionPane.INFORMATION_MESSAGE);

            // Exit game
            System.exit(0);
        } else {
            JOptionPane.showMessageDialog(window,
                    "You need to find the Ancient Crown before leaving!",
                    "Cannot Exit", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
