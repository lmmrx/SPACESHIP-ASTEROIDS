import java.awt.Color;
import java.awt.Graphics;

class ShootingStar {
    private int x, y;
    private int speed;

    // Constructor
    public ShootingStar(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed; // Keep this value higher for faster movement
    }

    // Update the position of the shooting star
    public void update() {
        // Move the star upwards at a higher speed
        y -= speed; 
    }

    // Draw the shooting star
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, 2, 2); // Smaller size for faster appearance
    }

    // Check if the star is off the screen
    public boolean isOffScreen() {
        return y < 0; // Check if it has moved off the top of the screen
    }
}