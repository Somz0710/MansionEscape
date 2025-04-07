package mansionescape;

import javax.swing.*;

class Enemy extends GameObject {
    private int health;
    private int attack;
    private int defense;
    private int experienceValue;

    public Enemy(String name, int health, int attack, int experienceValue) {
        super(name);
        this.health = health;
        this.attack = attack;
        this.defense = 0;
        this.experienceValue = experienceValue;
    }

    @Override
    public void interact(Player player, GameWorld gameWorld, GameWindow window) {
        // When player interacts with enemy, attack them
        player.takeDamage(attack);

        JOptionPane.showMessageDialog(window,
                "The " + getName() + " attacks you for " + attack + " damage!",
                "Enemy Attack", JOptionPane.WARNING_MESSAGE);
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getExperienceValue() {
        return experienceValue;
    }
}
