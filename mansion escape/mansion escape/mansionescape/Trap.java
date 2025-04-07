package mansionescape;

import javax.swing.*;

class Trap extends GameObject {
    private int damage;

    public Trap(String name, int damage) {
        super(name);
        this.damage = damage;
    }

    @Override
    public void interact(Player player, GameWorld gameWorld, GameWindow window) {
        // Nothing happens when interacting directly
    }

    public void trigger(Player player) {
        player.takeDamage(damage);

        JOptionPane.showMessageDialog(null,
                "You triggered a " + getName() + " and took " + damage + " damage!",
                "Trap Triggered", JOptionPane.WARNING_MESSAGE);
    }
}