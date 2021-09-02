
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

/*
 * TUGAS AKHIR GRAFIKA KOMPUTER
 */
/**
 * Kelas Game merupakan kelas yang merepresentasikan permainan dari PacMan.
 * Dalam kelas ini, terdapat sebelas atribut utama. Atribut n menyatakan jumlah
 * kotak dalam board. Atribut tileLength menyatakan panjang kotak dalam board.
 * Atribut totalLength menyatakan panjang total board. Atribut start menyatakan
 * permaianan sudah dimulai atau belum. Atribut board menyatakan papan
 * permainan. Atribut pacMan menyatakan PacMan. Atribut ghosts menyatakan empat
 * hantu. Atribut score menyatakan skor yang telah didapatkan. Atribut
 * bigDotEaten menyatakan apakah big dot telah dimakan oleh PacMan. Atribut
 * defeated menyatakan PacMan sudah dikalahkan atau belum. Atribut won
 * menyatakan apakah permainan telah dimenangkan. Enam atribut lainnya merupakan
 * atribut yang digunakan untuk keperluan suara. Suara tersebut adalah suara
 * intro (suara awal permainan), waka (suara PacMan memakan dot), movingGhosts
 * (suara pergerakan hantu), scaredGhosts (suara hantu ketika PacMan memakan big
 * dot), die (suara ketika PacMan telah mati) dan win (suara ketika permainan
 * dimenangkan).
 *
 * @author williamo1099 (William Oktavianus | 2017730010)
 */
public class Game extends JPanel implements Runnable, KeyListener {

    private final int n, tileLength, totalLength;
    private boolean start = false;
    private Board board;
    private PacMan pacMan;
    private Ghost[] ghosts;
    private int score;
    private boolean bigDotEaten, defeated, won;
    // atribut suara
    private AudioInputStream introAudioInputStream, wakaAudioInputStream, movingGhostsAudioInputStream, scaredGhostsAudioInputStream, dieAudioInputStream, winAudioInputStream;
    private Clip intro, wakaWaka, movingGhosts, scaredGhosts, die, win;

    public Game(int n, int tileLength) {
        this.addKeyListener(this);
        this.n = n;
        this.tileLength = tileLength;
        this.totalLength = this.n * this.tileLength;
        this.init();
    }

    /**
     * Melakukan inisialisasi atribut-atribut dalam game PacMan (dipisahkan dari
     * constructor dikarenakan pemain dimungkinkan untuk bermain game ulang
     * setelah kalah atau menang)
     */
    public void init() {
        this.board = new Board(this.n, this.tileLength);
        this.pacMan = new PacMan(this.n, this.tileLength, this.n - 1, 0);
        this.board.setDotToFalse(this.n - 1, 0); // supaya ga bentrok ama PacMan
        this.board.setBigDotToFalse(this.n - 1, 0); // supaya ga bentrok ama PacMan
        this.board.setMazeToFalse(this.n - 1, 0); // supaya ga bentrok ama PacMan
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

        // suara
        try {
            // suara intro
            this.introAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Intro.wav").getAbsoluteFile());
            this.intro = AudioSystem.getClip();
            this.intro.open(this.introAudioInputStream);
            this.intro.loop(Clip.LOOP_CONTINUOUSLY);
            this.intro.start();

            // suara hantu
            this.movingGhostsAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Moving Ghosts.wav").getAbsoluteFile());
            this.movingGhosts = AudioSystem.getClip();
            this.movingGhosts.open(this.movingGhostsAudioInputStream);

            // suara hantu (big dot dimakan)
            this.scaredGhostsAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Scared Ghosts.wav").getAbsoluteFile());
            this.scaredGhosts = AudioSystem.getClip();
            this.scaredGhosts.open(this.scaredGhostsAudioInputStream);

            // suara pacman mati
            this.dieAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Die.wav").getAbsoluteFile());
            this.die = AudioSystem.getClip();
            this.die.open(this.dieAudioInputStream);

            // suara menang
            this.winAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Victory.wav").getAbsoluteFile());
            this.win = AudioSystem.getClip();
            this.win.open(this.winAudioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Mengecek apakah PacMan berhasil memakan big dot
     *
     * @return true jika PacMan berhasil memakan big dot atau false jika PacMan
     * belum berhasil memakan big dot
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
     * Mengecek apakah PacMan telah dimakan oleh hantu
     *
     * @return true jika PacMan telah dimakan atau false jika PacMan belum
     * dimakan
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
     * Mengecek apakah PacMan telah berhasil memakan semua dot
     *
     * @return true jika PacMan berhasil memakan semua dot atau false jika
     * PacMan belum berhasil memakan semua dot
     */
    private boolean won() {
        if (this.board.finished()) {
            this.won = true;
            return true;
        }
        return false;
    }

    /**
     * Mencetak layar intro dari game
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
     * Mencetak score (skor terbaru) di layar
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
     * Mencetak layar game over dan skor yang berhasil diraih saat PacMan telah
     * dimakan oleh hantu (defeated == true)
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
     * Mencetak layar win saat PacMan berhasil memakan semua dot dalam board
     * (won == true)
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
     * Menggambar permainan PacMan
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setBackground(Color.BLACK);

        if (this.start) {
            // menggambar board (papan permainan)
            this.board.draw(g2d);
            // menggambar pacman dalam board
            this.pacMan.draw(g2d);
            // menggambar hantu-hantu dalam board
            for (Ghost ghost : this.ghosts) {
                ghost.draw(g2d);
            }

            // mencetak atribut score
            this.printScore(g2d);

            if (this.won) {
                this.movingGhosts.stop(); // suara hantu bergerak berhenti
                this.win.start(); // suara win dimulai
                this.printWin(g2d);
            } else if (this.defeated) {
                this.movingGhosts.stop(); // suara hantu bergerak berhenti
                this.die.start(); // suara die dimulai
                this.printGameOver(g2d);
            }
        } else {
            // mencetak intro dari game
            this.printIntro(g2d);
        }
    }

    /**
     * Aktivitas yang dilakukan ketika thread dipanggil
     */
    @Override
    public void run() {
        while (!this.won && !this.defeated) {
            // gerak hantu-hantu secara acak
            for (int i = 0; i < 4; i++) {
                this.ghosts[i].randomizedMove();
            }

            this.bigDotEaten(); // mengecek apakah big dot telah dimakan
            this.won(); // mengecek apakah permainan telah dimenangkan
            this.defeated(); // mengecek apakah PacMan telah dikalahkan

            // melihat apakah dot telah dimakan oleh PacMan (jika dimakan, score + 100 dan dot hilang)
            if (this.board.getDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                this.score += 100;
                this.board.setDotToFalse(this.pacMan.getX(), this.pacMan.getY());
                this.pacMan.setEating(true); // biar mulut PacMan bisa gerak
            }

            // melihat apakah big dot telah dimakan oleh PacMan (jika dimakan, score + 100, hantu berubah menjadi biru dan big dot hilang)
            if (this.board.getBigDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                this.score += 200;
                this.board.setBigDotToFalse(this.pacMan.getX(), this.pacMan.getY());
                this.pacMan.setEating(true); // biar mulut PacMan bisa gerak
            }

            // melihat apakah hantu biru berhasil dimakan oleh PacMan (jika dimakan, score + 500 dan hantu hilang sementara)
            for (int i = 0; i < 4; i++) {
                if (this.ghosts[i].isBigDotEaten()) {
                    if (this.ghosts[i].getX() == this.pacMan.getX()
                            && this.ghosts[i].getY() == this.pacMan.getY()) {
                        this.score += 500;
                        this.ghosts[i].setEaten(true);
                        this.pacMan.setEating(true); // biar mulut PacMan bisa gerak
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
     * Mengatur kegiatan yang dilakukan ketika suatu key yang ditekan
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println(e);
        if (this.start) {
            if (this.won || this.defeated) {
                // kalau udh menang atau kalah
                if (e.getKeyCode() == 32) { // menekan key space
                    this.init();
                    this.intro.stop();
                    this.movingGhosts.loop(Clip.LOOP_CONTINUOUSLY);
                    repaint();
                } else if (e.getKeyCode() == 27) { // menekan esc
                    System.exit(1); // keluar dari aplikasi
                }
            } else {
                try {
                    this.wakaAudioInputStream = AudioSystem.getAudioInputStream(new File("resources/sound/Waka Waka.wav").getAbsoluteFile());
                    this.wakaWaka = AudioSystem.getClip();
                    this.wakaWaka.open(this.wakaAudioInputStream);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                }

                // menekan key kanan (→)
                if (e.getKeyCode() == 39) {
                    this.pacMan.move(1);
                    if (this.board.getDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                        this.wakaWaka.start();
                    }
                }
                // menekan key kiri (←)
                if (e.getKeyCode() == 37) {
                    this.pacMan.move(3);
                    if (this.board.getDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                        this.wakaWaka.start();
                    }
                }
                // menekan key atas (↑)
                if (e.getKeyCode() == 38) {
                    this.pacMan.move(0);
                    if (this.board.getDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                        this.wakaWaka.start();
                    }
                }
                // menekan key bawah (↓)
                if (e.getKeyCode() == 40) {
                    this.pacMan.move(2);
                    if (this.board.getDot()[this.pacMan.getX()][this.pacMan.getY()]) {
                        this.wakaWaka.start();
                    }
                }
            }
        } else {
            if (e.getKeyCode() == 32) { // menekan key space
                this.start = true;
                this.intro.stop();
                this.movingGhosts.loop(Clip.LOOP_CONTINUOUSLY);
            } else if (e.getKeyCode() == 27) { // menekan esc
                System.exit(1); // keluar dari aplikasi
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
