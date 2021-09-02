
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import javax.sound.sampled.Clip;

/*
 * TUGAS AKHIR GRAFIKA KOMPUTER
 */
/**
 * Kelas Ghost merupakan kelas yang merepresentasikan hantu dalam permainan.
 * Dalam kelas ini, terdapat lima atribut utama. Atribut x menyatakan posisi
 * koordinat x dari hantu. Atribut y menyatakan posisi koordinat y dari hantu.
 * Atribut color menyatakan warna dari hantu. Atribut bigDotEaten menyatakan
 * apakah big dot telah termakan oleh PacMan. Atribut eaten menyatakan apakah
 * hantu telah termakan oleh PacMan (jika bigDotEaten == true). Atribut maze
 * menyatakan posisi dinding dalam papan permainan. Tiga atribut lainnya adalah
 * n (jumlah kotak dalam papan), tileLength (panjang kotak) dan totalLength
 * (panjang total papan).
 *
 * @author williamo1099 (William Oktavianus | 2017730010)
 */
public class Ghost {

    private final int n, tileLength, totalLength;
    private int x, y;
    private Color color;
    private boolean bigDotEaten, eaten;
    private boolean[][] maze;

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
     * Mendapatkan atribut warna dari hantu
     *
     * @return warna
     */
    public Color getColor() {
        return color;
    }

    /**
     * Mendapatkan atribut bigDotEaten
     *
     * @return true jika big dot telah dimakan atau false jika big dot belum
     * dimakan
     */
    public boolean isBigDotEaten() {
        return bigDotEaten;
    }

    /**
     * Mengubah atribut color dengan input color
     *
     * @param color warna
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Mengubah atribut bigDotEaten dengan input bigDotEaten, dan akan berubah
     * lagi menjadi false selama 5 detik
     *
     * @param bigDotEaten
     * @param sound
     */
    public void setBigDotEaten(boolean bigDotEaten, Clip sound) {
        this.bigDotEaten = bigDotEaten;
        // timer selama 5 s (5000 ms) sampai ghost kembali bisa memakan pacman
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                setBigDotEatenToFalse();
                sound.stop();
            }
        }, 5000);
    }

    /**
     * Mengubah atribut bigDotEaten dengan nilai false
     */
    public void setBigDotEatenToFalse() {
        this.bigDotEaten = false;
        this.eaten = false;
    }

    /**
     * Mengubah atribut eaten dengan atribut eaten
     *
     * @param eaten
     */
    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }

    /**
     * Mengubah atribut maze dengan input maze
     *
     * @param maze
     */
    public void setMaze(boolean[][] maze) {
        this.maze = maze;
    }

    /**
     * Menggambar bentuk hantu
     *
     * @param g2d
     * @param x posisi koordinat x dari hantu dalam board
     * @param y posisi koordinat y dari hantu dalam board
     * @param width lebar dari PacMan
     * @param height tinggi dari PacMan
     */
    private void drawGhost(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(this.color);
        // g2d.fillRect(x, y, width, height);
        GeneralPath shape = new GeneralPath();
        shape.moveTo(x, y + height);
        shape.quadTo(x + (width / 2), y - height, x + width, y + height);
        g2d.fill(shape);
    }

    /**
     * Menggambar bentuk hantu ketika big dot berhasil dimakan oleh PacMan
     *
     * @param g2d
     * @param x posisi koordinat x dari hantu dalam board
     * @param y posisi koordinat y dari hantu dalam board
     * @param width lebar dari PacMan
     * @param height tinggi dari PacMan
     */
    private void drawBlueGhost(Graphics2D g2d, int x, int y, int width, int height) {
        if (!this.eaten) {
            g2d.setColor(new Color(16, 88, 251));
            // g2d.fillRect(x, y, width, height);
            GeneralPath shape = new GeneralPath();
            shape.moveTo(x, y + height);
            shape.quadTo(x + (width / 2), y - height, x + width, y + height);
            g2d.fill(shape);
        }
    }

    /**
     * Menggambar hantu
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
     * Melakukan gerak (acak) dari hantu
     */
    protected void randomizedMove() {
        double move = Math.random();
        if (move >= 0 && move < 0.25) {
            // atas
            if (this.y > 0 && !this.maze[this.x][this.y - 1]) {
                this.y = this.y - 1;
            }

        } else if (move >= 0.25 && move < 0.5) {
            // kanan
            if (this.x < this.n - 1 && !this.maze[this.x + 1][this.y]) {
                this.x = this.x + 1;
            }

        } else if (move >= 0.5 && move < 0.75) {
            // bawah
            if (this.y < this.n - 1 && !this.maze[this.x][this.y + 1]) {
                this.y = this.y + 1;
            }

        } else {
            // kiri
            if (this.x > 0 && !this.maze[this.x - 1][this.y]) {
                this.x = this.x - 1;
            }

        }
    }
}
