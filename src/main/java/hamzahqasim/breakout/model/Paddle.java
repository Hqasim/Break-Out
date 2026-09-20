package hamzahqasim.breakout.model;

import java.awt.Rectangle;

/**
 * The player-controlled paddle. Moves horizontally along a fixed Y coordinate
 * and is clamped so it can never leave the board.
 */
public final class Paddle {

    private final int width;
    private final int height;
    private final int y;
    private final int boardWidth;
    private final int step;
    private int x;

    /**
     * @param x          initial top-left X coordinate
     * @param y          fixed top-left Y coordinate
     * @param width      paddle width in pixels
     * @param height     paddle height in pixels
     * @param boardWidth width of the playable board, used to clamp movement
     * @param step       pixels moved per {@link #moveLeft()} / {@link #moveRight()} call
     */
    public Paddle(int x, int y, int width, int height, int boardWidth, int step) {
        this.y = y;
        this.width = width;
        this.height = height;
        this.boardWidth = boardWidth;
        this.step = step;
        this.x = clamp(x);
    }

    /** Moves the paddle left by one step, stopping at the left edge of the board. */
    public void moveLeft() {
        x = clamp(x - step);
    }

    /** Moves the paddle right by one step, stopping at the right edge of the board. */
    public void moveRight() {
        x = clamp(x + step);
    }

    /** Resets the paddle to the given X coordinate (clamped to the board). */
    public void resetTo(int x) {
        this.x = clamp(x);
    }

    private int clamp(int candidateX) {
        int maxX = boardWidth - width;
        if (candidateX < 0) {
            return 0;
        }
        if (candidateX > maxX) {
            return maxX;
        }
        return candidateX;
    }

    /** @return the paddle's current bounding box, used for collision checks and rendering */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
