import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeScreen {
    private JFrame gameWindow;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeScreen().createAndShowGUI());
    }

    private void createAndShowGUI() {
        // Create welcome frame
        JFrame welcomeFrame = new JFrame("Treasure Hunt Game");
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setResizable(false);
        welcomeFrame.setSize(600, 500);
        welcomeFrame.setLocationRelativeTo(null);

        // Main panel setup
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50));

        // Title label
        JLabel titleLabel = new JLabel("Welcome to the Treasure Hunt Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Algerian", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 223, 0));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 0, 0, 120));
        titleLabel.setPreferredSize(new Dimension(600, 50));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Game description
        JTextArea descriptionArea = new JTextArea(
            "In Treasure Hunt, you will embark on an exciting adventure to find the hidden treasure!\n" +
            "Navigate through the mysterious maze, face challenges, and solve puzzles to uncover the treasure's location.\n" +
            "Are you ready for the challenge?\n\n" +
            "Enter your name below to begin your journey!"
        );
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setForeground(Color.WHITE);
        descriptionArea.setBackground(new Color(50, 50, 50));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setPreferredSize(new Dimension(550, 150));
        descriptionArea.setBorder(BorderFactory.createLineBorder(new Color(255, 223, 0), 2));
        descriptionArea.setMargin(new Insets(10, 10, 10, 10));
        panel.add(descriptionArea, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(new Color(50, 50, 50));

        // Name input label
        JLabel inputLabel = new JLabel("Enter Your Name: ", SwingConstants.CENTER);
        inputLabel.setFont(new Font("Arial", Font.BOLD, 18));
        inputLabel.setForeground(new Color(255, 223, 0));
        inputPanel.add(inputLabel, BorderLayout.NORTH);

        // Name input field
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(nameField, BorderLayout.CENTER);

        // Start button
        JButton startButton = new JButton("Start Your Adventure!");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBackground(new Color(255, 223, 0));
        startButton.setForeground(Color.BLACK);
        startButton.setPreferredSize(new Dimension(300, 50));
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createRaisedBevelBorder());

        // Start button action
        startButton.addActionListener(e -> {
            String playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                welcomeFrame.dispose();
                startGame(playerName);
            } else {
                JOptionPane.showMessageDialog(welcomeFrame, 
                    "Please enter your name to start the game.",
                    "Name Required",
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        inputPanel.add(startButton, BorderLayout.SOUTH);
        panel.add(inputPanel, BorderLayout.SOUTH);
        welcomeFrame.add(panel);
        welcomeFrame.setVisible(true);
    }

    private void startGame(String playerName) {
        SwingUtilities.invokeLater(() -> {
            gameWindow = new JFrame("Treasure Hunt Game - Player: " + playerName);
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.setResizable(false);

            GamePanel gamePanel = new GamePanel();
            gameWindow.add(gamePanel);
            gameWindow.pack();
            gameWindow.setLocationRelativeTo(null);
            gameWindow.setVisible(true);

            gamePanel.startGameThread();
        });
    }
}
