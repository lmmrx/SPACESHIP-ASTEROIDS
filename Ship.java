import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

// Class representing a spaceship in the game.
public class Ship {
    private int x, y; // Position of the ship on the screen.
    private int speed; // Normal speed of the ship.
    private int turboSpeed; // Speed of the ship when turbo is active.
    private int width, height; // Dimensions of the ship.
    private Image image; // Image representing the ship.
    private boolean turboActive; // Flag to indicate if turbo mode is active.
    private ArrayList<Particle> particles; // List of particles for visual effects (e.g., turbo exhaust).
    private String imgPath = "D:\\College\\game deveploment\\FINAL GAME\\img\\"; // Path to the image resources.

    // Constructor to initialize the ship with position, speed, and dimensions.
    public Ship(int x, int y, int speed, int width, int height) {
        this.x = x;
        this.y = y;
        this.speed = speed > 0 ? speed : 1; // Ensure speed is positive.
        this.turboSpeed = Math.max(speed * 2, 1); // Turbo speed is double the normal speed, with a minimum of 1.
        this.width = width;
        this.height = height;
        ImageIcon icon = new ImageIcon(imgPath + "spaceship.png"); // Load the spaceship image.
        image = icon.getImage();
        if (image != null) {
            image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Scale the image to fit the ship's dimensions.
        }
        turboActive = false; // Initialize turbo mode as inactive.
        particles = new ArrayList<>(); // Initialize the list of particles.
    }

    // Method to move the ship based on input direction and update position.
    public void move(int dx, int dy) {
        int currentSpeed = turboActive ? turboSpeed : speed; // Use turbo speed if turbo is active.
        x += dx * currentSpeed;
        y += dy * currentSpeed;

        // Ensure the ship stays within the screen bounds.
        if (x < 0) x = 0;
        if (x > 800 - width) x = 800 - width;
        if (y < 0) y = 0;
        if (y > 600 - height) y = 600 - height;

        // Add particles for visual effect if turbo is active.
        if (turboActive) {
            particles.add(new Particle(x + width / 2, y + height, 4)); // Add new particle at the ship's position.
            for (int i = particles.size() - 1; i >= 0; i--) {
                particles.get(i).update(); // Update each particle.
                if (particles.get(i).isDead()) { // Remove dead particles.
                    particles.remove(i);
                }
            }
        }
    }

    // Activate turbo mode.
    public void activateTurbo() {
        turboActive = true;
    }

    // Deactivate turbo mode.
    public void deactivateTurbo() {
        turboActive = false;
    }

    // Draw the ship and its particles.
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null); // Draw the ship image.
        for (Particle particle : particles) {
            particle.draw(g); // Draw each particle.
        }
    }

    // Inner class representing a particle for visual effects.
    private class Particle {
        private int x, y; // Position of the particle.
        private int size; // Size of the particle.
        private int life; // Life of the particle, used to control fading.
        private Color color; // Color of the particle with alpha transparency.

        // Constructor to initialize a particle with position and size.
        public Particle(int x, int y, int initialSize) {
            this.x = x;
            this.y = y;
            this.size = initialSize;
            this.life = 255; // Start with full opacity.
            this.color = new Color(255, 255, 0, life); // Initialize color (yellow with full opacity).
        }

        // Update the particle's position and fade it out.
        public void update() {
            y -= 4; // Move the particle up.
            life -= 20; // Reduce the opacity.
            color = new Color(255, 255, 0, Math.max(life, 0)); // Update the color with the new opacity.
        }

        // Draw the particle.
        public void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(x - size / 2, y - size / 2, size, size); // Draw the particle as a filled oval.
        }

        // Check if the particle is dead (opacity <= 0).
        public boolean isDead() {
            return life <= 0;
        }
    }

    // Getters and setters for the ship's position.
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
