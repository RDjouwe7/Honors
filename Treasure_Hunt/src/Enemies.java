import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemies extends Entity {
    GamePanel gp;
    Player player;

    public Enemies(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;

        setDefaultValues();
        getEnemyImage();
    }

    public void setDefaultValues() {
        x = 200;
        y = 200;
        speed = 2;
        direction = "down";
    }

    public void getEnemyImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/enemy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/enemy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/enemy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/enemy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/enemy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/enemy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/enemy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/enemy_right_2.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Calculate direction to player
        int diffX = player.x - x;
        int diffY = player.y - y;
        
        // Store potential next position
        int nextX = x;
        int nextY = y;

        // Move towards player while avoiding walls
        if(Math.abs(diffX) > Math.abs(diffY)) {
            if(diffX > 0) {
                direction = "right";
                nextX += speed;
            } else {
                direction = "left";
                nextX -= speed;
            }
        } else {
            if(diffY > 0) {
                direction = "down";
                nextY += speed;
            } else {
                direction = "up";
                nextY -= speed;
            }
        }
        
        // Check if next position is valid (not a wall)
        int nextRow = nextY / gp.tilesize;
        int nextCol = nextX / gp.tilesize;
        
        if(nextRow >= 0 && nextCol >= 0 && 
           nextRow < gp.maxScreenRow && nextCol < gp.maxScreenCol && 
           gp.mazeLayout[nextRow][nextCol] == 1) {
            x = nextX;
            y = nextY;
        }

        // Animation
        SpriteCounter++;
        if(SpriteCounter > 15) {
            spriteNumber = (spriteNumber == 1) ? 2 : 1;
            SpriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch(direction) {
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