
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import javax.sound.sampled.Clip;

/**
 * Ghost
 * The class which represents a ghost.
 *
 * @author William Oktavianus (williamo1099)
 */
public class Ghost {

    private final int n, tileLength, totalLength;
    private int x, y;
    private Color color;
    private boolean bigDotEaten, eaten;
    private boolean[][] maze;

    /**
     * The constructor for Ghost class.
     * 
     * @param n the board size
     * @param tileLength the tile length
     * @param x x position coordinate
     * @param y y position coordinate
     * @param color ghost color
     */
    public Ghost(int n, int tileLength, int x, int y, Color color) {
        this.n = n;
        this.tileLength = tileLength;
        this.totalLength = this.n * this.tileLength;
        this.x = x;
        this.y = y;
        this.color = color;
        this.bigDotEaten = false;
        this.eaten = false;
    }

    /**
     * The method to get x position coordinate.
     *
     * @return x position coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * The method to set x position coordinate.
     *
     * @param x x position coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * The method to get y position coordinate.
     *
     * @return y position coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * The method to set y position coordinate.
     *
     * @param y y position coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * The method to get the ghost color.
     *
     * @return ghost color
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * The method to set the ghost color.
     *
     * @param color ghost color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * The method to get the big dot status.
     * The status is True if a big dot has been eaten by Pac-Man or False if a big dot has not been eaten.
     *
     * @return big dot status
     */
    public boolean isBigDotEaten() {
        return bigDotEaten;
    }

    /**
     * The method to set the big dot status temporarily (for 5 seconds).
     *
     * @param bigDotEaten big dot status
     * @param sound ghosts sound effect while a big dot is eaten
     */
    public void setBigDotEaten(boolean bigDotEaten, Clip sound) {
        this.bigDotEaten = bigDotEaten;
        
        // Set a timer for 5 seconds (5000 miliseconds).
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                setBigDotEatenToFalse();
                sound.stop();
            }
        }, 5000);
    }

    /**
     * The method to set the big dot status to False.
     */
    public void setBigDotEatenToFalse() {
        this.bigDotEaten = false;
        this.eaten = false;
    }

    /**
     * The method to set the eaten status.
     * The status is True if the ghost has been eaten by Pac-Man or False if the ghost has not been eaten.
     *
     * @param eaten eaten status
     */
    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }

    /**
     * The method to set the maze.
     *
     * @param maze
     */
    public void setMaze(boolean[][] maze) {
        this.maze = maze;
    }

    /**
     * The method to draw the ghost.
     *
     * @param g2d
     * @param x x position coordinate
     * @param y y position coordinate
     * @param width width of the ghost
     * @param height height of the ghost
     */
    private void drawGhost(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(this.color);
        GeneralPath shape = new GeneralPath();
        shape.moveTo(x, y + height);
        shape.quadTo(x + (width / 2), y - height, x + width, y + height);
        g2d.fill(shape);
    }

    /**
     * The method to draw the blue ghost (when a big dot is eaten by Pac-Man).
     *
     * @param g2d
     * @param x x position coordinate
     * @param y y position coordinate
     * @param width width of the ghost
     * @param height height of the ghost
     */
    private void drawBlueGhost(Graphics2D g2d, int x, int y, int width, int height) {
        if (!this.eaten) {
            g2d.setColor(new Color(16, 88, 251));
            GeneralPath shape = new GeneralPath();
            shape.moveTo(x, y + height);
            shape.quadTo(x + (width / 2), y - height, x + width, y + height);
            g2d.fill(shape);
        }
    }

    /**
     * The method to draw the ghost on board.
     *
     * @param g2d
     */
    protected void draw(Graphics2D g2d) {
        if (!this.bigDotEaten) {
            this.drawGhost(g2d, (this.x * this.tileLength) + this.tileLength / 4,
                    (this.y * this.tileLength) + this.tileLength / 4,
                    this.tileLength / 2,
                    this.tileLength / 2);
        } else {
            this.drawBlueGhost(g2d, (this.x * this.tileLength) + this.tileLength / 4,
                    (this.y * this.tileLength) + this.tileLength / 4,
                    this.tileLength / 2,
                    this.tileLength / 2);
        }
    }

    /**
     * The method to move the ghost randomly.
     */
    protected void randomizedMove() {
        double move = Math.random();
        if (move >= 0 && move < 0.25) {
            // Move up.
            if (this.y > 0 && !this.maze[this.x][this.y - 1]) {
                this.y = this.y - 1;
            }

        } else if (move >= 0.25 && move < 0.5) {
            // Move right.
            if (this.x < this.n - 1 && !this.maze[this.x + 1][this.y]) {
                this.x = this.x + 1;
            }

        } else if (move >= 0.5 && move < 0.75) {
            // Move down.
            if (this.y < this.n - 1 && !this.maze[this.x][this.y + 1]) {
                this.y = this.y + 1;
            }

        } else {
            // Move left.
            if (this.x > 0 && !this.maze[this.x - 1][this.y]) {
                this.x = this.x - 1;
            }

        }
    }
}
