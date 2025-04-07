package mansionescape;

import javax.swing.*;

class Collectible extends GameObject {
    private Item item;

    public Collectible(Item item) {
        super(item.getName());
        this.item = item;
    }

    @Override
    public void interact(Player player, GameWorld gameWorld, GameWindow window) {
        // Add item to player's inventory
        player.addItem(item);

        // Remove collectible from map
        Map currentMap = gameWorld.getCurrentMap();
        currentMap.removeObject(player.getRow() + player.getDirection().ordinal() % 2 * (player.getDirection().ordinal() - 1),
                player.getCol() + (1 - player.getDirection().ordinal() % 2) * (player.getDirection().ordinal() - 2));

        // Show message
        JOptionPane.showMessageDialog(window,
                "You found " + item.getName() + "!",
                "Item Found", JOptionPane.INFORMATION_MESSAGE);

        // Update score
        if (item instanceof Treasure) {
            Treasure treasure = (Treasure) item;
            window.addScore(treasure.getValue());

            // Set new objective if this is the main treasure
            if (treasure.getName().equals("Ancient Crown")) {
                gameWorld.setCurrentObjective("Find the exit to escape the dungeon");
            }
        } else {
            window.addScore(10);
        }

        // Play sound
        SoundManager.playSound("pickup");
    }

    public Item getItem() {
        return item;
    }
}
