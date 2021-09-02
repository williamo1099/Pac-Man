
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/*
 * TUGAS AKHIR GRAFIKA KOMPUTER
 */
/**
 * Kelas PacMan merupakan kelas yang merepresentasikan PacMan dalam permainan.
 * Dalam kelas ini, terdapat tiga atribut utama. Atribut x menyatakan posisi
 * koordinat x dari PacMan. Atribut y menyatakan posisi koordinat y dari PacMan.
 * Atribut direction menyatakan kode arah dari gerakan PacMan (kiri, atas, kanan
 * atau bawah). Atribut eating menyatakan apakah PacMan sedang dalam kondisi
 * memakan. Atribut defeated menyatakan apakah PacMan telah dimakan oleh hantu.
 * Atribut maze menyatakan posisi dinding dalam papan permainan. Tiga atribut
 * lainnya adalah n (jumlah kotak dalam papan), tileLength (panjang kotak) dan
 * totalLength (panjang total papan).
 *
 * @author williamo1099 (William Oktavianus | 2017730010)
 */
public class PacMan {

    private final int n, tileLength, totalLength;
    protected int x, y, direction;
    protected boolean eating, defeated;
    protected boolean[][] maze;

    public PacMan(int n, int tileLength, int x, int y) {
        this.n = n;
        this.tileLength = tileLength;
        this.totalLength = this.n * this.tileLength;
        this.x = x;
        this.y = y;
        this.direction = 3; // kiri (default)
        this.eating = false;
        this.defeated = false;
    }

    /**
     * Mendapatkan posisi koordinat x
     *
     * @return posisi koordinat x
     */
    public int getX() {
        return x;
    }

    /**
     * Mengubah atribut x dengan input x
     *
     * @param x posisi koordinat x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Mendapatkan posisi koordinat y
     *
     * @return posisi koordinat y
     */
    public int getY() {
        return y;
    }

    /**
     * Mengubah atribut y dengan input y
     *
     * @param y posisi koordinat y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Mendapatkan arah dari PacMan
     *
     * @return kode arah (0: kiri, 1: atas, 2: kanan, 3: bawah)
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Mengubah atribut direction dengan input direction
     *
     * @param direction kode arah (0: kiri, 1: atas, 2: kanan, 3: bawah)
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Mengubah atribut eating dengan input eating
     *
     * @param eating true jika PacMan sedang makan atau false jika PacMan tidak
     * sedang makan
     */
    public void setEating(boolean eating) {
        this.eating = eating;
    }

    /**
     * Mengubah atribut defeated dengan input defeated
     *
     * @param defeated true jika PacMan telah dimakan oleh hantu atau false jika
     * PacMan belum dimakan oleh hantu
     */
    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    /**
     * Mengubah atribut maze dengan input maze
     *
     * @param maze posisi dinding dalam papan permainan
     */
    public void setMaze(boolean[][] maze) {
        this.maze = maze;
    }

    /**
     * Menggambar bentuk PacMan (warna kuning)
     *
     * @param g2d
     * @param x posisi koordinat x dari PacMan dalam board
     * @param y posisi koordinat y dari PacMan dalam board
     * @param width lebar dari PacMan
     * @param height tinggi dari PacMan
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
                // atas
                transform.rotate(Math.toRadians(90));
                break;
            case 1:
                // kanan
                transform.rotate(Math.toRadians(170));
                break;
            case 2:
                // bawah
                transform.rotate(Math.toRadians(-100));
                break;
            default:
                // kiri
                transform.rotate(Math.toRadians(0));
                break;
        }
        transform.translate(-xc, -yc);
        Shape pacMan = transform.createTransformedShape(body);
        g2d.fill(pacMan);
    }

    /**
     * Menggambar bentuk PacMan (warna kuning) saat sedang memakan
     *
     * @param g2d
     * @param x posisi koordinat x dari PacMan dalam board
     * @param y posisi koordinat y dari PacMan dalam board
     * @param width lebar dari PacMan
     * @param height tinggi dari PacMan
     */
    private void drawPacManEating(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(new Color(255, 253, 56));
        Area body = new Area(new Ellipse2D.Double(x, y, width, height));
        g2d.fill(body);
    }

    /**
     * Menghilangkan PacMan (mengubah warna PacMan menjadi hitam)
     *
     * @param g2d
     * @param x posisi koordinat x dari PacMan dalam board
     * @param y posisi koordinat y dari PacMan dalam board
     * @param width lebar dari PacMan
     * @param height tinggi dari PacMan
     */
    private void removePacMan(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(Color.BLACK);
        Area body = new Area(new Ellipse2D.Double(x, y, width, height));
        g2d.fill(body);
    }

    /**
     * Menggambar PacMan
     *
     * @param g2d
     */
    protected void draw(Graphics2D g2d) {
        if (!this.defeated) { // kalau belum kalah
            if (this.eating) { // kalau lagi makan
                this.drawPacManEating(g2d, (this.x * this.tileLength) + this.tileLength / 4,
                        (this.y * this.tileLength) + this.tileLength / 4,
                        this.tileLength / 2,
                        this.tileLength / 2);

                // timer selama 10 ms supaya mulut PacMan terkesan bergerak saat memakan
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        setEating(false);
                    }
                }, 10);
            } else { // kalau lagi ga makan
                this.drawPacMan(g2d, (this.x * this.tileLength) + this.tileLength / 4,
                        (this.y * this.tileLength) + this.tileLength / 4,
                        this.tileLength / 2,
                        this.tileLength / 2);
            }
        } else { // kalau udah kalah
            // sembunyi PacMan
            this.removePacMan(g2d, (this.x * this.tileLength) + this.tileLength / 4,
                    (this.y * this.tileLength) + this.tileLength / 4,
                    this.tileLength / 2,
                    this.tileLength / 2);
        }
    }

    /**
     * Melakukan pergerakan sesuai direction
     *
     * @param direction kode arah gerak
     */
    protected void move(int direction) {
        switch (direction) {
            case 0:
                // atas
                if (this.y > 0 && !this.maze[this.x][this.y - 1]) {
                    this.y = this.y - 1;
                    this.direction = 0;
                }
                break;
            case 1:
                // kanan
                if (this.x < this.n - 1 && !this.maze[this.x + 1][this.y]) {
                    this.x = this.x + 1;
                    this.direction = 1;
                }
                break;
            case 2:
                // bawah
                if (this.y < this.n - 1 && !this.maze[this.x][this.y + 1]) {
                    this.y = this.y + 1;
                    this.direction = 2;
                }
                break;
            default:
                // kiri
                if (this.x > 0 && !this.maze[this.x - 1][this.y]) {
                    this.x = this.x - 1;
                    this.direction = 3;
                }
                break;
        }
    }
}
