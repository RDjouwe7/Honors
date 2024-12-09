import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class Enemies extends Entity {
    GamePanel gp;
    Player player;
    Random random = new Random();
    String[] possibleDirections = {"up", "down", "left", "right"};
    private static final int DETECTION_RANGE = 150; // Distance to detect player
    private boolean isChasing = false;

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
        direction = possibleDirections[random.nextInt(possibleDirections.length)];
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
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

    private boolean isPlayerNearby() {
        // Calculate distance to player using Pythagorean theorem
        int dx = Math.abs(player.x - x);
        int dy = Math.abs(player.y - y);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= DETECTION_RANGE;
    }

    private boolean canMove(int nextX, int nextY) {
        int nextRow = nextY / gp.tilesize;
        int nextCol = nextX / gp.tilesize;
        
        return nextRow >= 0 && nextCol >= 0 && 
               nextRow < gp.maxScreenRow && nextCol < gp.maxScreenCol && 
               gp.mazeLayout[nextRow][nextCol] == 1;
    }

    private String getRandomNewDirection() {
        java.util.List<String> validDirections = new java.util.ArrayList<>();
        
        if (canMove(x, y - speed)) validDirections.add("up");
        if (canMove(x, y + speed)) validDirections.add("down");
        if (canMove(x - speed, y)) validDirections.add("left");
        if (canMove(x + speed, y)) validDirections.add("right");
        
        validDirections.remove(direction);
        
        if (validDirections.isEmpty()) {
            validDirections.add(direction);
        }
        
        return validDirections.get(random.nextInt(validDirections.size()));
    }

    private void updatePatrolMovement() {
        int nextX = x;
        int nextY = y;
        
        switch(direction) {
            case "up": nextY -= speed; break;
            case "down": nextY += speed; break;
            case "left": nextX -= speed; break;
            case "right": nextX += speed; break;
        }
        
        if (canMove(nextX, nextY)) {
            x = nextX;
            y = nextY;
            if (random.nextInt(100) < 5) {
                direction = getRandomNewDirection();
            }
        } else {
            direction = getRandomNewDirection();
        }
    }

    private void updateChaseMovement() {
        int nextX = x;
        int nextY = y;
        
        // Calculate direction to player
        int diffX = player.x - x;
        int diffY = player.y - y;
        
        // Determine primary direction based on larger difference
        if (Math.abs(diffX) > Math.abs(diffY)) {
            // Try horizontal movement first
            nextX += diffX > 0 ? speed : -speed;
            if (canMove(nextX, y)) {
                x = nextX;
                direction = diffX > 0 ? "right" : "left";
            } else {
                // Try vertical movement if horizontal fails
                nextY += diffY > 0 ? speed : -speed;
                if (canMove(x, nextY)) {
                    y = nextY;
                    direction = diffY > 0 ? "down" : "up";
                }
            }
        } else {
            // Try vertical movement first
            nextY += diffY > 0 ? speed : -speed;
            if (canMove(x, nextY)) {
                y = nextY;
                direction = diffY > 0 ? "down" : "up";
            } else {
                // Try horizontal movement if vertical fails
                nextX += diffX > 0 ? speed : -speed;
                if (canMove(nextX, y)) {
                    x = nextX;
                    direction = diffX > 0 ? "right" : "left";
                }
            }
        }
    }

    public void update() {
        // Check if player is nearby
        isChasing = isPlayerNearby();
        
        // Update movement based on mode
        if (isChasing) {
            updateChaseMovement();
        } else {
            updatePatrolMovement();
        }

        // Update animation
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

        int size = gp.originalTileSize * gp.scale * 2;
        g2.drawImage(image, x, y, size, size, null);
    }
}
