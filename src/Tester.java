
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Tester
 * The main class to run the game.
 *
 * @author William Oktavianus (williamo1099)
 */
public class Tester {

    public static void main(String[] args) {
        JFrame f = new JFrame("Pac-Man");
        Game board = new Game(11, 50); // Currently, board size is fixed 11x11.
        board.setFocusable(true);
        f.getContentPane().add("Center", board);
        f.pack();
        f.setSize(new Dimension(620, 620));
        f.setVisible(true);
    }
}
