package mansionescape;

class Wall extends GameObject {
    public Wall() {
        super("Wall");
    }

    @Override
    public void interact(Player player, GameWorld gameWorld, GameWindow window) {
        // Wall doesn't do anything when interacted with
    }
}
