package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class CollisionChecker {
    GamePanel gamePanel;
    int leftCol;
    int rightCol;
    int topRow;
    int bottomRow;
    Tile[][] levelTiles;

    CollisionChecker(GamePanel gamePanel, Tile[][] levelTiles){
        this.gamePanel = gamePanel;
        this.levelTiles = levelTiles;

    }

    public void checkTile (Player player, GamePanel gamePanel){
        int hitboxLeft = (int)player.hitbox.x;
        int hitboxRight = (int)player.hitbox.x + player.hitbox.width;
        int hitboxTop = (int)player.hitbox.y;
        int hitboxBottom = (int)player.hitbox.y + player.hitbox.height;

        for (int i = 0; i < levelTiles[0].length; i++) {
            for (int j = 0; j < levelTiles.length; j++) {
                if (levelTiles[j][i].collision == true){

                    float tileLeft = levelTiles[j][i].pos[0];
                    float tileRight = levelTiles[j][i].pos[0] + 64;
                    float tileTop = levelTiles[j][i].pos[1];
                    float tileBottom = levelTiles[j][i].pos[1] + 64;

                    Rectangle tileBounds = new Rectangle((int)tileLeft, (int)tileTop, (int)gamePanel.tileSize, (int)gamePanel.tileSize);
                    
                    if (tileBounds.intersects(player.hitbox)){
                        float DistTop = Math.abs(hitboxTop - tileBottom);
                        float DistBottom = Math.abs(hitboxBottom - tileTop);
                        float DistLeft = Math.abs(hitboxLeft - tileRight);
                        float DistRight = Math.abs(hitboxRight - tileLeft);
                        
                        //Detects hits on the top of a block and moves the player out of it
                        if (DistBottom < DistLeft && DistBottom < DistRight && DistBottom < DistTop){
                            //player_hitbox.bottom = block.rect.top + 1;
                            player.worldYPos = tileTop - gamePanel.tileSize + 5*gamePanel.spriteScale - 1;
                        }

                        //Detects hits on the bottom of a block and moves the player out of it
                        else if (DistTop < DistLeft && DistTop < DistRight && DistTop < DistBottom){
                            //self.player_hitbox.top = block.rect.bottom;
                            player.worldYPos = tileBottom - 5*gamePanel.spriteScale + 1;
                        }

                        //Detects hits on the Left of a block and moves the player out of it
                        else if (DistRight < DistLeft && DistRight < DistTop && DistRight < DistBottom){
                            //self.player_hitbox.right = block.rect.left;
                            player.worldXPos = tileLeft - gamePanel.tileSize + 5*gamePanel.spriteScale - 1;
                        }

                        //Detects hits on the right of a block and moves the player out of it
                        else if (DistLeft < DistRight && DistLeft < DistTop && DistLeft < DistBottom){
                            //self.player_hitbox.left = block.rect.right;
                            player.worldXPos = tileRight - 5*gamePanel.spriteScale + 1;
                        }           
                    }            
                }
            }
        }
    }   
}
