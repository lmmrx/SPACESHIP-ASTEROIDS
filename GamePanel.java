import javax.swing.*; // Import Swing classes for GUI components like JPanel, Timer, JOptionPane, etc.
import java.awt.*; // Import AWT classes for graphical operations like Color, Font, Insets, etc.
import java.awt.event.KeyEvent; // Import AWT classes for handling key events.
import java.awt.event.KeyListener; // Import AWT classes for listening to key events.
import java.io.BufferedWriter; // Import classes for writing data to files.
import java.io.FileWriter; // Import classes for writing data to files.
import java.io.IOException; // Import classes for handling I/O exceptions.
import java.text.SimpleDateFormat; // Import classes for formatting dates.
import java.util.ArrayList; // Import classes for using ArrayList.
import java.util.Date; // Import classes for handling dates.
import java.util.Random; // Import classes for generating random numbers.

class GamePanel extends JPanel implements KeyListener { // Define the GamePanel class that extends JPanel and implements KeyListener.
    private Timer timer; // Timer to handle game updates and animations.
    private Ship ship; // The player's ship.
    private ArrayList<Asteroid> asteroids; // List of asteroids in the game.
    private ArrayList<Bullet> bullets; // List of bullets fired by the ship.
    private GameConfig config; // Configuration settings for the game.
    private Random random; // Random number generator.
    private final int ASTEROID_WIDTH = 35; // Width of the asteroids.
    private final int ASTEROID_HEIGHT = 35; // Height of the asteroids.
    private final int SHIP_WIDTH = 50; // Width of the ship.
    private final int SHIP_HEIGHT = 50; // Height of the ship.
    private int score; // Player's current score.
    private int bestScore; // Player's best score.
    private boolean gameOver; // Flag indicating if the game is over.
    private int chances; // Number of remaining lives or chances.
    private String playerName = ""; // Player's name.
    private int difficulty; // Game difficulty level.
    private boolean gameStarted = false; // Flag indicating if the game has started.
    private String wavPath = "D:\\College\\game deveploment\\FINAL GAME\\wav\\"; // Path to sound files.
    private boolean collisionEffectActive = false; // Flag indicating if collision effect is active.
    private long collisionEffectStartTime; // Time when the collision effect started.
    private final long COLLISION_EFFECT_DURATION = 5000; // Duration of the collision effect in milliseconds.
    private boolean paused = false; // Flag indicating if the game is paused.
    private ArrayList<String> chatMessages; // List of chat messages.
    private StringBuilder currentChatInput; // Builder for the current chat input.
    private boolean leftPressed = false; // Flag indicating if the left arrow key is pressed.
    private boolean rightPressed = false; // Flag indicating if the right arrow key is pressed.
    private boolean upPressed = false; // Flag indicating if the up arrow key is pressed.
    private boolean downPressed = false; // Flag indicating if the down arrow key is pressed.
    private String bgmSoundPath = wavPath + "bgm2.wav"; // Path to the background music file.
    private String bgm2SoundPath = wavPath + "bgm3.wav"; // Path to an alternative background music file.
    private String asteroidDestroyedSoundPath = wavPath + "asteroid_destroyed.wav"; // Path to the asteroid destroyed sound file.
    private String collideSoundPath = wavPath + "collide.wav"; // Path to the collision sound file.
    private String deathSoundPath = wavPath + "death.wav"; // Path to the player's death sound file.
    private String gameOverSoundPath = wavPath + "gameover.wav"; // Path to the game over sound file.
    private String shootSoundPath = wavPath + "shoot.wav"; // Path to the shooting sound file.
    private String turboSoundPath = wavPath + "turbo.wav"; // Path to the turbo sound file.
    private boolean muted = false; // Flag indicating if the sound is muted.
    private Timer chatClearTimer; // Timer to clear chat messages.
    private ArrayList<ShootingStar> shootingStars; // List of shooting stars in the game.
    private final int SHOOTING_STAR_SPAWN_RATE = 50; // Rate at which shooting stars spawn.

    public GamePanel(GameConfig config) { // Constructor for the GamePanel class.
        this.score = 0; // Initialize the score.
        this.bestScore = 0; // Initialize the best score.
        this.gameOver = false; // Initialize the game over flag.
        chatMessages = new ArrayList<>(); // Initialize the list of chat messages.
        currentChatInput = new StringBuilder(); // Initialize the chat input builder.
        chatClearTimer = new Timer(3000, e -> clearChatMessages()); // Initialize the chat clear timer to clear messages every 3 seconds.
        chatClearTimer.setRepeats(false); // Set the timer to only run once.
        shootingStars = new ArrayList<>(); // Initialize the list of shooting stars.
        // Load all sound files using the AudioManager.
        AudioManager.loadSound(bgmSoundPath);
        AudioManager.loadSound(bgm2SoundPath);
        AudioManager.loadSound(asteroidDestroyedSoundPath);
        AudioManager.loadSound(collideSoundPath);
        AudioManager.loadSound(deathSoundPath);
        AudioManager.loadSound(gameOverSoundPath);
        AudioManager.loadSound(shootSoundPath);
        AudioManager.loadSound(turboSoundPath);
        setBackground(Color.BLACK); // Set the background color of the panel.
        this.config = config; // Set the game configuration.
        this.difficulty = config.getDifficulty(); // Set the game difficulty.
        this.chances = config.getLives(); // Set the number of lives.
        ship = new Ship(375, 500, config.getShipSpeed(), SHIP_WIDTH, SHIP_HEIGHT); // Initialize the player's ship.
        asteroids = new ArrayList<>(); // Initialize the list of asteroids.
        bullets = new ArrayList<>(); // Initialize the list of bullets.
        random = new Random(); // Initialize the random number generator.
        // Create a timer to update the game every 16 milliseconds (approximately 60 FPS).
        timer = new Timer(16, e -> {
            if (!gameOver && !paused) { // Check if the game is not over and not paused.
                updateGame(); // Update the game state.
                repaint(); // Repaint the panel.
            }
        });
        addKeyListener(this); // Add key listener to the panel.
        setFocusable(true); // Make the panel focusable to receive key events.
    }

    private void clearChatMessages() { // Method to clear chat messages.
        chatMessages.clear(); // Clear the list of chat messages.
        repaint(); // Repaint the panel to update the display.
    }

    public void startGame() { // Method to start the game.
        paused = true; // Set the game to paused state.
        timer.start(); // Start the game timer.
        generateAsteroids(); // Generate asteroids.
        AudioManager.stopSound(bgmSoundPath); // Stop the current background music.
        AudioManager.playSound(bgmSoundPath, true); // Play the background music.
    }

    private void updateGame() { // Method to update the game state.
        // Update bullets and check for collisions with asteroids.
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i); // Get the bullet.
            bullet.update(); // Update the bullet's position.
            if (bullet.getY() < 0) { // Check if the bullet is off the screen.
                bullets.remove(i); // Remove the bullet from the list.
            } else {
                for (int j = asteroids.size() - 1; j >= 0; j--) {
                    Asteroid asteroid = asteroids.get(j); // Get the asteroid.
                    if (isColliding(bullet, asteroid)) { // Check for collision between bullet and asteroid.
                        asteroid.breakApart(); // Break the asteroid into pieces.
                        bullets.remove(i); // Remove the bullet from the list.
                        score++; // Increase the score.
                        if (!muted) { // Check if sound is not muted.
                            AudioManager.playSound(asteroidDestroyedSoundPath, false); // Play the asteroid destroyed sound.
                        }
                        break; // Exit the loop once the bullet collides with an asteroid.
                    }
                }
            }
        }

        // Update asteroids and check for collisions with the ship.
        for (int i = asteroids.size() - 1; i >= 0; i--) {
            Asteroid asteroid = asteroids.get(i); // Get the asteroid.
            asteroid.update(); // Update the asteroid's position.
            if (isColliding(ship, asteroid)) { // Check for collision between ship and asteroid.
                chances--; // Decrease the number of chances.
                asteroids.remove(i); // Remove the asteroid from the list.
                if (!muted) { // Check if sound is not muted.
                    AudioManager.playSound(collideSoundPath, false); // Play the collision sound.
                }
                collisionEffectActive = true; // Activate collision effect.
                collisionEffectStartTime = System.currentTimeMillis(); // Record the time when the collision effect started.
                if (chances <= 0) { // Check if chances are exhausted.
                    if (!muted) { // Check if sound is not muted.
                        AudioManager.stopSound(bgmSoundPath); // Stop the background music.
                        AudioManager.playSound(deathSoundPath, false); // Play the death sound.
                        AudioManager.playSound(gameOverSoundPath, false); // Play the game over sound.
                        AudioManager.playSound(bgm2SoundPath, true); // Play the alternative background music.
                    }
                    gameOver = true; // Set the game over flag.
                    if (score > bestScore) { // Check if the current score is higher than the best score.
                        bestScore = score; // Update the best score.
                    }
                    repaint(); // Repaint the panel to update the display.
                }
                break; // Exit the loop once the ship collides with an asteroid.
            }
        }

        // Spawn shooting stars randomly.
        if (random.nextInt(SHOOTING_STAR_SPAWN_RATE) < 2) {
            int starX = random.nextInt(getWidth()); // Random X position for the shooting star.
            int starSpeed = random.nextInt(3) + 2; // Random speed for the shooting star.
            shootingStars.add(new ShootingStar(starX, getHeight(), starSpeed)); // Add a new shooting star to the list.
        }

        // Update shooting stars and remove those that are off-screen.
        for (int i = shootingStars.size() - 1; i >= 0; i--) {
            ShootingStar star = shootingStars.get(i); // Get the shooting star.
            star.update(); // Update the shooting star's position.
            if (star.isOffScreen()) { // Check if the shooting star is off the screen.
                shootingStars.remove(i); // Remove the shooting star from the list.
            }
        }

        // Deactivate collision effect if duration has passed.
        if (collisionEffectActive && System.currentTimeMillis() - collisionEffectStartTime > COLLISION_EFFECT_DURATION) {
            collisionEffectActive = false; // Deactivate collision effect.
        }

        // Randomly generate asteroids based on the configured rate.
        if (random.nextInt(100) < config.getAsteroidCount()) {
            generateAsteroid(); // Generate a new asteroid.
        }

        // Update ship movement based on key presses.
        int dx = 0;
        int dy = 0;
        if (leftPressed) {
            dx = -2; // Move the ship left.
        }
        if (rightPressed) {
            dx = 2; // Move the ship right.
        }
        if (upPressed) {
            dy = -2; // Move the ship up.
        }
        if (downPressed) {
            dy = 2; // Move the ship down.
        }
        ship.move(dx, dy); // Move the ship based on dx and dy.

        // Ensure the ship stays within the bounds of the screen.
        if (ship.getX() < 0) {
            ship.setX(0); // Prevent the ship from moving off the left edge.
        } else if (ship.getX() + SHIP_WIDTH > getWidth()) {
            ship.setX(getWidth() - SHIP_WIDTH); // Prevent the ship from moving off the right edge.
        }
        if (ship.getY() < 0) {
            ship.setY(0); // Prevent the ship from moving off the top edge.
        } else if (ship.getY() + SHIP_HEIGHT > getHeight()) {
            ship.setY(getHeight() - SHIP_HEIGHT); // Prevent the ship from moving off the bottom edge.
        }
    }

    private void enterPlayerName() { // Method to prompt the player to enter their name.
        String name = JOptionPane.showInputDialog(this, "Enter your name to save your record:"); // Show input dialog.
        if (name != null && !name.trim().isEmpty()) {
            playerName = name.trim(); // Set the player's name.
        } else {
            playerName = "Unknown Player"; // Default name if input is invalid.
        }
        saveGameRecord(); // Save the game record.
        showRetryOrExitDialog(); // Show dialog to retry, return to main menu, or exit.
    }

    private void showRetryOrExitDialog() { // Method to show options for retrying, returning to the main menu, or exiting.
        AudioManager.playSound(bgm2SoundPath, true); // Play alternative background music.
        String[] options = {"Retry", "Main Menu", "Exit"}; // Options for the dialog.
        int choice = JOptionPane.showOptionDialog(this, "Please select", "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); // Show option dialog.

        if (choice == 0) {
            resetGame(); // Reset the game if "Retry" is selected.
            startGame(); // Start the game.
        } else if (choice == 1) {
            returnToMainMenu(); // Return to the main menu if "Main Menu" is selected.
        } else {
            System.exit(0); // Exit the application if "Exit" is selected.
        }
    }

    private void returnToMainMenu() { // Method to return to the main menu.
        new GameMenu().setVisible(true); // Create and show the GameMenu.
        this.setVisible(false); // Hide the current GamePanel.
    }

    private void generateAsteroids() { // Method to generate multiple asteroids.
        for (int i = 0; i < config.getAsteroidCount(); i++) {
            generateAsteroid(); // Generate an asteroid.
        }
    }

    private void generateAsteroid() { // Method to generate a single asteroid.
        int x = random.nextInt(getWidth() - ASTEROID_WIDTH); // Random X position for the asteroid.
        int y = -ASTEROID_HEIGHT; // Start the asteroid off the top of the screen.
        int speed = config.getAsteroidSpeed(); // Get the asteroid speed from config.
        asteroids.add(new Asteroid(x, y, speed, ASTEROID_WIDTH, ASTEROID_HEIGHT)); // Add the asteroid to the list.
    }

    private boolean isColliding(Ship ship, Asteroid asteroid) { // Method to check collision between ship and asteroid.
        return ship.getX() < asteroid.getX() + asteroid.getWidth() && // Check if ship's left edge is left of asteroid's right edge.
               ship.getX() + ship.getWidth() > asteroid.getX() && // Check if ship's right edge is right of asteroid's left edge.
               ship.getY() < asteroid.getY() + asteroid.getHeight() && // Check if ship's top edge is above asteroid's bottom edge.
               ship.getY() + ship.getHeight() > asteroid.getY(); // Check if ship's bottom edge is below asteroid's top edge.
    }

    private boolean isColliding(Bullet bullet, Asteroid asteroid) { // Method to check collision between bullet and asteroid.
        return bullet.getX() < asteroid.getX() + asteroid.getWidth() && // Check if bullet's left edge is left of asteroid's right edge.
               bullet.getX() + bullet.getWidth() > asteroid.getX() && // Check if bullet's right edge is right of asteroid's left edge.
               bullet.getY() < asteroid.getY() + asteroid.getHeight() && // Check if bullet's top edge is above asteroid's bottom edge.
               bullet.getY() + bullet.getHeight() > asteroid.getY(); // Check if bullet's bottom edge is below asteroid's top edge.
    }

    private void resetGame() { // Method to reset the game state.
        score = 0; // Reset the score.
        chances = config.getLives(); // Reset the number of chances.
        gameOver = false; // Reset the game over flag.
        asteroids.clear(); // Clear the list of asteroids.
        bullets.clear(); // Clear the list of bullets.
        ship.setX(375); // Reset the ship's X position.
        ship.setY(500); // Reset the ship's Y position.
        collisionEffectActive = false; // Deactivate collision effect.
        AudioManager.stopSound(bgmSoundPath); // Stop the background music.
        AudioManager.stopSound(bgm2SoundPath); // Stop the alternative background music.
        AudioManager.playSound(bgmSoundPath, true); // Play the background music.
        startGame(); // Start the game.
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode(); // Get the code of the key that was pressed.
    
        // Start the game when Enter is pressed and the game hasn't started yet.
        if (!gameStarted && keyCode == KeyEvent.VK_ENTER) {
            gameStarted = true;
            paused = false;
            startGame(); // Initialize and start the game.
            return;
        }
    
        // Prompt for player name and handle game over when Enter is pressed.
        if (gameOver && keyCode == KeyEvent.VK_ENTER) {
            enterPlayerName(); // Prompt the player to enter their name and save the record.
            return;
        }
    
        // Toggle pause/resume game when Enter is pressed and the game is already started.
        if (gameStarted && keyCode == KeyEvent.VK_ENTER) {
            paused = !paused; // Toggle the paused state.
            repaint(); // Repaint the panel to update the display.
            return;
        }
    
        // Open a chat input dialog when 'C' is pressed and add the message to the chat.
        if (keyCode == KeyEvent.VK_C) {
            currentChatInput.setLength(0); // Clear current chat input.
            String message = JOptionPane.showInputDialog(this, "Enter your message:"); // Show input dialog.
            if (message != null && !message.trim().isEmpty()) {
                chatMessages.add(playerName + ": " + message); // Add message to chat.
                chatClearTimer.restart(); // Restart the timer to clear the chat.
            }
            return;
        }
    
        // Handle movement controls using arrow keys or WASD keys.
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
            leftPressed = true; // Start moving left.
        } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
            rightPressed = true; // Start moving right.
        } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
            upPressed = true; // Start moving up.
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
            downPressed = true; // Start moving down.
        }
    
        // Activate turbo mode when Shift is pressed and play turbo sound.
        if (keyCode == KeyEvent.VK_SHIFT) {
            AudioManager.playSound(turboSoundPath, false); // Play turbo sound.
            ship.activateTurbo(); // Activate turbo mode on the ship.
        } 
        // Fire a bullet when Spacebar is pressed and play shoot sound if not muted.
        else if (keyCode == KeyEvent.VK_SPACE) {
            bullets.add(new Bullet(ship.getX() + (SHIP_WIDTH / 2) - (Bullet.WIDTH / 2), ship.getY() - Bullet.HEIGHT)); // Add a new bullet.
            if (!muted) {
                AudioManager.playSound(shootSoundPath, false); // Play shoot sound.
            }
        } 
        // Toggle pause when Escape is pressed.
        else if (keyCode == KeyEvent.VK_ESCAPE) {
            paused = !paused; // Toggle the paused state.
            repaint(); // Repaint the panel to update the display.
        } 
        // Toggle mute when 'M' is pressed.
        else if (keyCode == KeyEvent.VK_M) {
            muted = !muted; // Toggle the muted state.
            if (muted) {
                AudioManager.stopSound(bgmSoundPath); // Stop background music if muted.
                AudioManager.stopSound(bgm2SoundPath);
            } else {
                if (!gameOver) {
                    AudioManager.playSound(bgmSoundPath, true); // Resume background music if not muted.
                }
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode(); // Get the code of the key that was released.
    
        // Stop movement when arrow keys or WASD keys are released.
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
            leftPressed = false; // Stop moving left.
        } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
            rightPressed = false; // Stop moving right.
        } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
            upPressed = false; // Stop moving up.
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
            downPressed = false; // Stop moving down.
        }
    
        // Deactivate turbo mode when Shift is released.
        if (keyCode == KeyEvent.VK_SHIFT) {
            ship.deactivateTurbo(); // Deactivate turbo mode on the ship.
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // No implementation needed for this method.
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawStars(g); // Draw background stars.
        g.setColor(Color.WHITE); // Set text color to white.
        g.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Set font for chat messages.
    
        // Draw chat messages.
        int chatY = 30;
        for (String message : chatMessages) {
            g.drawString(message, 10, chatY + 20);
            chatY += 15;
        }
    
        // Draw shooting stars.
        for (ShootingStar star : shootingStars) {
            star.draw(g);
        }
    
        // Display instructions if the game hasn't started.
        if (!gameStarted) {
            g.setColor(Color.WHITE); // Set color for instructions.
            g.setFont(new Font("Monospaced", Font.PLAIN, 15)); // Set font for instructions.
            g.drawString("Objectives:", 20, 30);
            g.drawString("Your objective is to shoot the asteroids to accumulate points.", 20, 50);
            g.drawString("Depending on the difficulty level you choose, your number of lives will vary:", 20, 70);
            g.drawString("Easy mode grants you 5 lives, Medium mode provides 4 lives,", 20, 90);
            g.drawString("and Hard mode offers 3 lives.", 20, 110);
            g.drawString("Be careful colliding with asteroids will cost you a life!", 20, 150);
            g.drawString("Once your lives reach zero, the game ends.", 20, 170);
            g.drawString("Instructions:", 20, 210);
            g.drawString("Use the Arrow keys or WASD keys to move your spaceship.", 20, 230);
            g.drawString("Press the Spacebar to shoot at the asteroids.", 20, 250);
            g.drawString("Press 'C' key to use the chat message.", 20, 270);
            g.drawString("Press Enter or Esc key to pause the game.", 20, 290);
            g.drawString("Toggle Shift key to activate and deactivate the turbo.", 20, 310);
            g.drawString("Toggle the sound on and off by pressing the 'M' key to mute or unmute the game.", 20, 330);
            g.drawString("Good luck and have fun!", 20, 370);
            g.drawString("Press Enter to continue the game...", 20, 410);
            return; // Skip drawing game elements if the game hasn't started.
        }
    
        // Draw the ship, asteroids, and bullets if the game is active.
        if (collisionEffectActive) {
            g.setColor(Color.RED); // Set color for collision effect.
        } else {
            g.setColor(Color.WHITE); // Set default color.
        }
        ship.draw(g); // Draw the ship.
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(g); // Draw each asteroid.
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g); // Draw each bullet.
        }
    
        // Display game score and lives if the game is ongoing.
        if (!gameOver) {
            g.setColor(Color.WHITE); // Set color for score and lives display.
            g.setFont(new Font("Monospaced", Font.PLAIN, 15)); // Set font for score and lives.
            g.drawString("Score: " + score, 10, 30);
            int barWidth = 200;
            int barHeight = 15;
            int barX = getWidth() - barWidth - 20;
            int barY = 20;
            int barFilledWidth = Math.max(0, Math.min(barWidth, barWidth * chances / config.getLives()));
            g.setColor(Color.GREEN); // Set color for the lives bar.
            g.fillRect(barX, barY, barFilledWidth, barHeight); // Draw the filled part of the lives bar.
            g.setColor(Color.WHITE); // Set color for the lives bar border.
            g.drawRect(barX, barY, barWidth, barHeight); // Draw the border of the lives bar.
            g.drawString("Lives: " + chances, barX + (barWidth / 2) - 30, barY + barHeight + 15);
    
            // Display pause message if the game is paused.
            if (paused) {
                g.setColor(Color.WHITE); // Set color for the pause message.
                g.setFont(new Font("Monospaced", Font.BOLD, 30)); // Set font for the pause message.
                g.drawString("Game Paused", getWidth() / 2 - 75, getHeight() / 2);
            }
    
            // Display muted status if the game is muted.
            if (muted) {
                g.setColor(Color.WHITE); // Set color for the muted status.
                g.setFont(new Font("Monospaced", Font.BOLD, 20)); // Set font for the muted status.
                g.drawString("Muted", getWidth() / 2 - 30, 50);
            }
        } 
        // Display game over message if the game has ended.
        else {
            g.setColor(Color.WHITE); // Set color for the game over message.
            g.setFont(new Font("Monospaced", Font.PLAIN, 15)); // Set font for the game over message.
            g.drawString("Game Over", getWidth() / 2 - 40, getHeight() / 2 - 10);
            g.drawString("Score: " + score, getWidth() / 2 - 40, getHeight() / 2 + 15);
            g.drawString("Best Score: " + bestScore, getWidth() / 2 - 40, getHeight() / 2 + 40);
            g.drawString("Press enter to continue ...", getWidth() / 2 - 75, getHeight() / 2 + 85);
            requestFocusInWindow(); // Request focus to ensure key events are processed.
        }
    }
    
    // Draws random stars in the background.
    private void drawStars(Graphics g) {
        g.setColor(Color.WHITE); // Set color for stars.
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(getWidth());
            int y = random.nextInt(getHeight());
            int size = random.nextInt(3) + 1;
            g.fillOval(x, y, size, size); // Draw a star at a random position.
        }
    }
    
    // Converts difficulty level to a string representation.
    private String getDifficultyString(int difficulty) {
        switch (difficulty) {
            case 0:
                return "Easy";
            case 1:
                return "Medium";
            case 2:
                return "Hard";
            default:
                return "unknown"; // Return "unknown" for invalid difficulty levels.
        }
    }
    
    // Saves the current game record to a file.
    private void saveGameRecord() {
        String difficultyString = getDifficultyString(difficulty);
        String record = "Player: " + playerName +
                ", Score: " + score +
                ", Best Score: " + bestScore +
                ", Difficulty: " + difficultyString +
                ", Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("game_records.txt", true))) {
            writer.write(record); // Write the record to the file.
            writer.newLine(); // Add a new line after each record.
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace in case of an exception.
        }
    }
}    