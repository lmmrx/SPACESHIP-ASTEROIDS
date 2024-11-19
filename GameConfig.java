public class GameConfig { // Define the GameConfig class to manage game configuration settings.
    private int asteroidSpeed; // Variable to store the speed of asteroids.
    private int asteroidCount; // Variable to store the number of asteroids.
    private int shipSpeed; // Variable to store the speed of the player's ship.
    private int lives; // Variable to store the number of lives the player has.
    private int difficulty; // Variable to store the difficulty level.

    // Constructor to initialize the game configuration with given parameters.
    public GameConfig(int asteroidSpeed, int asteroidCount, int shipSpeed, int lives, int difficulty) {
        this.asteroidSpeed = asteroidSpeed; // Set the speed of asteroids.
        this.asteroidCount = asteroidCount; // Set the number of asteroids.
        this.shipSpeed = shipSpeed; // Set the speed of the player's ship.
        this.lives = lives; // Set the number of lives.
        this.difficulty = difficulty; // Set the difficulty level.
    }

    // Getter method to retrieve the difficulty level.
    public int getDifficulty() {
        return difficulty;
    }

    // Getter method to retrieve the speed of asteroids.
    public int getAsteroidSpeed() {
        return asteroidSpeed;
    }

    // Getter method to retrieve the number of asteroids.
    public int getAsteroidCount() {
        return asteroidCount;
    }

    // Getter method to retrieve the speed of the player's ship.
    public int getShipSpeed() {
        return shipSpeed;
    }

    // Getter method to retrieve the number of lives.
    public int getLives() {
        return lives;
    }

    // Static method to get a GameConfig instance based on the difficulty level.
    public static GameConfig getConfigForDifficulty(int difficulty) {
        // Return a different GameConfig instance based on the provided difficulty level.
        switch (difficulty) {
            case 0: return new GameConfig(2, 5, 5, 5, difficulty); // Easy difficulty settings.
            case 1: return new GameConfig(4, 10, 7, 4, difficulty); // Medium difficulty settings.
            case 2: return new GameConfig(6, 15, 10, 3, difficulty); // Hard difficulty settings.
            default: throw new IllegalArgumentException("Difficulty not valid"); // Throw an exception for invalid difficulty levels.
        }
    }
}
