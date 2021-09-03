
import java.awt.Dimension;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 * Tester
 * The main class to run the game.
 *
 * @author William Oktavianus (williamo1099)
 */
public class Tester {

    public static void main(String[] args) {
        // Get input n and tile length.
        Scanner scan = new Scanner(System.in);
        System.out.print("Input n : ");
        int n = scan.nextInt();
        System.out.print("Input tile length : ");
        int tileLength = scan.nextInt();
        int frameSize = n * tileLength;
        
        // Set game frame.
        JFrame f = new JFrame("Pac-Man");
        Game board = new Game(n, tileLength);
        board.setFocusable(true);
        f.getContentPane().add("Center", board);
        f.pack();
        f.setSize(new Dimension(frameSize, frameSize + 70));
        f.setVisible(true);
    }
}
