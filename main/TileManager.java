package main;

//Imports needed for class
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


import javax.imageio.ImageIO;

public class TileManager {

    //The gamepanel
    GamePanel gamePanel;

    //The tile images
    BufferedImage[] tileImages;

    //A list of all the tiles in the level
    ArrayList<int[]> tileMap = new ArrayList<int[]>();
    Tile[][] levelTiles;


    public TileManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;

        //Loads images
        tileImages = new BufferedImage[10];
        getTileImage();
    }

    //Loads all images
    public void getTileImage(){
        try {
            tileImages[0] = ImageIO.read(getClass().getResourceAsStream("/main/Images/TileSprites/GameTitle.png"));
            tileImages[1] = ImageIO.read(getClass().getResourceAsStream("/main/Images/TileSprites/Background.png"));
            tileImages[2] = ImageIO.read(getClass().getResourceAsStream("/main/Images/TileSprites/Brick.png"));
            tileImages[3] = ImageIO.read(getClass().getResourceAsStream("/main/Images/TileSprites/Tile.png"));
            tileImages[4] = ImageIO.read(getClass().getResourceAsStream("/main/Images/TileSprites/BannerTop.png"));
            tileImages[5] = ImageIO.read(getClass().getResourceAsStream("/main/Images/TileSprites/BannerBottom.png"));
            tileImages[6] = ImageIO.read(getClass().getResourceAsStream("/main/Images/TileSprites/CandleTop.png"));
            tileImages[7] = ImageIO.read(getClass().getResourceAsStream("/main/Images/TileSprites/CandleBottom.png"));
            tileImages[8] = ImageIO.read(getClass().getResourceAsStream("/main/Images/TileSprites/LavaBrick.png"));
            tileImages[9] = ImageIO.read(getClass().getResourceAsStream("/main/Images/TileSprites/EndFlag.png"));


        } catch (Exception e) {
            System.out.println("Error: tile images not found"); // Displays an error if the images cannot be found
        }
    }

    //Loads the given level
    public void loadLevel (String levelFilePath){
        //Clears the tilemap
        tileMap.clear();
        try {
            //Opens a buffered reader to read the file
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(levelFilePath)));

            //The current line
            String line;

            //While there are more lines
            while ((line = br.readLine()) != null) {
                //Split the line into characters
                String[] characters = line.split(" ");

                //Create a list of numbers
                int[] numLine = new int[characters.length];

                //Copy each character into the number line as an intager
                for (int i = 0; i < characters.length; i++) {
                    numLine[i] = Integer.parseInt(characters[i]);
                }

                //Add the line to the tilemap
                tileMap.add(numLine);
            }

            //Close the reader
            br.close();
        } catch (Exception e) {
            e.printStackTrace(); //Print an error if the level file cannot be found
        }

        //Update the size of level tiles
        levelTiles = new Tile[tileMap.get(0).length][tileMap.size()];

        //Creates list of tiles
        for (int i = 0; i < tileMap.get(0).length; i++) {
            for (int j = 0; j < tileMap.size(); j++) {
                //Set the position of each tile
                float[] pos = {i*gamePanel.TILESIZE,j*gamePanel.TILESIZE};

                //Create the tile
                levelTiles[i][j] = new Tile(pos,tileImages[tileMap.get(j)[i]]);
                
                //If the tile is 2, 3, 4, or 8 give it collision
                if (tileMap.get(j)[i] == 2 || tileMap.get(j)[i] == 3 || tileMap.get(j)[i] == 8){
                    levelTiles[i][j].collision = true;
                }

                //If the tile is 8 give it damage
                if (tileMap.get(j)[i] == 8){
                    levelTiles[i][j].damage = true;
                }

                //If the tile is 9 set it as the end goal
                if (tileMap.get(j)[i] == 9){
                    gamePanel.endGoal[0] = (int)(i * gamePanel.TILESIZE);
                    gamePanel.endGoal[1] = (int)(j * gamePanel.TILESIZE);
                }   
            }
        }
    }

    //Draw the tiles
    public void draw(Graphics2D g2, float playerAverageX, float playerAverageY){
        //Loop over each tile
        for (int i = 0; i < levelTiles.length; i++) {
            for (int j = 0; j < levelTiles[0].length; j++) {
                //Get the screen position of the tile
                float tileScreenX = (levelTiles[i][j].pos[0]/64) * gamePanel.TILESIZE;
                float tileScreenY = (levelTiles[i][j].pos[1]/64) * gamePanel.TILESIZE;

                //Draw the tile
                g2.drawImage(levelTiles[i][j].image, //Image
                (int)(tileScreenX - playerAverageX + 544 + gamePanel.TILESIZE), //X position
                (int)(tileScreenY - playerAverageY + 352 + gamePanel.TILESIZE), //Y position
                (int)Math.ceil(gamePanel.TILESIZE), //Width
                (int)Math.ceil(gamePanel.TILESIZE), //Height
                null);
            }
        }
    }
}
