package mansionescape;

import java.awt.*;

class Animation {
    private Image[] frames;
    private int currentFrame;
    private long lastFrameTime;
    private long frameDuration; // in milliseconds

    public Animation(Image[] frames, long frameDuration) {
        this.frames = frames;
        this.frameDuration = frameDuration;
        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastFrameTime > frameDuration) {
            currentFrame = (currentFrame + 1) % frames.length;
            lastFrameTime = now;
        }
    }

    public Image getCurrentFrame() {
        return frames[currentFrame];
    }
}
