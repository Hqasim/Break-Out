/**
 * Break Out - a Java Swing remake of the classic Atari Breakout arcade game.
 *
 * @author Hamzah Qasim
 * @since 2019-12-31
 */
package hamzahqasim.breakout;

import hamzahqasim.breakout.view.GameView;

import javax.swing.JFrame;

/** Application entry point: builds the window and wires it to the game view. */
public final class GameApp {

    private static final int BOARD_WIDTH = 1000;
    private static final int BOARD_HEIGHT = 700;
    private static final int WINDOW_X = 400;
    private static final int WINDOW_Y = 150;

    private GameApp() {
    }

    public static void main(String[] args) {
        System.out.println("Break Out Game by Hamzah Qasim");

        GameView gameView = new GameView(BOARD_WIDTH, BOARD_HEIGHT);

        JFrame window = new JFrame("BREAK OUT");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(gameView);
        window.pack();
        window.setLocation(WINDOW_X, WINDOW_Y);
        window.setVisible(true);

        // The panel must hold keyboard focus to receive Enter/arrow/P key events.
        gameView.requestFocusInWindow();
    }
}
