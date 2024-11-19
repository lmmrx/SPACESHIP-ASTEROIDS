import java.awt.Graphics; // Import the Graphics class for drawing 2D shapes and images.
import java.awt.Image; // Import the Image class for handling images.
import java.util.ArrayList; // Import ArrayList for dynamic array management.
import java.util.List; // Import List interface for list operations.
import java.util.Random; // Import Random class for generating random numbers.
import javax.swing.ImageIcon; // Import ImageIcon class for loading and handling images.

public class Asteroid { // Class representing an asteroid in the game.
    private int x, y, speed, width, height; // Variables to store the asteroid's position, speed, width, and height.
    private Image image; // Variable to hold the asteroid's image.
    private boolean isBroken = false; // Flag indicating if the asteroid is broken into fragments.
    private List<AsteroidFragment> fragments; // List to store fragments of the asteroid.
    private String imgPath = "D:\\College\\game deveploment\\FINAL GAME\\img\\"; // Path to the image resources.

    // Constructor for initializing the asteroid object.
    public Asteroid(int x, int y, int speed, int width, int height) { 
        this.x = x; // Set the x-coordinate.
        this.y = y; // Set the y-coordinate.
        this.speed = speed; // Set the speed of the asteroid.
        this.width = width; // Set the width of the asteroid.
        this.height = height; // Set the height of the asteroid.
        this.fragments = new ArrayList<>(); // Initialize the list to store fragments.
        ImageIcon icon = new ImageIcon(imgPath + "asteroid.png"); // Load the asteroid image.
        image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); // Scale the image to the asteroid's size.
    }

    // Method to update the asteroid's position or fragments.
    public void update() { 
        if (!isBroken) { // If the asteroid is not broken,
            y += speed; // Move the asteroid down by increasing the y-coordinate by its speed.
        } else { // If the asteroid is broken,
            for (AsteroidFragment fragment : fragments) { // Iterate through each fragment,
                fragment.update(); // Update each fragment's position.
            }
        }
    }

    // Method to draw the asteroid or its fragments on the screen.
    public void draw(Graphics g) { 
        if (!isBroken) { // If the asteroid is not broken,
            g.drawImage(image, x, y, null); // Draw the asteroid's image at its current position.
        } else { // If the asteroid is broken,
            for (AsteroidFragment fragment : fragments) { // Iterate through each fragment,
                fragment.draw(g); // Draw each fragment on the screen.
            }
        }
    }

    // Method to break the asteroid into fragments.
    public void breakApart() { 
        isBroken = true; // Set the flag indicating that the asteroid is broken.
        Random random = new Random(); // Create a Random object for generating random numbers.
        int fragmentCount = 4 + random.nextInt(3); // Determine the number of fragments (between 4 and 6).
        for (int i = 0; i < fragmentCount; i++) { // Loop to create each fragment.
            int fragmentSpeedX = random.nextInt(6) - 3; // Randomize the horizontal speed of the fragment.
            int fragmentSpeedY = random.nextInt(6) + 2; // Randomize the vertical speed of the fragment.
            int fragmentWidth = width / 3; // Set the fragment width to one-third of the asteroid's width.
            int fragmentHeight = height / 3; // Set the fragment height to one-third of the asteroid's height.
            fragments.add(new AsteroidFragment( // Add a new fragment to the list.
                    x + random.nextInt(width / 3) - (width / 6), // Set the fragment's x position with some random offset.
                    y + random.nextInt(height / 3) - (height / 6), // Set the fragment's y position with some random offset.
                    fragmentSpeedX, // Set the fragment's x speed.
                    fragmentSpeedY, // Set the fragment's y speed.
                    fragmentWidth, // Set the fragment's width.
                    fragmentHeight)); // Set the fragment's height.
        }
    }

    // Method to check if the asteroid is broken.
    public boolean isBroken() { 
        return isBroken; // Return the broken status.
    }

    // Getter method for the x-coordinate.
    public int getX() { 
        return x; // Return the x-coordinate.
    }

    // Getter method for the y-coordinate.
    public int getY() { 
        return y; // Return the y-coordinate.
    }

    // Getter method for the width of the asteroid.
    public int getWidth() { 
        return width; // Return the width.
    }

    // Getter method for the height of the asteroid.
    public int getHeight() { 
        return height; // Return the height.
    }
}
