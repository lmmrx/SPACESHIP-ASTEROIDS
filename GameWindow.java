import javax.swing.*;

// Class to create the main game window for the Spaceship Asteroids game.
public class GameWindow extends JFrame {
    private GamePanel gamePanel; // The panel where the game is drawn and interacted with.

    // Constructor to initialize the game window.
    public GameWindow(GameConfig config) {
        setTitle("Spaceship Asteroids"); // Set the title of the window.
        setSize(800, 600); // Set the dimensions of the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application exits when the window is closed.
        gamePanel = new GamePanel(config); // Create a new GamePanel with the given configuration.
        add(gamePanel); // Add the GamePanel to the JFrame.
        setVisible(true); // Make the window visible.
    }

    // Method to start the game by delegating to the GamePanel.
    public void startGame() {
        gamePanel.startGame(); // Call the startGame method on the GamePanel to begin the game.
    }
}
