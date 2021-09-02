
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * TUGAS AKHIR GRAFIKA KOMPUTER
 */
/**
 * Kelas board merupakan kelas yang merepresentasikan papan permainan dalam
 * permainan. Dalam kelas ini, terdapat lima atribut. Atribut n menyatakan
 * jumlah kotak dalam board. Atribut tileLength menyatakan panjang kotak dalam
 * board. Atribut totalLength menyatakan panjang total dari board. Atribut dot
 * menyatakan posisi dot dalam papan permainan. Atribut big dot menyatakan
 * posisi big dot dalam papan permainan. Atribut maze menyatakan posisi dinding
 * dalam papan permainan.
 *
 * @author williamo1099 (William Oktavianus | 2017730010)
 */
public class Board {

    private final int n, tileLength, totalLength;
    private boolean[][] dot, bigDot, maze;

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
            File file = new File("maze_1 (11 x 11).txt");
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
     * Mendapatkan posisi dot dalam permainan
     *
     * @return dot
     */
    public boolean[][] getDot() {
        return dot;
    }

    /**
     *
     * @return
     */
    public boolean[][] getMaze() {
        return maze;
    }

    /**
     * Mengubah atribut dot[i][j] menjadi false
     *
     * @param i posisi indeks i
     * @param j posisi indeks j
     */
    public void setDotToFalse(int i, int j) {
        this.dot[i][j] = false;
    }

    /**
     * Mendapatkan posisi dot dalam permainan
     *
     * @return dot
     */
    public boolean[][] getBigDot() {
        return bigDot;
    }

    /**
     * Mengubah atribut dot[i][j] menjadi false
     *
     * @param i posisi indeks i
     * @param j posisi indeks j
     */
    public void setBigDotToFalse(int i, int j) {
        this.bigDot[i][j] = false;
    }

    public void setMazeToFalse(int i, int j) {
        this.maze[i][j] = false;
    }

    /**
     * Menggambar garis batas dalam board
     *
     * @param g2d
     * @param i posisi indeks i
     */
    private void drawBorder(Graphics2D g2d, int i) {
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(0, i * this.tileLength, this.totalLength, i * this.tileLength);
        g2d.drawLine(i * this.tileLength, 0, i * this.tileLength, this.totalLength);
    }

    /**
     * Menggambar dot dalam board
     *
     * @param g2d
     * @param i posisi indeks i
     * @param j posisi indeks j
     */
    private void drawDot(Graphics2D g2d, int i, int j) {
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect((i * this.tileLength) + this.tileLength / 2,
                (j * this.tileLength) + this.tileLength / 2,
                this.tileLength / 4,
                this.tileLength / 4, 50, 50);
    }

    /**
     * Menggambar big dot dalam board
     *
     * @param g2d
     * @param i posisi indeks i
     * @param j posisi indeks j
     */
    private void drawBigDot(Graphics2D g2d, int i, int j) {
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect((i * this.tileLength) + this.tileLength / 4,
                (j * this.tileLength) + this.tileLength / 4,
                this.tileLength / 2,
                this.tileLength / 2, 50, 50);
    }

    /**
     * Menggambarkan dinding dalam board
     * 
     * @param g2d
     * @param i posisi indeks i
     * @param j posisi indeks j
     */
    private void drawMaze(Graphics2D g2d, int i, int j) {
        g2d.setColor(new Color(65, 84, 179));
        g2d.fillRect(i * this.tileLength, j * this.tileLength, this.tileLength, this.tileLength);
    }

    /**
     * Menggambar board secara keseluruhan
     *
     * @param g2d
     */
    protected void draw(Graphics2D g2d) {
        // menggambarkan border
        for (int i = 0; i < this.n; i++) {
            this.drawBorder(g2d, i);
        }

        // menggambarkan dot
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.dot[i][j]) {
                    this.drawDot(g2d, i, j);
                }
            }
        }

        // menggambarkan big dot
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.bigDot[i][j]) {
                    this.drawBigDot(g2d, i, j);
                }
            }
        }

        // menggambarkan dinding
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.maze[i][j]) {
                    this.drawMaze(g2d, i, j);
                }
            }
        }
    }

    /**
     * Mengecek apakah permainan telah selesai (ketika semua dot telah
     * dikunjungi oleh PacMan)
     *
     * @return true jika permainan telah selesai atau false jika permainan belum
     * selesai
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
