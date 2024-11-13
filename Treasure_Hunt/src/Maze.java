import java.util.Random;
import java.util.Stack;

public class Maze {
    private final int rows;
    private final int cols;
    private final int[][] maze;
    private final Random random = new Random();
    

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new int[rows][cols];
        generateMaze();
    }

    // 0 = wall, 1 = path
    public int[][] getMaze() {
        return maze;
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

    // Helper class for cells
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