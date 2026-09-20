package hamzahqasim.breakout.engine;

import hamzahqasim.breakout.model.Ball;
import hamzahqasim.breakout.model.BrickGrid;
import hamzahqasim.breakout.model.Paddle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameEngineTest {

    private static final int BOARD_WIDTH = 1000;
    private static final int BOARD_HEIGHT = 700;
    private static final double DELTA = 1e-6;

    private GameEngine engine;

    @BeforeEach
    void setUp() {
        engine = new GameEngine(BOARD_WIDTH, BOARD_HEIGHT);
    }

    @Test
    void newGameStartsInReadyState() {
        assertEquals(GameState.READY, engine.getState());
        assertEquals(0, engine.getScore());
    }

    @Test
    void startBeginsTheGameAndResetsBoard() {
        engine.start();

        assertEquals(GameState.RUNNING, engine.getState());
        assertEquals(0, engine.getScore());
        assertEquals(36, engine.getBricks().getRemaining());
    }

    @Test
    void startWhileRunningIsNoOpAndPreservesProgress() {
        engine.start();
        engine.getBricks().destroy(0, 0);

        engine.start(); // should not reset progress mid-game

        assertEquals(GameState.RUNNING, engine.getState());
        assertEquals(35, engine.getBricks().getRemaining());
    }

    @Test
    void togglePauseFreezesAndResumesTheGame() {
        engine.start();

        engine.togglePause();
        assertEquals(GameState.PAUSED, engine.getState());

        engine.togglePause();
        assertEquals(GameState.RUNNING, engine.getState());
    }

    @Test
    void togglePauseIsNoOpBeforeGameStarts() {
        engine.togglePause();

        assertEquals(GameState.READY, engine.getState());
    }

    @Test
    void updateDoesNothingWhilePaused() {
        engine.start();
        Ball ball = engine.getBall();
        double xBefore = ball.getX();
        double yBefore = ball.getY();
        engine.togglePause();

        engine.update();

        assertEquals(xBefore, ball.getX(), DELTA);
        assertEquals(yBefore, ball.getY(), DELTA);
    }

    @Test
    void moveLeftAndRightAreIgnoredWhilePaused() {
        engine.start();
        Paddle paddle = engine.getPaddle();
        int xBefore = paddle.getX();
        engine.togglePause();

        engine.moveRight();
        engine.moveLeft();

        assertEquals(xBefore, paddle.getX());
    }

    @Test
    void moveRightAdvancesPaddleWhileRunning() {
        engine.start();
        int xBefore = engine.getPaddle().getX();

        engine.moveRight();

        assertTrue(engine.getPaddle().getX() > xBefore);
    }

    @Test
    void ballBouncesOffLeftWall() {
        engine.start();
        Ball ball = engine.getBall();
        ball.setX(0);
        ball.setY(300);
        ball.setDirection(-1, 0);

        engine.update();

        assertTrue(ball.getDirectionX() > 0, "ball should now be heading right");
    }

    @Test
    void ballBouncesOffRightWall() {
        engine.start();
        Ball ball = engine.getBall();
        ball.setX(BOARD_WIDTH - Ball.SIZE);
        ball.setY(300);
        ball.setDirection(1, 0);

        engine.update();

        assertTrue(ball.getDirectionX() < 0, "ball should now be heading left");
    }

    @Test
    void ballBouncesOffTopWall() {
        engine.start();
        Ball ball = engine.getBall();
        ball.setX(500);
        ball.setY(0);
        ball.setDirection(0, -1);

        engine.update();

        assertTrue(ball.getDirectionY() > 0, "ball should now be heading down");
    }

    @Test
    void hittingABrickDestroysItAndAwardsPoints() {
        engine.start();
        BrickGrid bricks = engine.getBricks();
        var brickBounds = bricks.getBounds(0, 0);
        Ball ball = engine.getBall();
        ball.setX(brickBounds.getCenterX() - Ball.SIZE / 2.0);
        ball.setY(brickBounds.getCenterY() - Ball.SIZE / 2.0);
        ball.setDirection(0, 0);

        engine.update();

        assertFalse(bricks.isActive(0, 0));
        assertEquals(5, engine.getScore());
        assertEquals(GameState.RUNNING, engine.getState());
    }

    @Test
    void clearingEveryBrickWinsTheGame() {
        engine.start();
        BrickGrid bricks = engine.getBricks();
        for (int row = 0; row < bricks.getRows(); row++) {
            for (int col = 0; col < bricks.getColumns(); col++) {
                if (row != 0 || col != 0) {
                    bricks.destroy(row, col);
                }
            }
        }
        var brickBounds = bricks.getBounds(0, 0);
        Ball ball = engine.getBall();
        ball.setX(brickBounds.getCenterX() - Ball.SIZE / 2.0);
        ball.setY(brickBounds.getCenterY() - Ball.SIZE / 2.0);
        ball.setDirection(0, 0);

        engine.update();

        assertTrue(bricks.isCleared());
        assertEquals(GameState.WON, engine.getState());
    }

    @Test
    void ballFallingPastPaddleEndsTheGame() {
        engine.start();
        Ball ball = engine.getBall();
        ball.setX(0);
        ball.setY(BOARD_HEIGHT + 50);
        ball.setDirection(0, 1);

        engine.update();

        assertEquals(GameState.GAME_OVER, engine.getState());
    }

    @Test
    void paddleReboundsBallLeftWhenHitOnLeftHalf() {
        engine.start();
        Paddle paddle = engine.getPaddle();
        Ball ball = engine.getBall();
        ball.setX(paddle.getX()); // relativeX = 0 -> left-most zone
        ball.setY(paddle.getY());
        ball.setDirection(0, 1);

        engine.update();

        assertTrue(ball.getDirectionX() < 0, "hitting the left half should send the ball left");
        assertTrue(ball.getDirectionY() < 0, "the ball should rebound upward");
    }

    @Test
    void paddleReboundsBallRightWhenHitOnRightHalf() {
        engine.start();
        Paddle paddle = engine.getPaddle();
        Ball ball = engine.getBall();
        ball.setX(paddle.getX() + 60); // relativeX = 60 -> right-side zone
        ball.setY(paddle.getY());
        ball.setDirection(0, 1);

        engine.update();

        assertTrue(ball.getDirectionX() > 0, "hitting the right half should send the ball right");
        assertTrue(ball.getDirectionY() < 0, "the ball should rebound upward");
    }
}
