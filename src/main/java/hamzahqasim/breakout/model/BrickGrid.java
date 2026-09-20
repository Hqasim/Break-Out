package hamzahqasim.breakout.model;

import java.awt.Rectangle;

/**
 * The grid of bricks the ball destroys. Tracks which bricks are still active
 * and knows how to compute each brick's on-screen bounds; it has no rendering
 * logic of its own so it can be unit tested without a display.
 */
public final class BrickGrid {

    private final int rows;
    private final int columns;
    private final int brickWidth;
    private final int brickHeight;
    private final int offsetX;
    private final int offsetY;
    private boolean[][] active;
    private int remaining;

    /**
     * @param rows        number of brick rows
     * @param columns     number of brick columns
     * @param brickWidth  width of a single brick in pixels
     * @param brickHeight height of a single brick in pixels
     * @param offsetX     X coordinate where the grid starts on the board
     * @param offsetY     Y coordinate where the grid starts on the board
     */
    public BrickGrid(int rows, int columns, int brickWidth, int brickHeight, int offsetX, int offsetY) {
        this.rows = rows;
        this.columns = columns;
        this.brickWidth = brickWidth;
        this.brickHeight = brickHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        reset();
    }

    /** Restores every brick to the active state, ready for a new game. */
    public void reset() {
        active = new boolean[rows][columns];
        for (boolean[] row : active) {
            java.util.Arrays.fill(row, true);
        }
        remaining = rows * columns;
    }

    /** @return true if the brick at (row, col) is still active */
    public boolean isActive(int row, int col) {
        return active[row][col];
    }

    /**
     * Destroys the brick at (row, col), if it was still active.
     *
     * @return true if a brick was actually destroyed by this call
     */
    public boolean destroy(int row, int col) {
        if (!active[row][col]) {
            return false;
        }
        active[row][col] = false;
        remaining--;
        return true;
    }

    /** @return the on-screen bounding box of the brick at (row, col) */
    public Rectangle getBounds(int row, int col) {
        return new Rectangle(
                offsetX + col * brickWidth,
                offsetY + row * brickHeight,
                brickWidth,
                brickHeight);
    }

    /** @return true once every brick has been destroyed */
    public boolean isCleared() {
        return remaining <= 0;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getRemaining() {
        return remaining;
    }
}
