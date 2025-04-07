package mansionescape;

import java.awt.*;

class ParticleEffect {
    private double x, y;
    private double vx, vy;
    private Color color;
    private int size;
    private int life;
    private int maxLife;

    public ParticleEffect(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        // Random velocity
        this.vx = -0.5 + Math.random();
        this.vy = -1 - Math.random();

        // Random size
        this.size = 1 + (int)(Math.random() * 5);

        // Random lifetime
        this.maxLife = 20 + (int)(Math.random() * 30);
        this.life = this.maxLife;
    }

    public void update() {
        // Apply physics
        x += vx;
        y += vy;
        vy += 0.05; // gravity

        // Decrease life
        life--;
    }

    public void draw(Graphics2D g) {
        // Calculate alpha based on remaining life
        int alpha = (int)(life * 255 / maxLife);
        Color drawColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        g.setColor(drawColor);
        g.fillOval((int)x, (int)y, size, size);
    }

    public boolean isAlive() {
        return life > 0;
    }
}
