package hamzahqasim.BreakOut;

import java.awt.*;

public class BrickGenerator {
    // Variables
    public int map[][];
    public int brickWidth = 100;
    public int brickHeight = 60;

    public BrickGenerator(int row, int col){
        // Brick Active Logic Matrix
        map = new int[row][col];
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                map[i][j] = 1; // 1 = Brick Active, 0 = Brick inactive
            }
        }
    }

    public void draw (Graphics2D g) {
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                if (map[i][j] > 0){
                    // Drawing Brick
                    g.setColor(Color.red.darker());
                    g.fillRect((j * brickWidth) + 50, (i * brickHeight + 50), brickWidth, brickHeight);
                    // Setting Brick Padding
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect((j * brickWidth) + 50, (i * brickHeight + 50), brickWidth, brickHeight);
                }
            }
        }
    }

    // Sets brick values to change bricks
    public void setBrickValue (int value, int row, int col){
        map[row][col] = value;
    }

}
