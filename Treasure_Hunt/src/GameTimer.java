import javax.swing.Timer;
import javax.swing.JOptionPane;
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
    
    // Display game over message with try again option
    private void displayGameOver() {
        int response = JOptionPane.showConfirmDialog(gamePanel, 
            "Time's Up! Game Over!\nWould you like to try again?", 
            "Game Over", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
            
        if (response == JOptionPane.YES_OPTION) {
            gamePanel.resetGame();
        } else {
            System.exit(0);
        }
    }
    
    // Display win message with time remaining
    public void displayWin() {
        stopTimer();
        JOptionPane.showMessageDialog(gamePanel, 
            "Well Done! You've Escaped the Labyrinth!\nTime Remaining: " + timeRemaining + " seconds", 
            "Victory", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Get current time remaining
    public int getTimeRemaining() {
        return timeRemaining;
    }
}
