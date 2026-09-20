package hamzahqasim.breakout.engine;

/** The finite set of states {@link GameEngine} can be in. */
public enum GameState {
    /** Waiting for the player to press Enter for the very first time. */
    READY,
    /** Ball is in motion, paddle responds to input. */
    RUNNING,
    /** Game frozen by the player pressing P; resumes back to {@link #RUNNING}. */
    PAUSED,
    /** Ball fell past the paddle. Pressing Enter starts a new game. */
    GAME_OVER,
    /** Every brick was destroyed. Pressing Enter starts a new game. */
    WON
}
