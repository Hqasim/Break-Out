package hamzahqasim.breakout.view;

import hamzahqasim.breakout.engine.GameEngine;
import hamzahqasim.breakout.engine.GameState;
import hamzahqasim.breakout.model.Ball;
import hamzahqasim.breakout.model.BrickGrid;
import hamzahqasim.breakout.model.Paddle;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Swing view for the game. Owns a {@link GameEngine}, drives it with a
 * {@link Timer}, renders its state with a minimalist dark theme, and forwards
 * keyboard input to it. Contains no game logic itself - see {@link GameEngine}.
 */
public final class GameView extends JPanel implements KeyListener, ActionListener {

    private static final int TICK_MILLIS = 8;
    private static final int BRICK_GAP = 4;
    private static final int BRICK_ARC = 10;
    private static final int PANEL_PADDING = 80;
    private static final int PANEL_HEIGHT = 120;

    private final GameEngine engine;
    private final Timer timer;

    /**
     * @param width  board width in pixels
     * @param height board height in pixels
     */
    public GameView(int width, int height) {
        this.engine = new GameEngine(width, height);
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addKeyListener(this);
        timer = new Timer(TICK_MILLIS, this);
        timer.start();
    }

    /** Advances the engine by one tick and repaints. Invoked by the Swing {@link Timer}. */
    @Override
    public void actionPerformed(ActionEvent event) {
        engine.update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        paintBackground(g);
        paintBricks(g);
        paintPaddle(g);
        paintBall(g);
        paintScore(g);
        paintOverlay(g);
    }

    private void paintBackground(Graphics2D g) {
        g.setPaint(new GradientPaint(0, 0, Theme.BACKGROUND_TOP, 0, getHeight(), Theme.BACKGROUND_BOTTOM));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintBricks(Graphics2D g) {
        BrickGrid bricks = engine.getBricks();
        for (int row = 0; row < bricks.getRows(); row++) {
            Color base = Theme.BRICK_ROW_COLORS[row % Theme.BRICK_ROW_COLORS.length];
            for (int col = 0; col < bricks.getColumns(); col++) {
                if (!bricks.isActive(row, col)) {
                    continue;
                }
                paintBrick(g, bricks.getBounds(row, col), base);
            }
        }
    }

    private void paintBrick(Graphics2D g, Rectangle bounds, Color base) {
        RoundRectangle2D brick = new RoundRectangle2D.Double(
                bounds.x + BRICK_GAP / 2.0, bounds.y + BRICK_GAP / 2.0,
                bounds.width - BRICK_GAP, bounds.height - BRICK_GAP,
                BRICK_ARC, BRICK_ARC);
        g.setColor(base);
        g.fill(brick);

        // Flat-glossy highlight: clip to the brick's rounded shape and wash the top third.
        Shape previousClip = g.getClip();
        g.clip(brick);
        g.setColor(Theme.BRICK_HIGHLIGHT);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height / 3);
        g.setClip(previousClip);
    }

    private void paintPaddle(Graphics2D g) {
        Paddle paddle = engine.getPaddle();
        double arc = paddle.getHeight();
        RoundRectangle2D shadow = new RoundRectangle2D.Double(
                paddle.getX(), paddle.getY() + 3, paddle.getWidth(), paddle.getHeight(), arc, arc);
        g.setColor(Theme.PADDLE_SHADOW);
        g.fill(shadow);

        RoundRectangle2D body = new RoundRectangle2D.Double(
                paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight(), arc, arc);
        g.setPaint(new GradientPaint(
                paddle.getX(), 0, Theme.PADDLE_START,
                paddle.getX() + paddle.getWidth(), 0, Theme.PADDLE_END));
        g.fill(body);
    }

    private void paintBall(Graphics2D g) {
        Ball ball = engine.getBall();
        int x = (int) Math.round(ball.getX());
        int y = (int) Math.round(ball.getY());
        int glowInset = Ball.SIZE / 2;

        g.setColor(Theme.BALL_GLOW);
        g.fillOval(x - glowInset, y - glowInset, Ball.SIZE * 2, Ball.SIZE * 2);
        g.setColor(Theme.BALL_CORE);
        g.fillOval(x, y, Ball.SIZE, Ball.SIZE);
    }

    private void paintScore(Graphics2D g) {
        String scoreText = String.valueOf(engine.getScore());
        String label = "SCORE";
        FontMetrics scoreMetrics = g.getFontMetrics(Theme.scoreFont());
        FontMetrics labelMetrics = g.getFontMetrics(Theme.badgeFont());
        int rightEdge = getWidth() - 30;

        g.setFont(Theme.scoreFont());
        g.setColor(Theme.TEXT_PRIMARY);
        g.drawString(scoreText, rightEdge - scoreMetrics.stringWidth(scoreText), 42);

        g.setFont(Theme.badgeFont());
        g.setColor(Theme.TEXT_MUTED);
        g.drawString(label, rightEdge - labelMetrics.stringWidth(label), 20);
    }

    private void paintOverlay(Graphics2D g) {
        GameState state = engine.getState();
        switch (state) {
            case READY -> drawCenteredPanel(g, "BREAK OUT",
                    "Press ENTER to play  •  ← → to move  •  P to pause");
            case PAUSED -> drawCenteredPanel(g, "PAUSED", "Press P to resume");
            case GAME_OVER -> drawCenteredPanel(g, "GAME OVER",
                    "Score: " + engine.getScore() + "  •  Press ENTER to play again");
            case WON -> drawCenteredPanel(g, "YOU WIN!",
                    "Score: " + engine.getScore() + "  •  Press ENTER to play again");
            case RUNNING -> {
                // No overlay while actively playing.
            }
        }
    }

    private void drawCenteredPanel(Graphics2D g, String title, String subtitle) {
        Font titleFont = Theme.titleFont();
        Font subtitleFont = Theme.subtitleFont();
        FontMetrics titleMetrics = g.getFontMetrics(titleFont);
        FontMetrics subtitleMetrics = g.getFontMetrics(subtitleFont);

        int titleWidth = titleMetrics.stringWidth(title);
        int subtitleWidth = subtitleMetrics.stringWidth(subtitle);
        int panelWidth = Math.max(titleWidth, subtitleWidth) + PANEL_PADDING;
        int panelX = (getWidth() - panelWidth) / 2;
        int panelY = (getHeight() - PANEL_HEIGHT) / 2;

        g.setColor(Theme.OVERLAY_PANEL);
        g.fill(new RoundRectangle2D.Double(panelX, panelY, panelWidth, PANEL_HEIGHT, 24, 24));

        g.setFont(titleFont);
        g.setColor(Theme.TEXT_PRIMARY);
        g.drawString(title, (getWidth() - titleWidth) / 2, panelY + 52);

        g.setFont(subtitleFont);
        g.setColor(Theme.TEXT_ACCENT);
        g.drawString(subtitle, (getWidth() - subtitleWidth) / 2, panelY + 86);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT -> engine.moveLeft();
            case KeyEvent.VK_RIGHT -> engine.moveRight();
            case KeyEvent.VK_ENTER -> engine.start();
            case KeyEvent.VK_P -> engine.togglePause();
            default -> {
                // Ignored.
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent event) {
        // Unused: movement and actions are handled in keyPressed.
    }

    @Override
    public void keyReleased(KeyEvent event) {
        // Unused: movement and actions are handled in keyPressed.
    }
}
