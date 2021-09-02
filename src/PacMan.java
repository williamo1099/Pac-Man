
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * PacMan
 * The class which represents the Pac-Man.
 *
 * @author William Oktavianus (williamo1099)
 */
public class PacMan {

    private final int n, tileLength, totalLength;
    protected int x, y, direction;
    protected boolean eating, defeated;
    protected boolean[][] maze;

    /**
     * The constructor for PacMan class.
     * 
     * @param n the board size
     * @param tileLength the tile length
     * @param x x position coordinate
     * @param y y position coordinate
     */
    public PacMan(int n, int tileLength, int x, int y) {
        this.n = n;
        this.tileLength = tileLength;
        this.totalLength = this.n * this.tileLength;
        this.x = x;
        this.y = y;
        this.direction = 3; // Set left as the default direction.
        this.eating = false;
        this.defeated = false;
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
     * The method to set the x position coordinate.
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
     * The method to set the y position coordinate.
     *
     * @param y y position coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * The method to get the facing direction.
     * 0 is up, 1 is right, 2 is down and 3 is left.
     *
     * @return facing direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * The method to set the facing direction.
     * 0 is up, 1 is right, 2 is down and 3 is left.
     *
     * @param direction facing direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * The method to set the eating status.
     * The status is True if Pac-Man is eating a dot or a big dot or False if Pac-Man is not eating.
     *
     * @param eating eating status
     */
    public void setEating(boolean eating) {
        this.eating = eating;
    }

    /**
     * The method to set the defeated status.
     * The status is True if Pac-Man has been eaten by a ghost or False if Pac-Man has not been eaten by a ghost.
     * 
     * @param defeated defeated status
     */
    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
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
     * The method to draw Pac-Man.
     *
     * @param g2d
     * @param x x position coordinate
     * @param y y position coordinate
     * @param width width of Pac-Man
     * @param height height of Pac-Man
     */
    private void drawPacMan(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(new Color(255, 253, 56));
        double xc = x + (width / 2), yc = y + (height / 2);
        Area body = new Area(new Ellipse2D.Double(x, y, width, height));
        Area mouth = new Area(new Arc2D.Double(x, y, width, height, 130, 90, Arc2D.PIE));
        body.subtract(mouth);

        AffineTransform transform = new AffineTransform();
        transform.translate(xc, yc);
        switch (this.direction) {
            case 0:
                // If the facing direction is up.
                transform.rotate(Math.toRadians(90));
                break;
            case 1:
                // If the facing direction is right.
                transform.rotate(Math.toRadians(170));
                break;
            case 2:
                // If the facing direction is down.
                transform.rotate(Math.toRadians(-100));
                break;
            default:
                // If the facing direction is left.
                transform.rotate(Math.toRadians(0));
                break;
        }
        transform.translate(-xc, -yc);
        Shape pacMan = transform.createTransformedShape(body);
        g2d.fill(pacMan);
    }

    /**
     * The method to draw eating Pac-Man.
     *
     * @param g2d
     * @param x x position coordinate
     * @param y y position coordinate
     * @param width width of Pac-Man
     * @param height height of Pac-Man
     */
    private void drawPacManEating(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(new Color(255, 253, 56));
        Area body = new Area(new Ellipse2D.Double(x, y, width, height));
        g2d.fill(body);
    }

    /**
     * The method to remove Pac-Man.
     *
     * @param g2d
     * @param x x position coordinate
     * @param y y position coordinate
     * @param width width of Pac-Man
     * @param height height of Pac-Man
     */
    private void removePacMan(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(Color.BLACK);
        Area body = new Area(new Ellipse2D.Double(x, y, width, height));
        g2d.fill(body);
    }

    /**
     * The method to draw Pac-Man on the board.
     *
     * @param g2d
     */
    protected void draw(Graphics2D g2d) {
        if (!this.defeated) {
            // If the players have not lost the game.
            if (this.eating) {
                // If Pac-Man is eating a dot or a big dot.
                this.drawPacManEating(g2d, (this.x * this.tileLength) + this.tileLength / 4,
                        (this.y * this.tileLength) + this.tileLength / 4,
                        this.tileLength / 2,
                        this.tileLength / 2);
                
                // Set a timer for 10ms to make Pac-Man visibly eating.
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        setEating(false);
                    }
                }, 10);
            } else {
                // If Pac-Man is not eating a dot or a big dot.
                this.drawPacMan(g2d, (this.x * this.tileLength) + this.tileLength / 4,
                        (this.y * this.tileLength) + this.tileLength / 4,
                        this.tileLength / 2,
                        this.tileLength / 2);
            }
        } else {
            // If the players have lost the game, hide Pac-Man.
            this.removePacMan(g2d, (this.x * this.tileLength) + this.tileLength / 4,
                    (this.y * this.tileLength) + this.tileLength / 4,
                    this.tileLength / 2,
                    this.tileLength / 2);
        }
    }

    /**
     * The method to move Pac-Man.
     * 0 is up, 1 is right, 2 is down and 3 is left.
     *
     * @param direction moving direction
     */
    protected void move(int direction) {
        switch (direction) {
            case 0:
                // Move up.
                if (this.y > 0 && !this.maze[this.x][this.y - 1]) {
                    this.y = this.y - 1;
                    this.direction = 0;
                }
                break;
            case 1:
                // Move right.
                if (this.x < this.n - 1 && !this.maze[this.x + 1][this.y]) {
                    this.x = this.x + 1;
                    this.direction = 1;
                }
                break;
            case 2:
                // Move down.
                if (this.y < this.n - 1 && !this.maze[this.x][this.y + 1]) {
                    this.y = this.y + 1;
                    this.direction = 2;
                }
                break;
            default:
                // Move left.
                if (this.x > 0 && !this.maze[this.x - 1][this.y]) {
                    this.x = this.x - 1;
                    this.direction = 3;
                }
                break;
        }
    }
}
