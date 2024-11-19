import java.awt.Graphics; // Import the Graphics class for drawing 2D shapes and images.
import java.awt.Image; // Import the Image class for handling images.
import javax.swing.ImageIcon; // Import the ImageIcon class for loading and managing images.

public class AsteroidFragment { // Class representing a fragment of an asteroid.
    private float x, y, speedX, speedY; // Variables to store the fragment's position and speed in both x and y directions.
    private int width, height; // Variables to store the width and height of the fragment.
    private Image image; // Variable to hold the image of the fragment.
    private String imgPath = "D:\\College\\game deveploment\\FINAL GAME\\img\\"; // Path to the image resources.

    // Constructor to initialize the asteroid fragment object.
    public AsteroidFragment(int x, int y, int speedX, int speedY, int width, int height) {
        this.x = x; // Set the x-coordinate.
        this.y = y; // Set the y-coordinate.
        this.speedX = speedX; // Set the speed in the x direction.
        this.speedY = speedY; // Set the speed in the y direction.
        this.width = width; // Set the width of the fragment.
        this.height = height; // Set the height of the fragment.

        ImageIcon icon = new ImageIcon(imgPath + "fragments.png"); // Load the image for the fragment.
        image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // Scale the image to the fragment's size.
    }

    // Method to update the fragment's position.
    public void update() {
        x += speedX; // Update the x-coordinate by adding the x speed.
        y += speedY; // Update the y-coordinate by adding the y speed.
    }

    // Method to draw the fragment on the screen.
    public void draw(Graphics g) {
        g.drawImage(image, (int)x, (int)y, null); // Draw the fragment's image at its current position.
    }
}
