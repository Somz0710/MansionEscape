package mansionescape;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

class SplashScreen extends JFrame {
    public SplashScreen() {
        setTitle("Adventure Quest: The Lost Treasure");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);
        setUndecorated(true); // Remove window decorations

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSplashScreen(g);
            }
        };
        add(panel);
    }

    private void drawSplashScreen(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Fill background with dark blue color
        g2d.setColor(new Color(20, 30, 50));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw title with attractive font
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();
        String title = "ADVENTURE QUEST";
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, 200);

        // Draw subtitle
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.ITALIC, 28));
        fm = g2d.getFontMetrics();
        String subtitle = "The Lost Treasure";
        int subtitleWidth = fm.stringWidth(subtitle);
        g2d.drawString(subtitle, (getWidth() - subtitleWidth) / 2, 250);

        // Draw decorative elements
        for (int i = 0; i < 20; i++) {
            int x = new Random().nextInt(getWidth());
            int y = new Random().nextInt(getHeight());
            int size = 1 + new Random().nextInt(3);
            g2d.setColor(new Color(255, 255, 100, 150));
            g2d.fillOval(x, y, size, size);
        }

        // Draw loading text
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        String loading = "Loading game...";
        g2d.drawString(loading, getWidth() - 150, getHeight() - 30);
    }
}

