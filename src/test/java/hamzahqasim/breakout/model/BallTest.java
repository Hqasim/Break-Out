package hamzahqasim.breakout.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BallTest {

    private static final double DELTA = 1e-9;

    @Test
    void moveAdvancesPositionByCurrentDirection() {
        Ball ball = new Ball(100, 100, 1, 0, 5);

        ball.move();

        assertEquals(105, ball.getX(), DELTA);
        assertEquals(100, ball.getY(), DELTA);
    }

    @Test
    void directionIsNormalizedToConfiguredSpeed() {
        Ball ball = new Ball(0, 0, 3, 4, 10); // 3-4-5 triangle: magnitude 5, scaled to speed 10

        ball.move();

        assertEquals(6, ball.getX(), DELTA); // 10 * (3/5)
        assertEquals(8, ball.getY(), DELTA); // 10 * (4/5)
    }

    @Test
    void reverseXFlipsOnlyHorizontalDirection() {
        Ball ball = new Ball(0, 0, 1, 1, Math.sqrt(2));

        ball.reverseX();
        ball.move();

        assertEquals(-1, ball.getX(), DELTA);
        assertEquals(1, ball.getY(), DELTA);
    }

    @Test
    void reverseYFlipsOnlyVerticalDirection() {
        Ball ball = new Ball(0, 0, 1, 1, Math.sqrt(2));

        ball.reverseY();
        ball.move();

        assertEquals(1, ball.getX(), DELTA);
        assertEquals(-1, ball.getY(), DELTA);
    }

    @Test
    void boundsMatchPositionAndFixedSize() {
        Ball ball = new Ball(12, 34, 0, 0, 1);

        var bounds = ball.getBounds();

        assertEquals(12, bounds.x);
        assertEquals(34, bounds.y);
        assertEquals(Ball.SIZE, bounds.width);
        assertEquals(Ball.SIZE, bounds.height);
    }
}
