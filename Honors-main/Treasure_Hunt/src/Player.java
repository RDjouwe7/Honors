
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

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

            // Animation
            SpriteCounter++;
            if (SpriteCounter > 15) {
                spriteNumber = (spriteNumber == 1) ? 2 : 1;
                SpriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
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