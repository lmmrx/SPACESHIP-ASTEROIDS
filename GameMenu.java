import java.awt.*; // Import all classes from the java.awt package for graphical operations.
import java.util.ArrayList; // Import ArrayList for managing lists.
import java.util.Collections; // Import Collections for sorting operations.
import java.util.Comparator; // Import Comparator for custom sorting.
import java.util.List; // Import List interface for list operations.
import javax.swing.*; // Import all classes from the javax.swing package for GUI components.
import java.io.BufferedReader; // Import BufferedReader for reading text from a file.
import java.io.FileReader; // Import FileReader for reading files.
import java.io.IOException; // Import IOException for handling I/O exceptions.

public class GameMenu extends JFrame { // Define the GameMenu class that extends JFrame for the game menu window.
    private int difficulty; // Variable to store the selected difficulty level.
    private String bgm = "D:\\College\\game deveploment\\FINAL GAME\\wav\\bgm1.wav"; // Path to background music file.
    private String imgPath = "D:\\College\\game deveploment\\FINAL GAME\\img\\"; // Path to image files.
    private String scorePath = "D:\\College\\game deveploment\\FINAL GAME\\"; // Path to score records.

    public GameMenu() { // Constructor to initialize the game menu.
        setTitle("Spaceship Asteroids"); // Set the title of the window.
        setSize(1024, 576); // Set the size of the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation for the window.
        setLayout(new BorderLayout()); // Set the layout manager for the window to BorderLayout.

        AudioManager.loadSound(bgm); // Load the background music file.

        JPanel backgroundPanel = new JPanel() { // Create a JPanel for the background.
            @Override
            protected void paintComponent(Graphics g) { // Override paintComponent to draw custom graphics.
                super.paintComponent(g); // Call the superclass's paintComponent method.
                ImageIcon icon = new ImageIcon(imgPath + "menu.jpg"); // Load the background image.
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this); // Draw the background image.
            }
        };
        backgroundPanel.setLayout(null); // Set the layout manager for the panel to null (absolute positioning).

        // Create buttons for the game menu.
        JButton easyButton = createButton("", 100, 280, 200, 50); // Button for easy difficulty.
        JButton mediumButton = createButton("", 380, 370, 250, 50); // Button for medium difficulty.
        JButton hardButton = createButton("", 700, 280, 200, 50); // Button for hard difficulty.
        JButton highScoreButton = createButton("Top 5 Players", 350, 460, 300, 50); // Button to view high scores.

        // Set hand cursor for all buttons.
        Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR); 
        easyButton.setCursor(handCursor); 
        mediumButton.setCursor(handCursor); 
        hardButton.setCursor(handCursor); 
        highScoreButton.setCursor(handCursor);

        // Add action listeners to buttons.
        easyButton.addActionListener(e -> { // Action listener for the easy button.
            difficulty = 0; // Set difficulty level to easy.
            startGame(); // Start the game with easy difficulty.
        });
        mediumButton.addActionListener(e -> { // Action listener for the medium button.
            difficulty = 1; // Set difficulty level to medium.
            startGame(); // Start the game with medium difficulty.
        });
        hardButton.addActionListener(e -> { // Action listener for the hard button.
            difficulty = 2; // Set difficulty level to hard.
            startGame(); // Start the game with hard difficulty.
        });
        highScoreButton.addActionListener(e -> showHighScores()); // Action listener for the high scores button.

        // Add buttons to the background panel.
        backgroundPanel.add(easyButton); 
        backgroundPanel.add(mediumButton); 
        backgroundPanel.add(hardButton); 
        backgroundPanel.add(highScoreButton);

        add(backgroundPanel); // Add the background panel to the frame.
        AudioManager.stopAllSounds(); // Stop all currently playing sounds.
        AudioManager.playSound(bgm, true); // Play background music in a loop.
        setVisible(true); // Make the window visible.
    }

    // Method to create a button with specified properties.
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text); // Create a new JButton with the specified text.
        button.setBounds(x, y, width, height); // Set the position and size of the button.
        button.setForeground(Color.WHITE); // Set the text color of the button.
        button.setFont(new Font("Monospaced", Font.BOLD, 20)); // Set the font of the button text.
        button.setOpaque(false); // Set the button to be transparent.
        button.setContentAreaFilled(false); // Disable button background filling.
        button.setBorderPainted(false); // Disable button border painting.
        return button; // Return the configured button.
    }

    // Method to start the game with the selected difficulty.
    private void startGame() {
        AudioManager.stopSound(bgm); // Stop the background music.
        dispose(); // Close the current game menu window.
        GameConfig config = GameConfig.getConfigForDifficulty(difficulty); // Get game configuration based on difficulty.
        GameWindow gameWindow = new GameWindow(config); // Create a new GameWindow with the configuration.
        gameWindow.startGame(); // Start the game in the new window.
    }

    // Method to show high scores in a dialog box.
    private void showHighScores() {
        List<String[]> highScores = readHighScores(); // Read high scores from the file.
        StringBuilder scoreDisplay = new StringBuilder("Top 5 High Scores:\n"); // Prepare the score display message.

        // Check if there are high scores to display.
        if (highScores.isEmpty()) {
            scoreDisplay.append("No high scores recorded yet."); // Display a message if no scores are found.
        } else {
            // Append each high score to the display message.
            for (String[] score : highScores) {
                scoreDisplay.append("Player: ").append(score[0])
                            .append(", Score: ").append(score[2])
                            .append(", Best Score: ").append(score[2])
                            .append(", Difficulty: ").append(score[3])
                            .append(", Timestamp: ").append(score[1])
                            .append("\n");
            }
        }

        // Show the high scores in a message dialog.
        JOptionPane.showMessageDialog(this, scoreDisplay.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to read high scores from a file and return a sorted list.
    private List<String[]> readHighScores() {
        List<String[]> scores = new ArrayList<>(); // List to store high scores.
        String filePath = scorePath + "game_records.txt"; // Path to the high scores file.

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // Read the file using BufferedReader.
            String line;
            while ((line = br.readLine()) != null) { // Read each line from the file.
                String[] parts = line.split(", "); // Split the line by commas.
                if (parts.length >= 5) { // Check if the line contains enough parts.
                    // Extract relevant data from the line.
                    String playerName = parts[0].split(": ")[1].trim();
                    String score = parts[1].split(": ")[1].trim();
                    String difficulty = parts[3].split(": ")[1].trim();
                    String timestamp = parts[4].split(": ")[1].trim();
                    scores.add(new String[]{playerName, timestamp, score, difficulty}); // Add the score to the list.
                }
            }
        } catch (IOException e) { // Handle any I/O exceptions.
            e.printStackTrace(); // Print stack trace for debugging.
        }

        // Sort the scores in descending order based on score.
        Collections.sort(scores, new Comparator<String[]>() {
            @Override
            public int compare(String[] a, String[] b) {
                return Integer.compare(Integer.parseInt(b[2]), Integer.parseInt(a[2])); // Compare scores.
            }
        });

        // Return the top 5 scores or the entire list if there are fewer than 5 scores.
        return scores.size() > 5 ? scores.subList(0, 5) : scores;
    }

    // Main method to run the GameMenu application.
    public static void main(String[] args) {
        new GameMenu(); // Create a new instance of GameMenu, which displays the game menu.
    }
}
