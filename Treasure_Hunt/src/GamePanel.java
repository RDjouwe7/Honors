import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tilesize = originalTileSize * scale;
    final int maxScreenCol = 22;
    final int maxScreenRow = 14;
    final int screenWidth = tilesize * maxScreenCol;
    final int screenHeight = tilesize * maxScreenRow;

    // FPS
    int FPS = 60;

    // System
    KeyHandler KeyH = new KeyHandler();
    Thread gameThread;
    Player player;
    public ArrayList<Enemies> enemies = new ArrayList<>();
    private boolean isGameOver = false;

    // Game state
    Maze maze;
    int[][] mazeLayout;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyH);
        this.setFocusable(true);

        // Initialize game components
        player = new Player(this, KeyH);
        maze = new Maze(maxScreenRow, maxScreenCol);
        mazeLayout = maze.getMaze();
        setupEnemies();
    }

    private void setupEnemies() {
        enemies.clear();
        
        // Define safe zone for player (top-left area)
        int safeZoneSize = 5; // 5 tiles from start
        
        // Find valid spawn positions (must be on paths and away from player start)
        java.util.List<Point> validSpawnPoints = new java.util.ArrayList<>();
        for (int row = 0; row < maxScreenRow; row++) {
            for (int col = 0; col < maxScreenCol; col++) {
                // Check if it's a path and not in the safe zone
                if (mazeLayout[row][col] == 1 && 
                    (row > safeZoneSize || col > safeZoneSize)) {
                    validSpawnPoints.add(new Point(col * tilesize, row * tilesize));
                }
            }
        }
        
        // Spawn enemies at random valid positions
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            if (!validSpawnPoints.isEmpty()) {
                int index = random.nextInt(validSpawnPoints.size());
                Point spawnPoint = validSpawnPoints.get(index);
                
                Enemies enemy = new Enemies(this, player);
                enemy.setPosition(spawnPoint.x, spawnPoint.y);
                enemies.add(enemy);
                
                // Remove used spawn point to avoid duplicates
                validSpawnPoints.remove(index);
            }
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        Thread thisThread = Thread.currentThread();

        while(gameThread == thisThread) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void gameOver() {
        isGameOver = true;
        
        Thread currentThread = gameThread;
        gameThread = null;
        
        if (currentThread != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        JOptionPane.showMessageDialog(this, 
            "Game Over! You were caught by the enemy!", 
            "Game Over", 
            JOptionPane.INFORMATION_MESSAGE);
        
        int response = JOptionPane.showConfirmDialog(this, 
            "Would you like to play again?", 
            "Play Again?", 
            JOptionPane.YES_NO_OPTION);
            
        if (response == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
        if (gameThread != null) {
            gameThread = null;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        isGameOver = false;
        player.setGameOver(false);
        player.setDefaultValues();
        maze = new Maze(maxScreenRow, maxScreenCol);
        mazeLayout = maze.getMaze();
        setupEnemies();
        startGameThread();
    }

    public void update() {
        if (!isGameOver) {
            player.update();
            for (Enemies enemy : enemies) {
                enemy.update();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        for (int row = 0; row < maxScreenRow; row++) {
            for (int col = 0; col < maxScreenCol; col++) {
                if (mazeLayout[row][col] == 0) {
                    g2.setColor(Color.darkGray);
                } else {
                    g2.setColor(Color.lightGray);
                }
                g2.fillRect(col * tilesize, row * tilesize, tilesize, tilesize);
            }
        }

        if (!isGameOver) {
            player.draw(g2);
            for (Enemies enemy : enemies) {
                enemy.draw(g2);
            }
        }

        if (isGameOver) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            String gameOverText = "GAME OVER!";
            int x = screenWidth/2 - g2.getFontMetrics().stringWidth(gameOverText)/2;
            int y = screenHeight/2;
            g2.drawString(gameOverText, x, y);
        }

        g2.dispose();
    }
}


/*import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tilesize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tilesize * maxScreenCol;
    final int screenHeight = tilesize * maxScreenRow;

    int FPS = 60;

    KeyHandler KeyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, KeyH);

    Maze maze;
    int[][] mazeLayout;
    Maze.Cell treasurePosition;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyH);
        this.setFocusable(true);

        maze = new Maze(maxScreenRow, maxScreenCol);
        mazeLayout = maze.getMaze();
        treasurePosition = maze.getTreasurePosition(); // Initialize treasure position from the maze
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        double lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw the maze
        for (int row = 0; row < maxScreenRow; row++) {
            for (int col = 0; col < maxScreenCol; col++) {
                if (mazeLayout[row][col] == 0) {
                    g2.setColor(Color.darkGray); // Wall color
                } else {
                    g2.setColor(Color.lightGray); // Path color
                }
                g2.fillRect(col * tilesize, row * tilesize, tilesize, tilesize);
            }
        }

        // Draw the treasure
        if (!player.gameEnded) { // Only draw if game is not over
            g2.setColor(Color.yellow);
            g2.fillRect(treasurePosition.col * tilesize, treasurePosition.row * tilesize, tilesize, tilesize);
        }

        // Draw the player
        player.draw(g2);

        // Draw timer
        g2.setColor(Color.white);
        g2.drawString("Time left: " + player.timeRemaining, 10, 20);

        // Check for game end status
        if (player.gameEnded) {
            g2.setColor(Color.red);
            g2.drawString("Game Over!", screenWidth / 2 - 30, screenHeight / 2);
        }

        g2.dispose();
    }
}*/
