/**
 * This game application is inspired by the famous Breakout Arcade video game, developed and published by Atari, Inc.,
 * and released in 1976. Game is developed using Java programming language with Swing library.
 * @author  Hamzah Qasim
 * @email hqqasim55@gmail.com
 * @version 1.0
 * @since   2019-12-31
 */
package hamzahqasim.BreakOut;

import javax.swing.*;

class GameApp {
    // Variables
    public static int gameWindowWidth = 1000;
    public static int gameWindowHeight = 700;
    public static int gameWindowX = 400; // X location on screen where the game window is positioned
    public static int gameWindowY = 150; // Y location on screen where the game window is positioned

    public static void main(String[] args){
        System.out.println("Break Out Game by Hamzah Qasim");
        // Calling Game Logic class
        GameLogic gameLogic = new GameLogic();
        //Configuring Game Window
        JFrame gameWindow = new JFrame();
        gameWindow.setBounds(gameWindowX ,gameWindowY, gameWindowWidth + 16, gameWindowHeight+ 39);
        gameWindow.setResizable(false); // Fixed window size
        gameWindow.setTitle("BREAK OUT");
        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.add(gameLogic);
    }
}
