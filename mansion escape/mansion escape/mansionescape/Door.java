package mansionescape;

import javax.swing.*;

class Door extends GameObject {
    private String targetMap;
    private int targetRow;
    private int targetCol;
    private boolean locked;

    public Door(String targetMap, int targetRow, int targetCol, boolean locked) {
        super("Door to " + targetMap);
        this.targetMap = targetMap;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
        this.locked = locked;
    }

    @Override
    public void interact(Player player, GameWorld gameWorld, GameWindow window) {
        if (locked) {
            // Check if player has the key
            if (player.hasKey(targetMap)) {
                // Unlock door
                locked = false;
                Key key = player.getKey(targetMap);
                player.removeItem(key);

                JOptionPane.showMessageDialog(window,
                        "You used the " + key.getName() + " to unlock the door.",
                        "Door Unlocked", JOptionPane.INFORMATION_MESSAGE);

                SoundManager.playSound("unlock");
            } else {
                JOptionPane.showMessageDialog(window,
                        "This door is locked. You need a key to unlock it.",
                        "Locked Door", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Change map
            gameWorld.changeMap(targetMap, targetRow, targetCol);
            window.setCurrentMapName(targetMap);
        }
    }

    public boolean isLocked() {
        return locked;
    }
}
