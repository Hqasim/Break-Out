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
                    // Coloring Rows with different colors
                    if (i == 0){
                        Color row0Color = new Color(190,15,15);
                        g.setColor(row0Color);
                        g.fillRect((j * brickWidth) + 50, (i * brickHeight + 50), brickWidth, brickHeight);
                    } else if (i == 1){
                        Color row1Color = new Color(255, 111, 14);
                        g.setColor(row1Color);
                        g.fillRect((j * brickWidth) + 50, (i * brickHeight + 50), brickWidth, brickHeight);
                    } else if (i == 2){
                        Color row2Color = new Color(49, 184, 117);
                        g.setColor(row2Color);
                        g.fillRect((j * brickWidth) + 50, (i * brickHeight + 50), brickWidth, brickHeight);
                    } else if (i==3){
                        Color row3Color = new Color(109, 14, 217);
                        g.setColor(row3Color);
                        g.fillRect((j * brickWidth) + 50, (i * brickHeight + 50), brickWidth, brickHeight);
                    }
                    // Adds padding between bricks
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
