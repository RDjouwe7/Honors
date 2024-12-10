import java.util.Random;
import java.util.Stack;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.awt.Graphics2D;

public class Maze {
    private final int rows;
    private final int cols;
    private final int[][] maze;
    private final Random random = new Random();
    private BufferedImage doorClosedImage, doorOpenedImage;
    private int doorRow, doorCol;
    private boolean doorOpened;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new int[rows][cols];
        generateMaze();  // Generate the maze first
        placeDoor();     // Place the door after generating the maze
        loadDoorImages(); // Load images for the door
        doorOpened = false; // Initialize door as closed
    }

    // Load images for door
    private void loadDoorImages() {
        try {
            doorClosedImage = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/door_closed.png"));
            doorOpenedImage = ImageIO.read(getClass().getResourceAsStream("/Walking sprites/door_opened.png"));
        } catch (IOException e) {
            System.out.println("Error loading door image at: /Walking sprites/door_closed.png");
            e.printStackTrace();
        }
    }

    // Get the door's position
    public int getDoorRow() {
        return doorRow;
    }

    public int getDoorCol() {
        return doorCol;
    }

    public boolean isDoorOpened() {
        return doorOpened;
    }

    // Place the door in the maze at a random valid position
    private void placeDoor() {
        while (true) {
            doorRow = random.nextInt(rows);  // Random row position
            doorCol = random.nextInt(cols);  // Random column position
            // Ensure that the door is placed on a valid path (not a wall)
            if (maze[doorRow][doorCol] == 1) {
                break;  // Exit the loop once a valid position is found
            }
        }
    }

    // Draw the door in its current state (open or closed)
    public void drawDoor(Graphics2D g2, int tileSize) {
        BufferedImage doorImage = doorOpened ? doorOpenedImage : doorClosedImage;
        g2.drawImage(doorImage, doorCol * tileSize, doorRow * tileSize, tileSize, tileSize, null);
    }

    // Mark the door as opened
    public void openDoor() {
        doorOpened = true;  // Set the door as opened
        
    }


    // Get the maze layout
    public int[][] getMaze() {
        return maze;
    }

    // Generate the maze using recursive backtracking
    private void generateMaze() {
        Stack<Cell> stack = new Stack<>();
        Cell start = new Cell(0, 0);  // Starting cell
        maze[start.row][start.col] = 1;  // Mark starting cell as a path
        stack.push(start);

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            Cell next = getNextCell(current);

            if (next != null) {
                maze[next.row][next.col] = 1;
                maze[(current.row + next.row) / 2][(current.col + next.col) / 2] = 1; // Carve path between cells
                stack.push(next);
            } else {
                stack.pop();  // Backtrack if no valid neighboring cells
            }
        }
    }

    // Get the next cell to move to
    private Cell getNextCell(Cell cell) {
        Stack<Cell> neighbors = new Stack<>();

        if (isValid(cell.row - 2, cell.col)) neighbors.push(new Cell(cell.row - 2, cell.col));  // Up
        if (isValid(cell.row + 2, cell.col)) neighbors.push(new Cell(cell.row + 2, cell.col));  // Down
        if (isValid(cell.row, cell.col - 2)) neighbors.push(new Cell(cell.row, cell.col - 2));  // Left
        if (isValid(cell.row, cell.col + 2)) neighbors.push(new Cell(cell.row, cell.col + 2));  // Right

        if (neighbors.isEmpty()) return null;  // No valid neighbors

        return neighbors.get(random.nextInt(neighbors.size()));  // Randomly select a neighbor
    }

    // Check if a cell is within bounds and is a wall (0)
    private boolean isValid(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols && maze[row][col] == 0;
    }

    // Helper class to represent a cell in the maze
    private static class Cell {
        int row, col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}

/*import java.util.Random;
import java.util.Stack;

public class Maze {
    private final int rows;
    private final int cols;
    private final int[][] maze;
    private final Random random = new Random();
    private Cell treasure; // Store the treasure cell

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new int[rows][cols];
        generateMaze();
        placeTreasure(); // Place treasure after maze generation
    }

    // 0 = wall, 1 = path
    public int[][] getMaze() {
        return maze;
    }

    // Getter for treasure position
    public Cell getTreasurePosition() {
        return treasure;
    }

    // Recursive Backtracking to generate a new maze
    private void generateMaze() {
        Stack<Cell> stack = new Stack<>();
        Cell start = new Cell(0, 0);
        maze[start.row][start.col] = 1; // Starting cell as a path
        stack.push(start);

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            Cell next = getNextCell(current);

            if (next != null) {
                maze[next.row][next.col] = 1;
                maze[(current.row + next.row) / 2][(current.col + next.col) / 2] = 1; // Carve path between cells
                stack.push(next);
            } else {
                stack.pop();
            }
        }
    }

    // Place the treasure randomly on a path cell
    private void placeTreasure() {
        while (true) {
            int row = randomOdd(rows);
            int col = randomOdd(cols);

            if (maze[row][col] == 1) { // Ensure the cell is a path, not a wall
                treasure = new Cell(row, col);
                break;
            }
        }
    }

    // Get the next cell to move to
    private Cell getNextCell(Cell cell) {
        Stack<Cell> neighbors = new Stack<>();

        if (isValid(cell.row - 2, cell.col)) neighbors.push(new Cell(cell.row - 2, cell.col));
        if (isValid(cell.row + 2, cell.col)) neighbors.push(new Cell(cell.row + 2, cell.col));
        if (isValid(cell.row, cell.col - 2)) neighbors.push(new Cell(cell.row, cell.col - 2));
        if (isValid(cell.row, cell.col + 2)) neighbors.push(new Cell(cell.row, cell.col + 2));

        if (neighbors.isEmpty()) return null;

        return neighbors.get(random.nextInt(neighbors.size()));
    }

    // Check if cell is within bounds and is a wall
    private boolean isValid(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols && maze[row][col] == 0;
    }

    // Generate a random odd integer for row or column selection
    private int randomOdd(int limit) {
        int num = random.nextInt(limit / 2) * 2 + 1; // Ensure the index is odd
        return Math.min(num, limit - 1); // Stay within bounds
    }

    // Helper class for cells
    public static class Cell {
        int row, col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
*/
