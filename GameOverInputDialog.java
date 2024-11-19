import javax.swing.*; // Import Swing classes for GUI components.
import java.awt.*; // Import AWT classes for graphical operations.

public class GameOverInputDialog { // Define the GameOverInputDialog class.

    // Static method to show an input dialog and return user input.
    public static String showInputDialog(Component parent, String title, String message) {
        // Create a modal dialog with the specified title.
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setLayout(new GridBagLayout()); // Set the layout manager to GridBagLayout.
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Set the default close operation to dispose of the dialog.
        dialog.setSize(400, 200); // Set the size of the dialog.
        dialog.setLocationRelativeTo(parent); // Center the dialog relative to the parent component.

        GridBagConstraints gbc = new GridBagConstraints(); // Create GridBagConstraints for layout management.
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components.
        gbc.gridx = 0; // Set the x position of the component.
        gbc.weightx = 1; // Allow the component to grow horizontally.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill the width of the cell.

        // Create a panel to display the message.
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(255, 235, 205)); // Set the background color of the panel.
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel.
        JLabel messageLabel = new JLabel(message, JLabel.CENTER); // Create a label with the message.
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Set the font of the label text.
        messagePanel.add(messageLabel); // Add the message label to the message panel.
        dialog.add(messagePanel, gbc); // Add the message panel to the dialog.

        gbc.gridy = 1; // Set the y position of the component.
        JTextField inputField = new JTextField(); // Create a text field for user input.
        inputField.setFont(new Font("Arial", Font.PLAIN, 16)); // Set the font of the text field text.
        inputField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // Add a border to the text field.
        dialog.add(inputField, gbc); // Add the text field to the dialog.

        gbc.gridy = 2; // Set the y position for the next component.
        JPanel buttonPanel = new JPanel(); // Create a panel for buttons.
        JButton okButton = new JButton("OK"); // Create an "OK" button.
        JButton cancelButton = new JButton("Cancel"); // Create a "Cancel" button.

        // Set properties for the "OK" button.
        okButton.setBackground(new Color(76, 175, 80)); // Green background.
        okButton.setForeground(Color.WHITE); // White text color.
        okButton.setFocusPainted(false); // Remove focus painting.
        okButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding around the button.

        // Set properties for the "Cancel" button.
        cancelButton.setBackground(new Color(244, 67, 54)); // Red background.
        cancelButton.setForeground(Color.WHITE); // White text color.
        cancelButton.setFocusPainted(false); // Remove focus painting.
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding around the button.

        buttonPanel.add(okButton); // Add the "OK" button to the button panel.
        buttonPanel.add(cancelButton); // Add the "Cancel" button to the button panel.
        dialog.add(buttonPanel, gbc); // Add the button panel to the dialog.

        // Array to hold the user input, which will be returned.
        String[] userInput = new String[1];

        // Add action listener to the "OK" button.
        okButton.addActionListener(e -> {
            String input = inputField.getText(); // Get the text from the input field.
            if (!input.isEmpty()) { // Check if the input is not empty.
                userInput[0] = input; // Store the input.
                dialog.dispose(); // Close the dialog.
            } else {
                // Show a warning message if the input is empty.
                JOptionPane.showMessageDialog(dialog, "Please enter your name.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Add action listener to the "Cancel" button.
        cancelButton.addActionListener(e -> {
            userInput[0] = null; // Set the input to null if canceled.
            dialog.dispose(); // Close the dialog.
        });

        dialog.setVisible(true); // Make the dialog visible.
        return userInput[0]; // Return the user input or null if canceled.
    }
}
