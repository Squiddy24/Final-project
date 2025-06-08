package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


import javax.imageio.ImageIO;

public class TileManager {

    GamePanel gamePanel;
    BufferedImage[] tileImages;
    ArrayList<int[]> tileMap = new ArrayList<int[]>();
    Tile[][] levelTiles;
    //int timer = 500;

    public TileManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        tileImages = new BufferedImage[10];
        getTileImage();
        //loadLevel("/main/LevelData/1.txt");
    }

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
            System.out.println("tile broke :(");
        }
    }

    public void loadLevel (String levelFilePath){
        tileMap.clear();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(levelFilePath)));
            String line;

            while ((line = br.readLine()) != null) {
                String[] characters = line.split(" ");
                int[] numLine = new int[characters.length];
                for (int i = 0; i < characters.length; i++) {
                    numLine[i] = Integer.parseInt(characters[i]);
                }
                tileMap.add(numLine);

            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        levelTiles = new Tile[tileMap.get(0).length][tileMap.size()];

        //Creates list of tiles
        for (int i = 0; i < tileMap.get(0).length; i++) {
            for (int j = 0; j < tileMap.size(); j++) {
                float[] pos = {i*gamePanel.tileSize,j*gamePanel.tileSize};
                levelTiles[i][j] = new Tile(pos,tileImages[tileMap.get(j)[i]]);
                
                if (tileMap.get(j)[i] == 2 || tileMap.get(j)[i] == 3 || tileMap.get(j)[i] == 8){
                    // TODO NOT COLLISION 
                    levelTiles[i][j].collision = true;
                }
                //Damage
                if (tileMap.get(j)[i] == 8){
                    levelTiles[i][j].damage = true;
                }

                if (tileMap.get(j)[i] == 9){
                    gamePanel.endGoal[0] = (int)(i * gamePanel.tileSize);
                    gamePanel.endGoal[1] = (int)(j * gamePanel.tileSize);

                }
                
            }
        }
    }

    public void draw(Graphics2D g2, float playerAverageX, float playerAverageY){
        for (int i = 0; i < levelTiles.length; i++) {
            for (int j = 0; j < levelTiles[0].length; j++) {
                float tileScreenX = (levelTiles[i][j].pos[0]/64) * gamePanel.tileSize;
                float tileScreenY = (levelTiles[i][j].pos[1]/64) * gamePanel.tileSize;

                
                g2.drawImage(levelTiles[i][j].image, 
                (int)(tileScreenX - playerAverageX + gamePanel.player.screenX + gamePanel.tileSize), 
                (int)(tileScreenY - playerAverageY + gamePanel.player.screenY + gamePanel.tileSize),
                //(int)(tileScreenX - playerAverageX + gamePanel.player.screenX - gamePanel.tileSize/2), 
                //(int)(tileScreenY - playerAverageY + gamePanel.player.screenY - gamePanel.tileSize/2),
                (int)Math.ceil(gamePanel.tileSize),
                (int)Math.ceil(gamePanel.tileSize),
                null);
            }
        }
    }
}
