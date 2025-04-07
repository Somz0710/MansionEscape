package mansionescape;

abstract class GameObject {
    private String name;

    public GameObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Basic interaction function, to be overridden by subclasses
    public abstract void interact(Player player, GameWorld gameWorld, GameWindow window);
}
