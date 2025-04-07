package mansionescape;

import java.util.HashMap;

class GameWorld {
    private HashMap<String, Map> maps;
    private Map currentMap;
    private Player player;
    private String currentObjective;

    public GameWorld() {
        maps = new HashMap<>();
        initWorld();
    }

    public void initWorld() {
        // Create maps
        createDungeonEntranceMap();
        createMainHallMap();
        createTreasureRoomMap();

        // Set current map
        currentMap = maps.get("Dungeon Entrance");

        // Create player
        player = new Player(1, 1);
        player.setDirection(Direction.DOWN);

        // Set initial objective
        currentObjective = "Find the key to the Main Hall";
    }

    private void createDungeonEntranceMap() {
        Map entranceMap = new Map(10, 10);
        maps.put("Dungeon Entrance", entranceMap);

        // Fill with walls
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (r == 0 || r == 9 || c == 0 || c == 9) {
                    entranceMap.setObject(r, c, new Wall());
                }
            }
        }

        // Add a locked door to the next map
        Door door = new Door("Main Hall", 1, 1, true);
        entranceMap.setObject(5, 9, door);

        // Add key
        Key key = new Key("Main Hall Key");
        entranceMap.setObject(8, 1, new Collectible(key));

        // Add potion
        Potion potion = new Potion("Health Potion", 25);
        entranceMap.setObject(3, 7, new Collectible(potion));

        // Add enemies
        entranceMap.setObject(6, 3, new Enemy("Goblin", 20, 5, 10));
        entranceMap.setObject(2, 8, new Enemy("Skeleton", 15, 3, 15));

        // Add trap
        entranceMap.setObject(4, 4, new Trap("Spike Trap", 10));
    }

    private void createMainHallMap() {
        Map hallMap = new Map(12, 15);
        maps.put("Main Hall", hallMap);

        // Fill with walls
        for (int r = 0; r < 12; r++) {
            for (int c = 0; c < 15; c++) {
                if (r == 0 || r == 11 || c == 0 || c == 14) {
                    hallMap.setObject(r, c, new Wall());
                }
            }
        }

        // Add entrance door
        Door entranceDoor = new Door("Dungeon Entrance", 5, 8, false);
        hallMap.setObject(1, 1, entranceDoor);

        // Add exit door
        Door exitDoor = new Door("Treasure Room", 1, 7, true);
        hallMap.setObject(11, 7, exitDoor);

        // Add key
        Key key = new Key("Treasure Room Key");
        hallMap.setObject(3, 12, new Collectible(key));

        // Add potions
        Potion potion1 = new Potion("Health Potion", 25);
        hallMap.setObject(5, 5, new Collectible(potion1));

        Potion potion2 = new Potion("Greater Health Potion", 50);
        hallMap.setObject(9, 10, new Collectible(potion2));

        // Add enemies
        hallMap.setObject(4, 8, new Enemy("Orc Warrior", 35, 8, 25));
        hallMap.setObject(8, 4, new Enemy("Dark Mage", 25, 12, 30));
        hallMap.setObject(6, 12, new Enemy("Zombie", 40, 6, 20));

        // Add traps
        hallMap.setObject(3, 3, new Trap("Flame Trap", 15));
        hallMap.setObject(9, 9, new Trap("Poison Trap", 20));
    }

    private void createTreasureRoomMap() {
        Map treasureMap = new Map(8, 15);
        maps.put("Treasure Room", treasureMap);

        // Fill with walls
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 15; c++) {
                if (r == 0 || r == 7 || c == 0 || c == 14) {
                    treasureMap.setObject(r, c, new Wall());
                }
            }
        }

        // Add entrance door
        Door entranceDoor = new Door("Main Hall", 11, 6, false);
        treasureMap.setObject(1, 7, entranceDoor);

        // Add treasure
        Treasure treasure = new Treasure("Ancient Crown", 1000);
        treasureMap.setObject(4, 7, new Collectible(treasure));

        // Add exit
        treasureMap.setObject(7, 7, new Exit("Victory"));

        // Add final boss
        treasureMap.setObject(5, 7, new Enemy("Dragon Guardian", 100, 15, 100));

        // Add potions
        Potion potion1 = new Potion("Supreme Health Potion", 75);
        treasureMap.setObject(2, 3, new Collectible(potion1));

        Potion potion2 = new Potion("Supreme Health Potion", 75);
        treasureMap.setObject(2, 11, new Collectible(potion2));
    }

    public Player getPlayer() {
        return player;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(String mapName) {
        currentMap = maps.get(mapName);
    }

    public String getCurrentObjective() {
        return currentObjective;
    }

    public void setCurrentObjective(String objective) {
        currentObjective = objective;
    }

    public void changeMap(String mapName, int playerRow, int playerCol) {
        // Set the current map
        currentMap = maps.get(mapName);

        // Update player position
        player.setRow(playerRow);
        player.setCol(playerCol);

        // Update objectives based on the map
        switch (mapName) {
            case "Dungeon Entrance":
                currentObjective = "Find the key to the Main Hall";
                break;
            case "Main Hall":
                currentObjective = "Find the key to the Treasure Room";
                break;
            case "Treasure Room":
                currentObjective = "Defeat the Dragon and claim the treasure";
                break;
            case "Victory":
                currentObjective = "Congratulations! You have completed the game!";
                break;
        }

        // Play sound
        SoundManager.playSound("unlock");
    }
}
