package hamzahqasim.breakout.model;

import java.awt.Rectangle;

/**
 * The ball the player bounces around the board.
 *
 * <p>Holds only position, size and direction — it knows nothing about the board,
 * the paddle or the bricks. Collision resolution lives in the engine so this class
 * stays trivial to unit test.
 */
public final class Ball {

    /** Diameter of the ball in pixels. */
    public static final int SIZE = 20;

    private double x;
    private double y;
    private double directionX;
    private double directionY;
    private final double speed;

    /**
     * Creates a ball at the given position, already moving in the given direction.
     *
     * @param x          initial top-left X coordinate
     * @param y          initial top-left Y coordinate
     * @param directionX initial horizontal direction (sign matters, magnitude is normalized by speed)
     * @param directionY initial vertical direction (sign matters, magnitude is normalized by speed)
     * @param speed      distance travelled per {@link #move()} call, in pixels
     */
    public Ball(double x, double y, double directionX, double directionY, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        setDirection(directionX, directionY);
    }

    /** Advances the ball by one step along its current direction. */
    public void move() {
        x += directionX;
        y += directionY;
    }

    /**
     * Sets the direction of travel. The vector is normalized so the ball always
     * travels at {@link #speed} pixels per {@link #move()} call, regardless of the
     * magnitude of the vector passed in.
     *
     * @param dx horizontal direction component
     * @param dy vertical direction component
     */
    public void setDirection(double dx, double dy) {
        double magnitude = Math.hypot(dx, dy);
        if (magnitude == 0) {
            this.directionX = 0;
            this.directionY = 0;
            return;
        }
        this.directionX = (dx / magnitude) * speed;
        this.directionY = (dy / magnitude) * speed;
    }

    /** Reflects the ball's horizontal direction, as if it bounced off a vertical wall. */
    public void reverseX() {
        directionX = -directionX;
    }

    /** Reflects the ball's vertical direction, as if it bounced off a horizontal wall. */
    public void reverseY() {
        directionY = -directionY;
    }

    /** @return the ball's current bounding box, used for collision checks and rendering */
    public Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), SIZE, SIZE);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDirectionX() {
        return directionX;
    }

    public double getDirectionY() {
        return directionY;
    }
}
