package mansionescape;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class GameWindow extends JFrame {
    private GamePanel gamePanel;
    private JPanel infoPanel;
    private JLabel statusLabel;
    private JLabel inventoryLabel;
    private JProgressBar healthBar;
    private JProgressBar expBar;
    private JLabel levelLabel;
    private JPanel controlPanel;
    private Timer gameTimer;
    private long gameStartTime;
    private JLabel timeLabel;
    private JLabel scoreLabel;
    private int score = 0;

    public GameWindow() {
        setTitle("Adventure Quest: The Lost Treasure");
        setSize(800, 700);  // Increased height for control panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // Center on screen

        gamePanel = new GamePanel();
        infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setPreferredSize(new Dimension(800, 30));
        infoPanel.setBorder(BorderFactory.createEtchedBorder());

        // Create status panel on the left
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Explore the dungeon. Find keys and escape with the treasure!");
        statusLabel.setPreferredSize(new Dimension(500, 20));
        leftPanel.add(statusLabel);

        // Create inventory panel on the right
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inventoryLabel = new JLabel("Inventory: None");
        inventoryLabel.setPreferredSize(new Dimension(250, 20));
        rightPanel.add(inventoryLabel);

        infoPanel.add(leftPanel, BorderLayout.WEST);
        infoPanel.add(rightPanel, BorderLayout.EAST);

        // Create control panel at the bottom
        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setPreferredSize(new Dimension(800, 70));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Add health bar to control panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 5));

        JPanel healthPanel = new JPanel(new BorderLayout());
        healthPanel.add(new JLabel("Health:"), BorderLayout.WEST);
        healthBar = new JProgressBar(0, 100);
        healthBar.setValue(100);
        healthBar.setStringPainted(true);
        healthBar.setForeground(Color.RED);
        healthPanel.add(healthBar, BorderLayout.CENTER);

        JPanel expPanel = new JPanel(new BorderLayout());
        expPanel.add(new JLabel("EXP:"), BorderLayout.WEST);
        expBar = new JProgressBar(0, 100);
        expBar.setValue(0);
        expBar.setStringPainted(true);
        expBar.setForeground(Color.BLUE);
        expPanel.add(expBar, BorderLayout.CENTER);

        levelLabel = new JLabel("Level: 1");

        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.add(new JLabel("Time:"), BorderLayout.WEST);
        timeLabel = new JLabel("00:00");
        timePanel.add(timeLabel, BorderLayout.CENTER);

        scoreLabel = new JLabel("Score: 0");

        JPanel soundPanel = new JPanel(new BorderLayout());
        JCheckBox soundToggle = new JCheckBox("Sound", true);
        soundToggle.addActionListener(e -> SoundManager.toggleSound());
        soundPanel.add(soundToggle, BorderLayout.CENTER);

        statsPanel.add(healthPanel);
        statsPanel.add(timePanel);
        statsPanel.add(scoreLabel);
        statsPanel.add(expPanel);
        statsPanel.add(levelLabel);
        statsPanel.add(soundPanel);

        controlPanel.add(statsPanel, BorderLayout.CENTER);

        // Add buttons for actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton helpButton = new JButton("Help (H)");
        helpButton.addActionListener(e -> showHelp());

        JButton inventoryButton = new JButton("Inventory (I)");
        inventoryButton.addActionListener(e -> showInventory());

        buttonPanel.add(inventoryButton);
        buttonPanel.add(helpButton);

        controlPanel.add(buttonPanel, BorderLayout.EAST);

        // Add panels to frame
        add(gamePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);

        // Set up keyboard input
        gamePanel.addKeyListener(new GameKeyListener(gamePanel, this));
        gamePanel.setFocusable(true);

        // Start game timer
        gameStartTime = System.currentTimeMillis();
        gameTimer = new Timer(1000, e -> updateGameTime());
        gameTimer.start();

        // Update game status in UI
        new Timer(100, e -> {
            Player player = gamePanel.getGameWorld().getPlayer();
            updateStatus(player);
            updateInventory(player);
            updateHealthAndExp(player);
        }).start();
    }

    private void updateGameTime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - gameStartTime) / 1000;

        long minutes = elapsedTime / 60;
        long seconds = elapsedTime % 60;

        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateStatus(Player player) {
        statusLabel.setText("Location: " + gamePanel.getCurrentMapName() + " | Health: " + player.getHealth() + "/" + player.getMaxHealth());
    }

    private void updateInventory(Player player) {
        StringBuilder sb = new StringBuilder("Inventory: ");
        ArrayList<Item> items = player.getInventory();
        if (items.isEmpty()) {
            sb.append("None");
        } else {
            for (int i = 0; i < items.size(); i++) {
                sb.append(items.get(i).getName());
                if (i < items.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        inventoryLabel.setText(sb.toString());
    }

    private void updateHealthAndExp(Player player) {
        healthBar.setValue(player.getHealth());
        healthBar.setString(player.getHealth() + "/" + player.getMaxHealth());

        expBar.setValue(player.getExperience() % 100);
        expBar.setString(player.getExperience() % 100 + "/100");

        levelLabel.setText("Level: " + player.getLevel());
    }

    public void showHelp() {
        JOptionPane.showMessageDialog(this,
                "ADVENTURE QUEST: THE LOST TREASURE\n\n" +
                        "CONTROLS:\n" +
                        "- WASD or Arrow Keys: Move your character\n" +
                        "- E: Interact with objects and NPCs\n" +
                        "- SPACE: Attack enemies or use active item\n" +
                        "- I: Open inventory menu\n" +
                        "- M: Toggle minimap visibility\n" +
                        "- ESC: Pause game\n" +
                        "- H: Show this help screen\n\n" +
                        "HINTS:\n" +
                        "- Find keys to unlock doors\n" +
                        "- Collect potions to restore health\n" +
                        "- Defeat enemies to earn experience and level up\n" +
                        "- Discover the ancient treasure and find the exit\n" +
                        "- Watch out for traps and puzzles!\n\n" +
                        "Good luck on your adventure!",
                "Game Help", JOptionPane.INFORMATION_MESSAGE);
        gamePanel.requestFocus();
    }

    public void showInventory() {
        Player player = gamePanel.getGameWorld().getPlayer();
        ArrayList<Item> items = player.getInventory();

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your inventory is empty!", "Inventory", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JPanel panel = new JPanel(new BorderLayout());

            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Item item : items) {
                listModel.addElement(item.getName());
            }

            JList<String> itemList = new JList<>(listModel);
            itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(itemList);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton useButton = new JButton("Use Item");
            JButton dropButton = new JButton("Drop Item");
            JButton cancelButton = new JButton("Cancel");

            useButton.addActionListener(e -> {
                int index = itemList.getSelectedIndex();
                if (index >= 0) {
                    Item item = items.get(index);
                    player.useItem(item);
                    if (item instanceof Potion) {
                        SoundManager.playSound("potion");
                        score += 10;
                        scoreLabel.setText("Score: " + score);
                    }
                    JOptionPane.getRootFrame().dispose();
                }
            });

            dropButton.addActionListener(e -> {
                int index = itemList.getSelectedIndex();
                if (index >= 0) {
                    player.removeItem(items.get(index));
                    JOptionPane.getRootFrame().dispose();
                    showInventory(); // Refresh inventory view
                }
            });

            cancelButton.addActionListener(e -> {
                JOptionPane.getRootFrame().dispose();
            });

            buttonPanel.add(useButton);
            buttonPanel.add(dropButton);
            buttonPanel.add(cancelButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            JOptionPane.showMessageDialog(this, panel, "Inventory", JOptionPane.PLAIN_MESSAGE);
        }

        gamePanel.requestFocus();
    }

    public void addScore(int points) {
        score += points;
        scoreLabel.setText("Score: " + score);
    }

    public void pauseGame() {
        gameTimer.stop();

        Object[] options = {"Resume", "Help", "Quit"};
        int choice = JOptionPane.showOptionDialog(this,
                "Game Paused",
                "Pause Menu",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0: // Resume
                break;
            case 1: // Help
                showHelp();
                break;
            case 2: // Quit
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to quit? Your progress will be lost.",
                        "Confirm Quit",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                break;
        }

        gameTimer.start();
        gamePanel.requestFocus();
    }

    public void setCurrentMapName(String targetMap) {
    }
}

