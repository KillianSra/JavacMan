package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager
{
    GamePanel gp;
    Tile[] tiles;
    int[][] mapTileNum;

    public TileManager(GamePanel gp)
    {
        this.gp = gp;
        this.tiles = new Tile[41];
        this.mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        this.getTileImage();
        this.loadMap("/map/gameboard.txt");
    }

    /**
     * Loads and sets up all the tile images into the `tiles` array.
     * Assigns placeholder images to unused tiles to prevent NullPointerExceptions.
     */
    private void getTileImage()
    {
        //Assign placeholder images to unused tiles (0-9)
        for(int i = 0; i < 9; i++)
        {
            setup(i, "black", false);
        }

        //Setup actual game tiles with their images and collision properties
        setup(10, "black", false);
        setup(11, "wall_bottom", true);
        setup(12, "wall_bottom_corner", true);
        setup(13, "wall_bottom_left", true);
        setup(14, "wall_bottom_right", true);
        setup(15, "wall_left", true);
        setup(16, "wall_left_corner", true);
        setup(17, "wall_right", true);
        setup(18, "wall_right_corner", true);
        setup(19, "wall_top", true);
        setup(20, "wall_top_corner", true);
        setup(21, "wall_top_left", true);
        setup(22, "wall_top_right", true);
        setup(23, "wall_horizontal", true);
        setup(24, "wall_vertical", true);
        setup(25, "wall_horizontal_to_down", true);
        setup(26, "wall_vertical_to_right_horizontal", true);
        setup(27, "wall_top_left_to_bottom_right_passing_by_top_right", true);
        setup(28, "wall_top_right_to_bottom_left_passing_by_top_left", true);
        setup(29, "wall_top_left_to_bottom_right_passing_by_bottom_left", true);
        setup(30, "wall_top_right_to_bottom_left_passing_by_bottom_right", true);
        setup(31, "wall_vertical_to_left_horizontal", true);
        setup(32, "wall_vertical_to_horizontal", true);
        setup(33, "wall_vertical_to_right_horizontal_2", true);
        setup(34, "wall_vertical_to_left_horizontal_2", true);
    }

    /**
     * Helper method to configure a tile with its image and collision property.
     *
     * @param index The index of the tile in the `tiles` array.
     * @param imageName The name of the image file (without extension) for the tile.
     * @param collision Whether the tile should block movement.
     */
    private void setup(int index, String imageName, boolean collision)
    {
        try
        {
            tiles[index] = new Tile();
            tiles[index].setImage(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/" + imageName + ".png"))));
            tiles[index].setImage(tiles[index].scaleImage(tiles[index].getImage(), gp.tileSize, gp.tileSize));
            tiles[index].setCollision(collision);
        }
        catch(IOException e)
        {
            System.err.println("TileManager.setup() : " + e);
        }
    }

    /**
     * Loads the tile map layout from a text file and stores it in the `mapTileNum` array.
     *
     * @param filePath The path to the map file.
     */
    public void loadMap(String filePath)
    {
        try
        {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            //Read the map file until all rows and columns are processed
            while(col < gp.maxScreenCol && row < gp.maxScreenRow)
            {
                String line = br.readLine();

                //Split the line into individual tile numbers
                while(col < gp.maxScreenCol)
                {
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                //Move to the next row after processing all columns
                if(col == gp.maxScreenCol)
                {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }
        catch (IOException e)
        {
            System.err.println("TileManager.loadMap() : " + e);
        }
    }

    /**
     * Draws all the tiles on the screen based on the map configuration.
     *
     * @param g2 The Graphics2D object used for drawing the tiles.
     */
    public void draw(Graphics2D g2)
    {
        int worldCol = 0;
        int worldRow = 0;

        //Iterate through all rows and columns of the map
        while (worldCol < gp.maxScreenCol && worldRow < gp.maxScreenRow)
        {
            int tileNum = mapTileNum[worldCol][worldRow];

            //Calculate the world coordinates for the current tile
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            //Draw the tile's image at the calculated position
            g2.drawImage(tiles[tileNum].getImage(), worldX, worldY, null);
            worldCol++;

            //Move to the next row after processing all columns
            if(worldCol == gp.maxScreenCol)
            {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}