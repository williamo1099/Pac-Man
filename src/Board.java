
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Board
 * The class which represents the game board.
 *
 * @author William Oktavianus (williamo1099)
 */
public class Board {

    private final int n, tileLength, totalLength;
    private boolean[][] dot, bigDot, maze;

    /**
     * The constructor for Board class.
     * 
     * @param n the board size
     * @param tileLength  the tile length
     */
    public Board(int n, int tileLength) {
        this.n = n;
        this.tileLength = tileLength;
        this.totalLength = this.tileLength * this.n;

        this.dot = new boolean[this.n][this.n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                double dot = Math.random();
                if (dot >= 0.5) {
                    this.dot[i][j] = true;
                } else {
                    this.dot[i][j] = false;
                }
            }
        }

        this.bigDot = new boolean[this.n][this.n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                double bigDot = Math.random();
                if (bigDot >= 0.97) {
                    this.bigDot[i][j] = true;
                    this.dot[i][j] = false;
                } else {
                    this.bigDot[i][j] = false;
                }
            }
        }

        this.maze = new boolean[this.n][this.n];
        try {
            File file = new File("resources/maze.txt");
            Scanner sc = new Scanner(file);
            for (int i = 0; i < this.n; i++) {
                char[] row = sc.nextLine().toCharArray();
                for (int j = 0; j < row.length; j++) {
                    if (row[j] == '@') {
                        this.maze[i][j] = true;
                        this.dot[i][j] = false;
                        this.bigDot[i][j] = false;
                    } else {
                        this.maze[i][j] = false;
                    }
                }
            }
        } catch (FileNotFoundException e) {
        }

    }

    /**
     * The method to get a dot on the board.
     *
     * @return dot
     */
    public boolean[][] getDot() {
        return dot;
    }

    /**
     * The method to get the maze.
     * 
     * @return maze
     */
    public boolean[][] getMaze() {
        return maze;
    }
    
    /**
     * The method to remove a wall in the maze.
     * 
     * @param i index position i
     * @param j index position j 
     */
    public void setMazeToFalse(int i, int j) {
        this.maze[i][j] = false;
    }

    /**
     * The method to remove a dot.
     *
     * @param i index position i
     * @param j index position j
     */
    public void setDotToFalse(int i, int j) {
        this.dot[i][j] = false;
    }

    /**
     * The method to get a big dot.
     *
     * @return bigDot
     */
    public boolean[][] getBigDot() {
        return bigDot;
    }

    /**
     * The method to remove a big dot.
     *
     * @param i index position i
     * @param j index position j
     */
    public void setBigDotToFalse(int i, int j) {
        this.bigDot[i][j] = false;
    }

    /**
     * The method to draw border on the board.
     *
     * @param g2d
     * @param i index position i
     */
    private void drawBorder(Graphics2D g2d, int i) {
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(0, i * this.tileLength, this.totalLength, i * this.tileLength);
        g2d.drawLine(i * this.tileLength, 0, i * this.tileLength, this.totalLength);
    }

    /**
     * The method to draw a dot on the board.
     *
     * @param g2d
     * @param i index position i
     * @param j index position j
     */
    private void drawDot(Graphics2D g2d, int i, int j) {
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect((i * this.tileLength) + this.tileLength / 2,
                (j * this.tileLength) + this.tileLength / 2,
                this.tileLength / 4,
                this.tileLength / 4, 50, 50);
    }

    /**
     * The method to draw a big dot on the board.
     *
     * @param g2d
     * @param i index position i
     * @param j index position j
     */
    private void drawBigDot(Graphics2D g2d, int i, int j) {
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect((i * this.tileLength) + this.tileLength / 4,
                (j * this.tileLength) + this.tileLength / 4,
                this.tileLength / 2,
                this.tileLength / 2, 50, 50);
    }

    /**
     * The method to draw a wall on the board.
     * 
     * @param g2d
     * @param i index position i
     * @param j index position j
     */
    private void drawMaze(Graphics2D g2d, int i, int j) {
        g2d.setColor(new Color(65, 84, 179));
        g2d.fillRect(i * this.tileLength, j * this.tileLength, this.tileLength, this.tileLength);
    }

    /**
     * The method to draw the board.
     *
     * @param g2d
     */
    protected void draw(Graphics2D g2d) {
        // Draw the borders.
        for (int i = 0; i < this.n; i++) {
            this.drawBorder(g2d, i);
        }

        // Draw the dots.
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.dot[i][j]) {
                    this.drawDot(g2d, i, j);
                }
            }
        }

        // Draw the big dots.
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.bigDot[i][j]) {
                    this.drawBigDot(g2d, i, j);
                }
            }
        }

        // Draw the walls.
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.maze[i][j]) {
                    this.drawMaze(g2d, i, j);
                }
            }
        }
    }

    /**
     * The method to check if all dots have been eaten by Pac-Man.
     *
     * @return True if all dots have been eaten by Pac-Man or False if Pac-Man has not eaten all dots.
     */
    protected boolean finished() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.dot[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
