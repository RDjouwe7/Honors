import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    //screen settings

    final int originalTileSize = 16;
    final int scale = 3;
    final int tilesize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth  = tilesize * maxScreenCol;
    final int screenHeight = tilesize * maxScreenRow;

    int FPS = 60;

    KeyHandler KeyH = new KeyHandler(); 
    Thread gameThread; 

    //set players default position

    int playerX = 100;
    int playerY = 100;
    int playerSpeed  = 4;

    public GamePanel(){
        this.setPreferredSize(new Dimension (screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    

    @Override
    public void run() {

        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null){

            
            update();
            
            repaint();

            try{
            double remainingTime = nextDrawTime - System.nanoTime();
            remainingTime = remainingTime/1000000;

            if (remainingTime < 0){
                remainingTime = 0;
            }

            Thread.sleep((long)remainingTime);

            nextDrawTime += drawInterval;
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
                }
            
    public void update() {
       if(KeyH.upPressed==true){
        playerY -= playerSpeed;
       }
       else if(KeyH.downPressed == true){
        playerY += playerSpeed;
       }
       else if(KeyH.leftPressed == true){
            playerX -= playerSpeed;
        }
        else if(KeyH.rightPressed== true){
            playerX += playerSpeed;
           }
    }
    
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D)g; 
            g2.setColor(Color.white);
            g2.fillRect(playerX,playerY,tilesize,tilesize);
            g2.dispose();
        }
}

