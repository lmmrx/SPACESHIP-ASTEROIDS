import javax.sound.sampled.*; // Import classes for handling audio data and playback.
import java.io.File; // Import the File class for file operations.
import java.io.IOException; // Import the IOException class for handling input/output exceptions.
import java.util.HashMap; // Import the HashMap class for storing key-value pairs.
import java.util.Map; // Import the Map interface for key-value pair storage.

public class AudioManager { // Class for managing audio playback.
    private static Map<String, Clip> audioClips = new HashMap<>(); // A map to store audio clips, using file paths as keys.

    // Method to load a sound file into memory.
    public static void loadSound(String filePath) {
        try {
            // Create an AudioInputStream from the specified file path.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            // Obtain a Clip object for audio playback.
            Clip clip = AudioSystem.getClip();
            // Open the clip with the audio input stream.
            clip.open(audioInputStream);
            // Store the clip in the map with the file path as the key.
            audioClips.put(filePath, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Print stack trace if an exception occurs during loading.
            e.printStackTrace();
        }
    }

    // Method to play a sound file.
    public static void playSound(String filePath, boolean loop) {
        // Run the playback in a new thread.
        new Thread(() -> {
            // Get the clip associated with the file path from the map.
            Clip clip = audioClips.get(filePath);
            // If the clip is not loaded, load it.
            if (clip == null) {
                loadSound(filePath);
                clip = audioClips.get(filePath);
            }
            // If the clip is available, start playback.
            if (clip != null) {
                clip.setFramePosition(0); // Reset playback position to the start.
                // Play the clip in loop if requested, otherwise play once.
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            }
        }).start(); // Start the new thread.
    }

    // Method to stop playback of a specific sound file.
    public static void stopSound(String filePath) {
        // Get the clip associated with the file path from the map.
        Clip clip = audioClips.get(filePath);
        // If the clip is running, stop and flush it.
        if (clip != null && clip.isRunning()) {
            clip.stop(); // Stop playback.
            clip.flush(); // Clear any remaining audio data.
        }
    }

    // Method to stop all currently playing sounds.
    public static void stopAllSounds() {
        // Iterate over all clips in the map.
        for (Clip clip : audioClips.values()) {
            // If a clip is running, stop and flush it.
            if (clip != null && clip.isRunning()) {
                clip.stop(); // Stop playback.
                clip.flush(); // Clear any remaining audio data.
            }
        }
        // Clear the map to remove all clips.
        audioClips.clear();
    }
}
