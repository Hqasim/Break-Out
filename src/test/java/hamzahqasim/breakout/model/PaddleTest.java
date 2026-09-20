package hamzahqasim.breakout.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaddleTest {

    @Test
    void moveRightAdvancesByStep() {
        Paddle paddle = new Paddle(100, 600, 100, 8, 1000, 20);

        paddle.moveRight();

        assertEquals(120, paddle.getX());
    }

    @Test
    void moveLeftAdvancesByStep() {
        Paddle paddle = new Paddle(100, 600, 100, 8, 1000, 20);

        paddle.moveLeft();

        assertEquals(80, paddle.getX());
    }

    @Test
    void moveRightClampsAtRightEdgeOfBoard() {
        Paddle paddle = new Paddle(920, 600, 100, 8, 1000, 20); // already at max X (1000 - 100 = 900)... start near edge

        paddle.moveRight();
        paddle.moveRight();

        assertEquals(900, paddle.getX()); // board width - paddle width
    }

    @Test
    void moveLeftClampsAtLeftEdgeOfBoard() {
        Paddle paddle = new Paddle(10, 600, 100, 8, 1000, 20);

        paddle.moveLeft();
        paddle.moveLeft();

        assertEquals(0, paddle.getX());
    }

    @Test
    void resetToClampsOutOfBoundsPosition() {
        Paddle paddle = new Paddle(100, 600, 100, 8, 1000, 20);

        paddle.resetTo(-50);

        assertEquals(0, paddle.getX());
    }

    @Test
    void constructorClampsInitialOutOfBoundsPosition() {
        Paddle paddle = new Paddle(5000, 600, 100, 8, 1000, 20);

        assertEquals(900, paddle.getX());
    }
}
