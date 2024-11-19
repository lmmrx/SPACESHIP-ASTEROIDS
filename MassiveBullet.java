import java.awt.*;

class MassiveBullet extends Bullet {
    public static final int WIDTH = 50; // Change width for massive bullet
    public static final int HEIGHT = 50; // Change height for massive bullet

    public MassiveBullet(int x, int y) {
        super(x, y);
        // You can add additional attributes or behaviors for massive bullets, if required.
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED); // Change color for massive bullet
        g.fillRect(getX(), getY(), WIDTH, HEIGHT); // Draw massive bullet
    }
}
