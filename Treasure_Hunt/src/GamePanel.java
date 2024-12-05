import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Random;

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
    private GameTimer gameTimer;

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
        
        // Initialize timer with 60 seconds
        gameTimer = new GameTimer(player, this, 60);
    }

    private void setupEnemies() {
        enemies.clear();
        
        // Define safe zone for player (top-left area)
        int safeZoneSize = 5;
        
        // Find valid spawn positions
        ArrayList<Point> validSpawnPoints = new ArrayList<>();
        for (int row = 0; row < maxScreenRow; row++) {
            for (int col = 0; col < maxScreenCol; col++) {
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
                
                validSpawnPoints.remove(index);
            }
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        gameTimer.startTimer();
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
        gameTimer.stopTimer();
        
        Thread currentThread = gameThread;
        gameThread = null;
        
        if (currentThread != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        int response = JOptionPane.showConfirmDialog(this, 
            "Game Over! You were caught by the enemy!\nWould you like to play again?", 
            "Game Over", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
            
        if (response == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    public void resetGame() {
        if (gameThread != null) {
            gameThread = null;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (gameTimer != null) {
            gameTimer.stopTimer();
        }

        isGameOver = false;
        player.setGameOver(false);
        player.setDefaultValues();
        maze = new Maze(maxScreenRow, maxScreenCol);
        mazeLayout = maze.getMaze();
        setupEnemies();
        
        // Reset timer to 60 seconds
        gameTimer = new GameTimer(player, this, 60);
        
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

        // Draw maze
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

        // Draw game elements if not game over
        if (!isGameOver) {
            player.draw(g2);
            for (Enemies enemy : enemies) {
                enemy.draw(g2);
            }
            
            // Draw timer
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.drawString("Time: " + gameTimer.getTimeRemaining(), 10, 30);
        }

        // Draw game over screen
        if (isGameOver) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            String gameOverText = gameTimer.getTimeRemaining() <= 0 ? "TIME'S UP!" : "GAME OVER!";
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
