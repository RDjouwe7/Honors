import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeScreen {

    public static void main(String[] args) {
        // Create the welcome screen JFrame
        JFrame welcomeFrame = new JFrame("Treasure Hunt Game");
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setResizable(false);
        welcomeFrame.setSize(600, 500);
        welcomeFrame.setLocationRelativeTo(null);

        // Set background color and image
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50));

        // Add a beautiful title label at the top
        JLabel titleLabel = new JLabel("Welcome to the Treasure Hunt Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Algerian", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 223, 0)); // Golden color
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 0, 0, 120)); // Slight transparency for background
        titleLabel.setPreferredSize(new Dimension(600, 50));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create a description text area about the game
        JTextArea descriptionArea = new JTextArea(
                "In Treasure Hunt, you will embark on an exciting adventure to find the hidden treasure!\n" +
                "Navigate through the mysterious maze, face challenges, and solve puzzles to uncover the treasure's location.\n" +
                "Are you ready for the challenge?\n\n" +
                "Enter your name below to begin your journey!"
        );
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setForeground(Color.WHITE);
        descriptionArea.setBackground(new Color(50, 50, 50));
        descriptionArea.setCaretColor(Color.WHITE);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setPreferredSize(new Dimension(550, 150));
        descriptionArea.setBorder(BorderFactory.createLineBorder(new Color(255, 223, 0), 2));
        descriptionArea.setMargin(new Insets(10, 10, 10, 10));
        panel.add(descriptionArea, BorderLayout.CENTER);

        // Create a panel for the bottom input area
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(new Color(50, 50, 50));

        // Create the user input label for name entry
        JLabel inputLabel = new JLabel("Enter Your Name: ", SwingConstants.CENTER);
        inputLabel.setFont(new Font("Arial", Font.BOLD, 18));
        inputLabel.setForeground(new Color(255, 223, 0));
        inputPanel.add(inputLabel, BorderLayout.NORTH);

        // Create the name input field
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(nameField, BorderLayout.CENTER);

        // Create the start button with an exciting design
        JButton startButton = new JButton("Start Your Adventure!");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBackground(new Color(255, 223, 0));
        startButton.setForeground(Color.BLACK);
        startButton.setPreferredSize(new Dimension(300, 50));
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createRaisedBevelBorder());

        // Add action listener for the button
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText().trim();
                if (!playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(welcomeFrame, "Welcome, " + playerName + "! Ready to begin your adventure?");
                    welcomeFrame.setVisible(false); // Hide the welcome screen
                    Main.main(new String[]{});  // Call your main class to start the game
                } else {
                    JOptionPane.showMessageDialog(welcomeFrame, "Please enter your name to start the game.");
                }
            }
        });
        
        // Add the start button to the input panel
        inputPanel.add(startButton, BorderLayout.SOUTH);

        // Add input panel to the main panel
        panel.add(inputPanel, BorderLayout.SOUTH);

        // Add the main panel to the JFrame
        welcomeFrame.add(panel);
        welcomeFrame.setVisible(true);
    }
}
