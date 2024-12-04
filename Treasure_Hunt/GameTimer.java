// GameTimer.java - Handles the countdown timer functionality
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameTimer {
    private int timeRemaining;         // Time remaining in seconds
    private Timer timer;               // Swing timer for countdown
    private final Player player;       // Reference to player
    private final GamePanel gamePanel; // Reference to game panel
    
    // Constructor
    public GameTimer(Player player, GamePanel gamePanel, int startTime) {
        this.player = player;
        this.gamePanel = gamePanel;
        this.timeRemaining = startTime;
        
        // Create timer that ticks every second (1000 milliseconds)
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimer();
            }
        });
    }
    
    // Start the timer
    public void startTimer() {
        timer.start();
    }
    
    // Stop the timer
    public void stopTimer() {
        timer.stop();
    }
    
    // Update timer and check win/lose conditions
    private void updateTimer() {
        timeRemaining--;
        
        if (timeRemaining <= 0) {
            gameOver();
        }
    }
    
    // Handle game over condition
    private void gameOver() {
        stopTimer();
        player.setGameOver(true);
        displayGameOver();
    }
    
    // Display game over message
    private void displayGameOver() {
        javax.swing.JOptionPane.showMessageDialog(null, 
            "Time's Up! Game Over!", 
            "Game Over", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Display win message
    public void displayWin() {
        stopTimer();
        javax.swing.JOptionPane.showMessageDialog(null, 
            "Well Done! You've Escaped the Labyrinth!", 
            "Victory", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Get current time remaining
    public int getTimeRemaining() {
        return timeRemaining;
    }
}
