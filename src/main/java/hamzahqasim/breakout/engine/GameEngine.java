package hamzahqasim.breakout.engine;

import hamzahqasim.breakout.model.Ball;
import hamzahqasim.breakout.model.BrickGrid;
import hamzahqasim.breakout.model.Paddle;

import java.awt.Rectangle;

/**
 * Pure game-logic core of Breakout: physics, collisions, scoring and state
 * transitions. Deliberately has no dependency on Swing/AWT painting so it can
 * be driven and asserted on directly in unit tests, independent of any UI.
 *
 * <p>The owning view is expected to call {@link #update()} once per timer tick,
 * and to forward player input to {@link #moveLeft()}, {@link #moveRight()},
 * {@link #start()} and {@link #togglePause()}.
 */
public final class GameEngine {

    private static final int BRICK_ROWS = 4;
    private static final int BRICK_COLUMNS = 9;
    private static final int BRICK_WIDTH = 100;
    private static final int BRICK_HEIGHT = 60;
    private static final int BRICK_OFFSET_X = 50;
    private static final int BRICK_OFFSET_Y = 50;

    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 8;
    private static final int PADDLE_STEP = 20;

    private static final double BALL_SPEED = Math.sqrt(5);
    private static final int POINTS_PER_BRICK = 5;
    // Kept just below the paddle's Y position so a legitimate paddle bounce always
    // resolves before this fires; only a ball that actually slips past the paddle trips it.
    private static final int BALL_LOSS_MARGIN = 12;

    private final int boardWidth;
    private final int boardHeight;
    private final int paddleY;
    private final int paddleStartX;
    private final int ballStartX;
    private final int ballStartY;

    private final Ball ball;
    private final Paddle paddle;
    private final BrickGrid bricks;
    private GameState state;
    private int score;

    /**
     * @param boardWidth  width of the playable board in pixels
     * @param boardHeight height of the playable board in pixels
     */
    public GameEngine(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.paddleY = boardHeight - (2 * PADDLE_HEIGHT);
        this.paddleStartX = (boardWidth - PADDLE_WIDTH) / 2;
        this.ballStartX = boardWidth / 2 - Ball.SIZE / 2;
        this.ballStartY = boardHeight / 2 - 50;

        this.paddle = new Paddle(paddleStartX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT, boardWidth, PADDLE_STEP);
        this.ball = new Ball(ballStartX, ballStartY, 0, 0, BALL_SPEED);
        this.bricks = new BrickGrid(BRICK_ROWS, BRICK_COLUMNS, BRICK_WIDTH, BRICK_HEIGHT, BRICK_OFFSET_X, BRICK_OFFSET_Y);
        this.state = GameState.READY;
        this.score = 0;
    }

    /**
     * Starts a new game: resets the ball, paddle, bricks and score, and moves
     * to {@link GameState#RUNNING}. A no-op while a game is already {@code RUNNING}
     * or {@code PAUSED} — matches the "press Enter to (re)start" behavior of the
     * title, game-over and win screens.
     */
    public void start() {
        if (state == GameState.RUNNING || state == GameState.PAUSED) {
            return;
        }
        score = 0;
        paddle.resetTo(paddleStartX);
        bricks.reset();
        ball.setX(ballStartX);
        ball.setY(ballStartY);
        ball.setDirection(-1, -2);
        state = GameState.RUNNING;
    }

    /** Toggles between {@code RUNNING} and {@code PAUSED}; a no-op in any other state. */
    public void togglePause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /** Moves the paddle left, unless the game is {@code PAUSED}. */
    public void moveLeft() {
        if (state != GameState.PAUSED) {
            paddle.moveLeft();
        }
    }

    /** Moves the paddle right, unless the game is {@code PAUSED}. */
    public void moveRight() {
        if (state != GameState.PAUSED) {
            paddle.moveRight();
        }
    }

    /**
     * Advances the simulation by one tick: moves the ball, resolves wall,
     * paddle and brick collisions, and updates score/state accordingly.
     * A no-op unless the game is currently {@code RUNNING}.
     */
    public void update() {
        if (state != GameState.RUNNING) {
            return;
        }

        ball.move();
        resolveWallCollisions();
        resolvePaddleCollision();
        resolveBrickCollisions();
        resolveEndConditions();
    }

    private void resolveWallCollisions() {
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.reverseX();
        } else if (ball.getX() >= boardWidth - Ball.SIZE) {
            ball.setX(boardWidth - Ball.SIZE);
            ball.reverseX();
        }
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.reverseY();
        }
    }

    private void resolvePaddleCollision() {
        if (!ball.getBounds().intersects(paddle.getBounds())) {
            return;
        }
        double relativeX = ball.getX() - paddle.getX();
        // Six fixed zones across the paddle width, each rebounding at a hand-tuned angle.
        // Left-of-center zones send the ball left, right-of-center send it right, getting
        // steeper towards the paddle's center. This mapping is deliberately preserved as-is.
        if (relativeX >= -10 && relativeX <= 16) {
            reboundAt(30, false);
        } else if (relativeX <= 32) {
            reboundAt(50, false);
        } else if (relativeX <= 50) {
            reboundAt(70, false);
        } else if (relativeX <= 68) {
            reboundAt(30, true);
        } else if (relativeX <= 84) {
            reboundAt(50, true);
        } else if (relativeX <= 110) {
            reboundAt(70, true);
        }
        // Outside [-10, 110] the ball keeps its current direction for this tick, matching
        // the original hit-table's behavior at the extreme edges of the paddle.
    }

    private void reboundAt(double angleDegrees, boolean rightward) {
        double radians = Math.toRadians(angleDegrees);
        double dirX = rightward ? Math.cos(radians) : -Math.cos(radians);
        ball.setDirection(dirX, -Math.sin(radians));
    }

    private void resolveBrickCollisions() {
        Rectangle ballBounds = ball.getBounds();
        for (int row = 0; row < bricks.getRows(); row++) {
            for (int col = 0; col < bricks.getColumns(); col++) {
                if (!bricks.isActive(row, col)) {
                    continue;
                }
                Rectangle brickBounds = bricks.getBounds(row, col);
                if (!ballBounds.intersects(brickBounds)) {
                    continue;
                }
                bricks.destroy(row, col);
                score += POINTS_PER_BRICK;

                boolean approachedHorizontally = ballBounds.x + ballBounds.width - 1 <= brickBounds.x
                        || ballBounds.x + 1 >= brickBounds.x + brickBounds.width;
                if (approachedHorizontally) {
                    ball.reverseX();
                } else {
                    ball.reverseY();
                }
                return; // Resolve at most one brick per tick.
            }
        }
    }

    private void resolveEndConditions() {
        if (ball.getY() > boardHeight - BALL_LOSS_MARGIN) {
            ball.setDirection(0, 0);
            state = GameState.GAME_OVER;
        } else if (bricks.isCleared()) {
            ball.setDirection(0, 0);
            state = GameState.WON;
        }
    }

    public Ball getBall() {
        return ball;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public BrickGrid getBricks() {
        return bricks;
    }

    public GameState getState() {
        return state;
    }

    public int getScore() {
        return score;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }
}
