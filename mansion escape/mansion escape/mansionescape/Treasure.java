package mansionescape;

class Treasure extends Item {
    private int value;

    public Treasure(String name, int value) {
        super(name);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
