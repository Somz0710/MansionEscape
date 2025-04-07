package mansionescape;

// File: AdventureGame.java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Adventure Quest: The Lost Treasure
 *
 * A 2D dungeon crawler game where you explore multiple interconnected maps,
 * collect items, solve puzzles, defeat enemies, and find the legendary treasure.
 *
 * Controls:
 * - WASD or Arrow Keys: Move character
 * - E: Interact with objects/NPCs
 * - SPACE: Attack or use active item
 * - I: Open inventory menu
 * - M: Toggle minimap
 * - ESC: Pause game
 * - H: Show help
 */
public class AdventureGame {
    public static void main(String[] args) {
        // Display welcome screen with game instructions
        JOptionPane.showMessageDialog(null,
                "ADVENTURE QUEST: THE LOST TREASURE\n\n" +
                        "Welcome brave adventurer! Your quest is to explore the dungeon,\n" +
                        "find the legendary treasure, and escape with your prize!\n\n" +
                        "CONTROLS:\n" +
                        "- WASD or Arrow Keys: Move your character\n" +
                        "- E: Interact with objects and NPCs\n" +
                        "- SPACE: Attack enemies or use active item\n" +
                        "- I: Open inventory menu\n" +
                        "- M: Toggle minimap visibility\n" +
                        "- ESC: Pause game\n" +
                        "- H: Show this help screen again\n\n" +
                        "HINTS:\n" +
                        "- Find keys to unlock doors\n" +
                        "- Collect potions to restore health\n" +
                        "- Defeat enemies to earn experience and level up\n" +
                        "- Discover the ancient treasure and find the exit\n" +
                        "- Watch out for traps and puzzles!\n\n" +
                        "Good luck on your adventure!",
                "Adventure Quest Instructions", JOptionPane.INFORMATION_MESSAGE);

        // Start the game with a splash screen
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);

        // Start the actual game after a short delay
        Timer timer = new Timer(3000, e -> {
            splash.dispose();
            GameWindow game = new GameWindow();
            game.setVisible(true);
            SoundManager.playSound("background");
        });
        timer.setRepeats(false);
        timer.start();
    }
}
