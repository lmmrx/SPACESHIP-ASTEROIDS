import java.awt.*; // Import classes for handling graphical elements such as colors and shapes.
import java.util.ArrayList; // Import the ArrayList class for storing a list of trail points.

class Bullet { // Define the Bullet class.
    public static final int WIDTH = 3; // Define the width of the bullet.
    public static final int HEIGHT = 10; // Define the height of the bullet.
    private static final Color TRAIL_COLOR = new Color(211, 211, 211, 75); // Define the color of the bullet's trail with a translucent effect.
    private static final int TRAIL_LENGTH = 10; // Define the maximum length of the bullet's trail.

    private float x, y; // Variables to store the current position of the bullet.
    private float speed; // Variable to store the speed at which the bullet moves.
    private ArrayList<Point> trailPoints; // List to store the points that make up the bullet's trail.

    // Constructor to initialize the bullet with a starting position.
    public Bullet(float x, float y) {
        this.x = x; // Set the x-coordinate of the bullet.
        this.y = y; // Set the y-coordinate of the bullet.
        this.speed = 5f; // Set the initial speed of the bullet.
        this.trailPoints = new ArrayList<>(); // Initialize the list for the trail points.
    }

    // Method to update the bullet's position and trail.
    public void update() {
        // Add the current position of the bullet to the trail points.
        trailPoints.add(new Point((int)x, (int)y));

        // If the trail is longer than the maximum allowed length, remove the oldest point.
        if (trailPoints.size() > TRAIL_LENGTH) {
            trailPoints.remove(0);
        }

        // Move the bullet up by its speed.
        y -= speed;
    }

    // Method to draw the bullet and its trail.
    public void draw(Graphics g) {
        // Draw the trail of the bullet.
        for (int i = 0; i < trailPoints.size(); i++) {
            Point p = trailPoints.get(i);
            // Set the color of the trail, fading out with distance.
            g.setColor(new Color(TRAIL_COLOR.getRed(), TRAIL_COLOR.getGreen(), TRAIL_COLOR.getBlue(),
                    (int) ((TRAIL_COLOR.getAlpha() / (float) TRAIL_LENGTH) * (i + 1))));
            // Draw a rectangle at each trail point.
            g.fillRect(p.x, p.y, WIDTH, HEIGHT);
        }

        // Draw the bullet itself.
        g.setColor(Color.lightGray); // Set the color for the bullet.
        g.fillRect((int)x - 2, (int)y - 2, WIDTH + 4, HEIGHT + 4); // Draw a larger rectangle to create a glowing effect.
        g.fillRect((int)x, (int)y, WIDTH, HEIGHT - 3); // Draw the main rectangle of the bullet.
        // Draw a triangle at the tip of the bullet to represent the nose.
        int[] xPoints = {(int)x, (int)x + WIDTH / 2, (int)x + WIDTH};
        int[] yPoints = {(int)y, (int)y - 5, (int)y};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    // Getter method for the x-coordinate of the bullet.
    public float getX() { return x; }
    // Getter method for the y-coordinate of the bullet.
    public float getY() { return y; }
    // Getter method for the width of the bullet.
    public int getWidth() { return WIDTH; }
    // Getter method for the height of the bullet.
    public int getHeight() { return HEIGHT; }
}
