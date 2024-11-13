import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    //screen settings

    final int originalTileSize = 16;
    final int scale = 3;
   public  final int tilesize = originalTileSize * scale;
    final int maxScreenCol = 22;
    final int maxScreenRow = 14;
    final int screenWidth  = tilesize * maxScreenCol;
    final int screenHeight = tilesize * maxScreenRow;

    int FPS = 60;

    KeyHandler KeyH = new KeyHandler(); 
    Thread gameThread; 
    Player player = new Player(this, KeyH);

    //set players default position

    int playerX = 100;
    int playerY = 100;
    int playerSpeed  = 4;

    Maze maze;
int[][] mazeLayout;

    public GamePanel(){
        this.setPreferredSize(new Dimension (screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyH);
        this.setFocusable(true);

        maze = new Maze(maxScreenRow, maxScreenCol);
        mazeLayout = maze.getMaze();
    }
    
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    

    @Override
             public void run(){
                    double drawInterval = 1000000000/FPS;
                    double delta = 0;
                    double lastTime = System.nanoTime();
                    long currentTime;

                    while(gameThread != null){
                        currentTime = System.nanoTime();
                        delta += (currentTime - lastTime)/ drawInterval;
                        lastTime = currentTime;

                        if(delta >=1){
                            update();
                        repaint();
                        delta --;
                        }

                        
                    }

                }
            
    public void update() {
       player.update();
    }
    
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D)g; 

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

            player.draw(g2);
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
