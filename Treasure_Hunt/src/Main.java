import javax.swing.JFrame;

// Main class that sets up the game window and starts the game
public class Main {
    public static void main(String[] args){
        JFrame window = new JFrame(); // Create a new JFrame window for the game
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the window when existing
        window.setResizable(false); // Make the window non-resizable 
        window.setTitle("Treasure Hunt Game"); // Set the title of the game window

        GamePanel gamepanel = new GamePanel(); // Create an instance of the GamePanel (where game logic and rendering happens)
        window.add(gamepanel); // Add the game panel to the window

        window.pack(); // Resize the window to fit the preferred size of the game panel

        window.setLocationRelativeTo(null); // Center the window on the screen
        window.setVisible(true); // Make the window visible

        gamepanel.startGameThread(); // Start the game thread (game loop)
    }

}
