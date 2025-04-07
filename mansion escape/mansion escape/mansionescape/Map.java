package mansionescape;

class Map {
    private GameObject[][] grid;
    private int width;
    private int height;

    public Map(int height, int width) {
        this.width = width;
        this.height = height;
        grid = new GameObject[height][width];

        // Initialize with empty spaces
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                grid[r][c] = null;
            }
        }
    }

    public GameObject[][] getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setObject(int row, int col, GameObject obj) {
        if (row >= 0 && row < height && col >= 0 && col < width) {
            grid[row][col] = obj;
        }
    }

    public GameObject getObjectAt(int row, int col) {
        if (row >= 0 && row < height && col >= 0 && col < width) {
            return grid[row][col];
        }
        return null;
    }

    public void removeObject(int row, int col) {
        if (row >= 0 && row < height && col >= 0 && col < width) {
            grid[row][col] = null;
        }
    }
}
