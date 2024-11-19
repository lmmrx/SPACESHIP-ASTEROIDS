import javax.swing.*; // Import Swing classes for GUI components.
import java.awt.*; // Import AWT classes for graphical operations.
import java.awt.event.ActionEvent; // Import ActionEvent for handling action events.
import java.awt.event.ActionListener; // Import ActionListener for action event listeners.

class GameOverDialog extends JDialog { // Define the GameOverDialog class that extends JDialog for displaying game over dialogs.

    // Private constructor to create the dialog with specified parameters.
    private GameOverDialog(Frame owner, String message, int score, int bestScore, ActionListener actionListener) {
        super(owner, "Game Over", true); // Call the superclass constructor with title "Game Over" and modal set to true.
        setLayout(new BorderLayout()); // Set the layout manager for the dialog to BorderLayout.
        setResizable(false); // Make the dialog non-resizable.
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Set the default close operation to dispose of the dialog.

        JPanel mainPanel = new JPanel(); // Create a panel for the main content.
        mainPanel.setBackground(new Color(248, 248, 248)); // Set the background color of the panel.
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Set the layout manager to BoxLayout with vertical alignment.
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the panel.

        JLabel messageLabel = new JLabel(message); // Create a label to display the message.
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set the font of the label text.
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label text horizontally.
        mainPanel.add(messageLabel); // Add the message label to the main panel.

        JPanel scorePanel = new JPanel(); // Create a panel for displaying scores.
        scorePanel.setLayout(new GridLayout(2, 1)); // Set the layout manager to GridLayout with 2 rows and 1 column.
        scorePanel.setOpaque(false); // Make the panel transparent.
        scorePanel.add(new JLabel("Score: " + score)); // Add a label showing the current score.
        scorePanel.add(new JLabel("Best Score: " + bestScore)); // Add a label showing the best score.
        scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding around the score panel.
        mainPanel.add(scorePanel); // Add the score panel to the main panel.

        JPanel buttonPanel = new JPanel(); // Create a panel for buttons.
        buttonPanel.setBackground(new Color(248, 248, 248)); // Set the background color of the panel.

        JButton retryButton = new JButton("Retry"); // Create a button for retrying the game.
        JButton exitButton = new JButton("Exit"); // Create a button for exiting the game.

        // Set background and foreground colors for buttons.
        retryButton.setBackground(new Color(76, 175, 80)); // Green background for retry button.
        retryButton.setForeground(Color.WHITE); // White text color for retry button.
        exitButton.setBackground(new Color(244, 67, 54)); // Red background for exit button.
        exitButton.setForeground(Color.WHITE); // White text color for exit button.

        // Add action listener to retry button.
        retryButton.addActionListener(e -> {
            actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Retry")); // Notify the action listener with "Retry" command.
            dispose(); // Close the dialog.
        });

        // Add action listener to exit button.
        exitButton.addActionListener(e -> {
            actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit")); // Notify the action listener with "Exit" command.
            dispose(); // Close the dialog.
        });

        buttonPanel.add(retryButton); // Add the retry button to the button panel.
        buttonPanel.add(Box.createHorizontalStrut(10)); // Add a spacer between buttons.
        buttonPanel.add(exitButton); // Add the exit button to the button panel.

        mainPanel.add(buttonPanel); // Add the button panel to the main panel.
        add(mainPanel, BorderLayout.CENTER); // Add the main panel to the center of the dialog.
        setSize(300, 200); // Set the size of the dialog.
        setLocationRelativeTo(owner); // Center the dialog relative to the owner frame.
    }

    // Public static method to display the game over dialog.
    public static void showDialog(Frame owner, String message, int score, int bestScore, ActionListener actionListener) {
        GameOverDialog dialog = new GameOverDialog(owner, message, score, bestScore, actionListener); // Create a new instance of GameOverDialog.
        dialog.setVisible(true); // Make the dialog visible.
    }
}
