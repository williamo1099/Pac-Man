
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

/**
 * Game
 * The class to control the game flow.
 *
 * @author William Oktavianus (williamo1099)
 */
public class Game extends JPanel implements Runnable, KeyListener {

    private final int n, tileLength, totalLength;
    private boolean start = false;
    private Board board;
    private PacMan pacMan;
    private Ghost[] ghosts;
    private int score;
    private boolean bigDotEaten, defeated, won;
    
    // Sound effect attributes
    private AudioInputStream introAudioInputStream, wakaAudioInputStream, movingGhostsAudioInputStream, scaredGhostsAudioInputStream, dieAudioInputStream, winAudioInputStream;
    private Clip intro, wakaWaka, movingGhosts, scaredGhosts, die, win;

    /**
     * The constructor for Game class.
     * 
     * @param n the board size
     * @param tileLength the tile length
     */
    public Game(int n, int tileLength) {
        this.addKeyListener(this);
        this.n = n;
        this.tileLength = tileLength;
        this.totalLength = this.n * this.tileLength;
        this.init();
    }

    /**
     * The method to initialize other attributes.
     * Those attributes are initialized separately because players can repeat the game after winning or losing the game.
     */
    public void init() {
        this.board = new Board(this.n, this.tileLength);
        this.pacMan = new PacMan(this.n, this.tileLength, this.n - 1, 0);
        this.board.setDotToFalse(this.n - 1, 0);
        this.board.setBigDotToFalse(this.n - 1, 0);
        this.board.setMazeToFalse(this.n - 1, 0);
        this.pacMan.setMaze(this.board.getMaze());
        this.ghosts = new Ghost[4];
        Color[] colors = new Color[]{new Color(44, 255, 254), new Color(254, 186, 253), new Color(253, 183, 91), new Color(252, 13, 27)};
        for (int i = 0; i < this.ghosts.length; i++) {
            int x, y;
            do {
                x = (int) (Math.random() * (n - 1));
                y = (int) (Math.random() * (n - 1));
            } while (this.board.getMaze()[x][y]);
            this.ghosts[i] = new Ghost(this.n, this.tileLength, x, y, colors[i]);
            this.ghosts[i].setMaze(this.board.getMaze());
        }
        this.score = 0;
        this.bigDotEaten = false;
        this.defeated = false;
        this.won = false;
        
        try {
            // Add the intro sound effect.
            this.introAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Intro.wav").getAbsoluteFile());
            this.intro = AudioSystem.getClip();
            this.intro.open(this.introAudioInputStream);
            this.intro.loop(Clip.LOOP_CONTINUOUSLY);
            this.intro.start();

            // Add the ghosts sound effect.
            this.movingGhostsAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Moving Ghosts.wav").getAbsoluteFile());
            this.movingGhosts = AudioSystem.getClip();
            this.movingGhosts.open(this.movingGhostsAudioInputStream);

            // Add the ghosts sound effect while a big dot is eaten.
            this.scaredGhostsAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Scared Ghosts.wav").getAbsoluteFile());
            this.scaredGhosts = AudioSystem.getClip();
            this.scaredGhosts.open(this.scaredGhostsAudioInputStream);

            // Add the die sound effect.
            this.dieAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Die.wav").getAbsoluteFile());
            this.die = AudioSystem.getClip();
            this.die.open(this.dieAudioInputStream);

            // Add the victory sound effect.
            this.winAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Victory.wav").getAbsoluteFile());
            this.win = AudioSystem.getClip();
            this.win.open(this.winAudioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * The method to check if Pac-Man successfully eats a big dot.
     *
     * @return True if Pac-Man successfully eats a big dot or False jika Pac-Man does not eat a big dot.
     */
    private boolean bigDotEaten() {
        if (this.board.getBigDot()[this.pacMan.getX()][this.pacMan.getY()]) {
            this.bigDotEaten = true;
            for (int i = 0; i < 4; i++) {
                this.ghosts[i].setBigDotEaten(true, this.scaredGhosts);
            }
            this.scaredGhosts.loop(Clip.LOOP_CONTINUOUSLY);
            return true;
        }
        return false;
    }

    /**
     * The method to check if Pac-Man has been eaten by a ghost.
     *
     * @return True if Pac-Man has been eaten by a ghost or False if Pac-Man has not been eaten by a ghost.
     */
    private boolean defeated() {
        if (!this.bigDotEaten) {
            for (int i = 0; i < 4; i++) {
                if (this.pacMan.getX() == this.ghosts[i].getX()
                        && this.pacMan.getY() == this.ghosts[i].getY()) {
                    this.defeated = true;
                    this.pacMan.setDefeated(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The method to check if Pac-Mas has successfully eaten all dots.
     *
     * @return True if Pac-Man has successfully eaten all dots or False if Pac-Man has not eaten all dots.
     */
    private boolean won() {
        if (this.board.finished()) {
            this.won = true;
            return true;
        }
        return false;
    }

    /**
     * The method to print the game intro screen.
     *
     * @param g2d
     */
    private void printIntro(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        Font font = new Font("Courier New", Font.BOLD, 50);
        g2d.setFont(font);
        g2d.drawString("Welcome", 60, 250);

        font = new Font("Courier New", Font.BOLD, 25);
        g2d.setFont(font);
        g2d.drawString("Press SPACE to start", 60, 280);

        font = new Font("Courier New", Font.BOLD, 25);
        g2d.setFont(font);
        g2d.drawString("Press ESC to quit", 60, 300);
    }

    /**
     * The method to print current score on the screen.
     *
     * @param g2d
     */
    private void printScore(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        Font font = new Font("Courier New", Font.BOLD, 30);
        g2d.setFont(font);
        g2d.drawString("Score : " + this.score, 10, this.totalLength + 30);
    }

    /**
     * The method to print game over screen and current score.
     *
     * @param g2d
     */
    private void printGameOver(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        Font font = new Font("Courier New", Font.BOLD, 70);
        g2d.setFont(font);
        g2d.drawString("GAME OVER", 60, 250);

        font = new Font("Courier New", Font.BOLD, 25);
        g2d.setFont(font);
        g2d.drawString("Score : " + this.score, 60, 280);

        font = new Font("Courier New", Font.BOLD, 25);
        g2d.setFont(font);
        g2d.drawString("Press SPACE to play again", 60, 310);

        font = new Font("Courier New", Font.BOLD, 25);
        g2d.setFont(font);
        g2d.drawString("Press ESC to quit", 60, 340);
    }

    /**
     * The method to print victory screen.
     *
     * @param g2d
     */
    private void printWin(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        Font font = new Font("Courier New", Font.BOLD, 70);
        g2d.setFont(font);
        g2d.drawString("WIN", 60, 250);

        font = new Font("Courier New", Font.BOLD, 25);
        g2d.setFont(font);
        g2d.drawString("Score : " + this.score, 60, 280);

        font = new Font("Courier New", Font.BOLD, 25);
        g2d.setFont(font);
        g2d.drawString("Press SPACE to play again", 60, 310);

        font = new Font("Courier New", Font.BOLD, 25);
        g2d.setFont(font);
        g2d.drawString("Press ESC to quit", 60, 340);
    }

    /**
     * The method to print the game.
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setBackground(Color.BLACK);

        if (this.start) {
            // Draw the game board.
            this.board.draw(g2d);
            
            // Draw Pac-Man on the board.
            this.pacMan.draw(g2d);
            
            // Draw four ghosts on the board.
            for (Ghost ghost : this.ghosts) {
                ghost.draw(g2d);
            }

            // Print the current score.
            this.printScore(g2d);

            if (this.won) {
                // Players have won the game.
                // Stop the ghosts sound effect and start the victory sound effect.
                this.movingGhosts.stop();
                this.win.start();
                this.printWin(g2d);
            } else if (this.defeated) {
                // Players have lost the game.
                // Stop the ghosts sound effect and start the die sound effect.
                this.movingGhosts.stop();
                this.die.start();
                this.printGameOver(g2d);
            }
        } else {
            // Print the game intro screen.
            this.printIntro(g2d);
        }
    }

    /**
     * The method to run the game flow (when the thread is called).
     */
    @Override
    public void run() {
        while (!this.won && !this.defeated) {
            // Move the ghosts randomly.
            for (int i = 0; i < 4; i++) {
                this.ghosts[i].randomizedMove();
            }

            // Check if a big dot has been eaten by Pac-Man.
            this.bigDotEaten();
            // Check if players have won the game.
            this.won();
            // Check if players have lost the game.
            this.defeated();

            // Check if a dot is eaten by Pac-Man.
            // If a dot is eaten, current score + 100 and the dot will disappear.
            if (this.board.getDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                this.score += 100;
                this.board.setDotToFalse(this.pacMan.getX(), this.pacMan.getY());
                this.pacMan.setEating(true);
            }

            // Check if a big dot is eaten by Pac-Man.
            // If a big dot is eaten, current socre + 200, all ghosts will become blue and the big dot will disappear.
            if (this.board.getBigDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                this.score += 200;
                this.board.setBigDotToFalse(this.pacMan.getX(), this.pacMan.getY());
                this.pacMan.setEating(true);
            }

            // Check if one of the blue ghosts is eaten by Pac-Man.
            // If a blue ghost is eaten, current socre + 500 and the ghost will disappear temporarily.
            for (int i = 0; i < 4; i++) {
                if (this.ghosts[i].isBigDotEaten()) {
                    if (this.ghosts[i].getX() == this.pacMan.getX()
                            && this.ghosts[i].getY() == this.pacMan.getY()) {
                        this.score += 500;
                        this.ghosts[i].setEaten(true);
                        this.pacMan.setEating(true);
                    }
                } else {
                    this.bigDotEaten = false;
                }
            }
            repaint();

            try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * The method to handle key event.
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (this.start) {
            if (this.won || this.defeated) {
                if (e.getKeyCode() == 32) {
                    // If the players press the space key.
                    this.init();
                    this.intro.stop();
                    this.movingGhosts.loop(Clip.LOOP_CONTINUOUSLY);
                    repaint();
                } else if (e.getKeyCode() == 27) {
                    // If the players press the ESC key.
                    System.exit(1);
                }
            } else {
                try {
                    this.wakaAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Waka Waka.wav").getAbsoluteFile());
                    this.wakaWaka = AudioSystem.getClip();
                    this.wakaWaka.open(this.wakaAudioInputStream);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                }
                
                // If the players press right arrow key (→).
                if (e.getKeyCode() == 39) {
                    this.pacMan.move(1);
                    if (this.board.getDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                        this.wakaWaka.start();
                    }
                }
                
                // If the players press left arrow key (←).
                if (e.getKeyCode() == 37) {
                    this.pacMan.move(3);
                    if (this.board.getDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                        this.wakaWaka.start();
                    }
                }
                
                // If the players press up arrow key (↑).
                if (e.getKeyCode() == 38) {
                    this.pacMan.move(0);
                    if (this.board.getDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                        this.wakaWaka.start();
                    }
                }
                
                // If the players press down arrow key (↓).
                if (e.getKeyCode() == 40) {
                    this.pacMan.move(2);
                    if (this.board.getDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                        this.wakaWaka.start();
                    }
                }
            }
        } else {
            if (e.getKeyCode() == 32) {
                // If the players press the space key.
                this.start = true;
                this.intro.stop();
                this.movingGhosts.loop(Clip.LOOP_CONTINUOUSLY);
            } else if (e.getKeyCode() == 27) {
                // If the players press the ESC key.
                System.exit(1);
            }
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }
}
