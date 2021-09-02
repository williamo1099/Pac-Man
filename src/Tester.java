
import java.awt.Dimension;
import javax.swing.JFrame;

/*
 * TTUGAS AKHIR GRAFIKA KOMPUTER
 */
/**
 * Kelas Tester merupakan kelas yang digunakan untuk uji coba.
 *
 * @author williamo1099 (William Oktavianus | 2017730010)
 */
public class Tester {

    public static void main(String[] args) {
        JFrame f = new JFrame("PacMan");
        Game board = new Game(11, 50); // saat ini dibuat fix 11x11 dulu (masalah maze)
        board.setFocusable(true);
        f.getContentPane().add("Center", board);
        f.pack();
        f.setSize(new Dimension(620, 620));
        f.setVisible(true);
    }
}
