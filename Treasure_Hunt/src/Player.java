import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

// Represents the player character
public class Player extends Entity {
    GamePanel gp;  // Reference to the GamePanel for game-related operations
    KeyHandler keyH;  // Reference to the KeyHandler for controlling player movement
    private boolean isGameOver; // Tracks if the player is in a game-over state
    private static final int COLLISION_DISTANCE = 30; // Added collision distance constant

    // Constructor that initializes the player with a reference to the game panel and key handler
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();  // Set initial position and game values
        getPlayerImage();  // Load player sprite images
    }

    // Set the default values for player properties (starting position, speed, etc.)
    public void setDefaultValues() {
        x = 100; // Initial x position
        y = 100; // Initial y position
        speed = 4; // Speed of player movement
        direction = "down"; // Initial facing direction
        isGameOver = false; // Initialize game-over state to false
    }

    // Load images for the player character's different directions
    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();  // Print an error if the image loading fails
        }
    }

    // Correctly defined checkEnemyCollision() method
    public boolean checkEnemyCollision() {
        // Iterate through the enemies in the game panel
        for (Enemies enemy : gp.enemies) {
            // Calculate distance between player and enemy
            int dx = Math.abs((x + gp.tilesize / 2) - (enemy.x + gp.tilesize / 2));
            int dy = Math.abs((y + gp.tilesize / 2) - (enemy.y + gp.tilesize / 2));
            
            // Check if the distance between player and enemy is less than the collision threshold
            if (dx < COLLISION_DISTANCE && dy < COLLISION_DISTANCE) {
                return true;  // Collision detected
            }
        }
        return false;  // No collision detected
    }

    // Check if the player can move to the given position based on the game map layout
    private boolean canMove(int nextX, int nextY) {
        int nextRow = nextY / gp.tilesize;
        int nextCol = nextX / gp.tilesize;

        // Check if the target tile is within bounds and is a path (value of 1)
        return nextRow >= 0 && nextCol >= 0 &&
                nextRow < gp.maxScreenRow && nextCol < gp.maxScreenCol &&
                gp.mazeLayout[nextRow][nextCol] == 1; // Only move to path cells
    }

    // Update the player's position and check for game-over conditions
    public void update() {
        if (!isGameOver && (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed)) {
            int nextX = x;
            int nextY = y;

            // Determine the next position based on the key pressed
            if (keyH.upPressed) {
                direction = "up";
                nextY -= speed;
                if (canMove(nextX, nextY)) {
                    y = nextY;
                    direction = "up";
                }
            } else if (keyH.downPressed) {
                direction = "down";
                nextY += speed;
                if (canMove(nextX, nextY)) {
                    y = nextY;
                    direction = "down";
                }
            } else if (keyH.leftPressed) {
                direction = "left";
                nextX -= speed;
                if (canMove(nextX, nextY)) {
                    x = nextX;
                    direction = "left";
                }
            } else if (keyH.rightPressed) {
                direction = "right";
                nextX += speed;
                if (canMove(nextX, nextY)) {
                    x = nextX;
                    direction = "right";
                }
            }

            // Calculate the row and column in the maze for the next position
            int nextRow = nextY / gp.tilesize;
            int nextCol = nextX / gp.tilesize;

            // Check if the player reaches the door (exit condition)
            if (nextRow == gp.maze.getDoorRow() && nextCol == gp.maze.getDoorCol() && !gp.maze.isDoorOpened()) {
                gp.maze.openDoor(); // Open the door
                isGameOver = true; // End the game when the player reaches the exit
                displayVictory();
            }

            // Animation logic: Alternate between sprite1 and sprite2 for walking animation
            SpriteCounter++;
            if (SpriteCounter > 15) {
                spriteNumber = (spriteNumber == 1) ? 2 : 1;
                SpriteCounter = 0;
            }
        }

        // Check for enemy collision and trigger game over
        if (checkEnemyCollision()) {
            isGameOver = true;
            gp.gameOver(); // Call game over method in GamePanel
        }
    }

    // Display victory message when the player reaches the exit
    public void displayVictory() {
        JOptionPane.showMessageDialog(
            null, // Parent component (null for default frame)
            "Well Done! You've Escaped the Labyrinth!", // Message to display
            "Victory", // Custom title
            JOptionPane.INFORMATION_MESSAGE // Message type
        );
    }

    // Draw the player sprite based on direction
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = up1;
                break;
            case "down":
                image = down1;
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
        }

        // Scale and draw the player image at the given position
        int size = gp.originalTileSize * gp.scale;
        g2.drawImage(image, x, y, size, size, null);
    }

    // Sets the game-over state
    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    // Gets the current game-over state
    public boolean isGameOver() {
        return isGameOver;
    }
}




/*import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    public int timeRemaining; // Timer for the game
    public boolean gameEnded; // Check if game has ended

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
        timeRemaining = 300; // Set timer to 300 ticks (adjust as needed)
        gameEnded = false;
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/boy_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (gameEnded) return; // Do not update if the game has ended

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            int nextX = x;
            int nextY = y;

            // Determine the next position based on the key pressed
            if (keyH.upPressed) {
                direction = "up";
                nextY -= speed;
            } else if (keyH.downPressed) {
                direction = "down";
                nextY += speed;
            } else if (keyH.leftPressed) {
                direction = "left";
                nextX -= speed;
            } else if (keyH.rightPressed) {
                direction = "right";
                nextX += speed;
            }

            // Calculate the row and column in the maze for the next position
            int nextRow = nextY / gp.tilesize;
            int nextCol = nextX / gp.tilesize;

            // Check if the next cell is a path (1) and within maze bounds
            if (nextRow >= 0 && nextCol >= 0 && nextRow < gp.maxScreenRow && nextCol < gp.maxScreenCol
                && gp.mazeLayout[nextRow][nextCol] == 1) {
                x = nextX;
                y = nextY;
            }

            // Check if player found the treasure
            Maze.Cell treasure = gp.maze.getTreasurePosition();
            if (nextRow == treasure.row && nextCol == treasure.col) {
                System.out.println("Treasure found! You win!");
                gameEnded = true;
                return;
            }

            // Animation
            SpriteCounter++;
            if (SpriteCounter > 15) {
                spriteNumber = (spriteNumber == 1) ? 2 : 1;
                SpriteCounter = 0;
            }
        }

        // Update timer
        timeRemaining--;
        if (timeRemaining <= 0) {
            System.out.println("Time's up! Game over.");
            gameEnded = true;
        }
    }

    public void draw(Graphics2D g2) {
        if (gameEnded) return; // Do not draw if game has ended

        BufferedImage image = null;
        switch (direction) {
            case "up":
                image = (spriteNumber == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNumber == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNumber == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNumber == 1) ? right1 : right2;
                break;
        }

        g2.drawImage(image, x, y, gp.tilesize, gp.tilesize, null);
        
        // Optionally draw the timer on the screen
        g2.drawString("Time left: " + timeRemaining, 10, 10);
    }
}
*/
