package hamzahqasim.BreakOut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameLogic extends JPanel implements KeyListener, ActionListener {
    // Variables
    private boolean playStatus = false;
    private boolean firstGame = true; // Keeps track of first game
    private int score = 0;
    private int brickRows = 4; // Total Rows of bricks
    private int brickColumns = 9; // Total Columns of bricks
    private int totalBricks = brickRows * brickColumns;
    private Timer timer;
    private int delay = 8;
    private int paddleX = 350; // Paddle X Coordinates
    private int paddleWidth = 100; // Paddle Width
    private int paddleHeight = 8; // Paddle Height
    private int paddleY = GameApp.gameWindowHeight - (2 * paddleHeight); // Paddle Y Coordinates
    private int ballXpos = 400; // Ball X Coordinate
    private int ballYpos = 350; // Ball Y Coordinate
    private double ballVelocity = Math.sqrt(5);
    private double ballXdir = 1; // Ball X direction step
    private double ballYdir = 2; // Ball Y direction step
    private BrickGenerator bricks;

    public GameLogic(){
        bricks = new BrickGenerator(brickRows, brickColumns);
        addKeyListener(this); // Allows to work with Key Listeners in instantiated objects
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g){
        // Background
        g.setColor(Color.BLACK);
        g.fillRect(0,0,GameApp.gameWindowWidth, GameApp.gameWindowHeight);
        // Score
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD, 25));
        g.drawString("" + score, GameApp.gameWindowWidth - 45, 30);
        // Bricks
        bricks.draw((Graphics2D)g);
        // Paddle
        g.setColor(Color.GREEN.darker());
        g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);
        // Ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballXpos, ballYpos, 20, 20); // Ball size is 20 x 20 pixels
        // Game starting Scenario
        if (firstGame == true){
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("Break Out game by Hamzah Qasim", 300, 420);
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press Enter to play. Use arrow keys to move paddle", 305, 450);
            firstGame = false;
        }
        // Game Lose Scenario
        if (ballYpos > GameApp.gameWindowHeight -12){
            playStatus = false;
            ballYdir = 0;
            ballXdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 360, 420);
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press Enter to play again", 395, 450);
        }
        // Game Win Scenario
        if (totalBricks <= 0 ){
            totalBricks = brickRows * brickColumns; // resets brick count
            playStatus = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("You Win! Score: " + score, 360, 420);
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press Enter to play again", 370, 450);

        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int ballXPreImpact = ballXpos; // Position of ball before impact
        timer.start();
        if (playStatus) {
            // Move the ball
            ballXpos += ballXdir; // Add velocity to ball X direction
            ballYpos += ballYdir; // Add velocity to ball Y direction

            // Left Border Collision
            if (ballXpos <= 0) {
                ballXdir = 1;
            }

            //Right Border Collision
            if (ballXpos >= (GameApp.gameWindowWidth - 20)) {
                ballXdir = -1;
            }
            // Top border Collision
            if (ballYpos <= 5) {
                ballYdir = -ballYdir;
            }

            // Ball and paddle collision
            if (new Rectangle(ballXpos, ballYpos, 20, 20).intersects(new
                    Rectangle(paddleX, paddleY, paddleWidth, paddleHeight))) {
                paddleCollision(ballXPreImpact);
            }

            // Ball and Brick collision
            A:
            for (int i = 0; i < bricks.map.length; i++) {
                for (int j = 0; j < bricks.map[0].length; j++) {
                    if (bricks.map[i][j] > 0) {
                        int brickX = j * bricks.brickWidth + 50;
                        int brickY = i * bricks.brickHeight + 50;
                        int brickWidth = bricks.brickWidth;
                        int brickHeight = bricks.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballrect = new Rectangle(ballXpos, ballYpos, 20, 20);

                        if (ballrect.intersects(brickRect)) {
                            bricks.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;
                            if (ballXpos + 19 <= brickRect.x || ballXpos + 1 >= (brickRect.x + brickRect.width)) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { }
    @Override
    public void keyReleased(KeyEvent keyEvent) { }
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // Right Key Paddle Movement
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight(); // Right Move method
        }
        // Left Key Paddle Movement
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft(); // Left Move method
        }

        // Starting a new game by pressing Enter
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
            if(playStatus == false){
                playStatus = true;
                ballXpos = 400;
                ballYpos = 350;
                ballXdir = -1;
                ballYdir = -2;
                paddleX = 310;
                score = 0;
                bricks = new BrickGenerator(brickRows, brickColumns);
                repaint();
            }
        }
    }
    public void moveRight(){
        if (paddleX >= GameApp.gameWindowWidth - paddleWidth){
            paddleX = GameApp.gameWindowWidth - paddleWidth;
        } else{
            paddleX += 20; //Increments paddle to right
        }
    }
    public void moveLeft(){
        if (paddleX <= 0) {
            paddleX = 0;
        } else {
            paddleX -= 20; //Increments paddle to Left
        }
    }
    // Paddle Collision Method. Called when ever ball hits paddle
    public void paddleCollision(int ballXPreImpact){
        // Rebound Mechanics
        if (ballXPreImpact < ballXpos) { // Ball moving right impacted with paddle
            if (ballXpos >= (paddleX-10) && ballXpos <= (paddleX + 16)) {
                ballXdir = -ballVelocity * Math.cos(Math.toRadians(30));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(30));
            } else if (ballXpos > (paddleX + 16) && ballXpos <= (paddleX + 32)) {
                ballXdir = -ballVelocity * Math.cos(Math.toRadians(50));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(50));
            } else if (ballXpos > (paddleX + 32) && ballXpos <= (paddleX + 50)) {
                ballXdir = -ballVelocity * Math.cos(Math.toRadians(70));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(70));
            } else if (ballXpos > (paddleX + 50) && ballXpos <= (paddleX + 68)) {
                ballXdir = ballVelocity * Math.cos(Math.toRadians(30));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(30));
            } else if (ballXpos > (paddleX + 68) && ballXpos <= (paddleX + 84)) {
                ballXdir = ballVelocity * Math.cos(Math.toRadians(50));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(50));
            } else if (ballXpos > (paddleX + 84) && ballXpos <= (paddleX + 100 + 10)) {
                ballXdir = ballVelocity * Math.cos(Math.toRadians(70));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(70));
            }
        } else if (ballXPreImpact > ballXpos) { // Ball moving left impacted with paddle
            if (ballXpos >= (paddleX-10) && ballXpos <= (paddleX + 16)) {
                ballXdir = -ballVelocity * Math.cos(Math.toRadians(30));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(30));
            } else if (ballXpos > (paddleX + 16) && ballXpos <= (paddleX + 32)) {
                ballXdir = -ballVelocity * Math.cos(Math.toRadians(50));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(50));
            } else if (ballXpos > (paddleX + 32) && ballXpos <= (paddleX + 50)) {
                ballXdir = -ballVelocity * Math.cos(Math.toRadians(70));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(70));
            } else if (ballXpos > (paddleX + 50) && ballXpos <= (paddleX + 68)) {
                ballXdir = ballVelocity * Math.cos(Math.toRadians(30));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(30));
            } else if (ballXpos > (paddleX + 68) && ballXpos <= (paddleX + 84)) {
                ballXdir = ballVelocity * Math.cos(Math.toRadians(50));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(50));
            } else if (ballXpos > (paddleX + 84) && ballXpos <= (paddleX + 100 + 10)) {
                ballXdir = ballVelocity * Math.cos(Math.toRadians(70));
                ballYdir = -ballVelocity * Math.sin(Math.toRadians(70));
            }
        } else { // Simple reflection rebound
            ballXdir = -ballXdir;
            ballYdir = -ballYdir;
        }
    }
}