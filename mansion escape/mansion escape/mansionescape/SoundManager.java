package mansionescape;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.util.HashMap;

class SoundManager {
    private static HashMap<String, Clip> sounds = new HashMap<>();
    private static boolean soundEnabled = true;

    static {
        // Initialize sounds - in a real game, these would be actual sound files
        createSilentClip("background"); // Background music
        createSilentClip("pickup");     // Item pickup sound
        createSilentClip("unlock");     // Door unlock sound
        createSilentClip("attack");     // Player attack sound
        createSilentClip("hurt");       // Player hurt sound
        createSilentClip("victory");    // Victory sound
        createSilentClip("potion");     // Potion use sound
    }

    private static void createSilentClip(String soundName) {
        try {
            // Create a silent clip for demonstration purposes
            AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
            byte[] silentData = new byte[format.getFrameSize() * 1];

            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(format, silentData, 0, silentData.length);

            sounds.put(soundName, clip);
        } catch (Exception e) {
            System.out.println("Error creating sound: " + e.getMessage());
        }
    }

    public static void playSound(String soundName) {
        if (!soundEnabled) return;

        Clip clip = sounds.get(soundName);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public static void loopSound(String soundName) {
        if (!soundEnabled) return;

        Clip clip = sounds.get(soundName);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static void stopSound(String soundName) {
        Clip clip = sounds.get(soundName);
        if (clip != null) {
            clip.stop();
        }
    }

    public static void toggleSound() {
        soundEnabled = !soundEnabled;
        if (!soundEnabled) {
            // Stop all sounds
            for (Clip clip : sounds.values()) {
                clip.stop();
            }
        } else {
            // Resume background music
            loopSound("background");
        }
    }
}
