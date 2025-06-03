package main;
import java.awt.Rectangle;

public class CollisionChecker {
    GamePanel gamePanel;
    int leftCol;
    int rightCol;
    int topRow;
    int bottomRow;
    double zoomFactor;
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
        zoomFactor = ((gamePanel.tileSize/64)) ;
        System.out.println(zoomFactor);

        for (int i = 0; i < levelTiles[0].length; i++) {
            for (int j = 0; j < levelTiles.length; j++) {
                if (levelTiles[j][i].collision == true){

                    float tileLeft = levelTiles[j][i].pos[0];
                    float tileRight = levelTiles[j][i].pos[0] + 64;
                    float tileTop = levelTiles[j][i].pos[1];
                    float tileBottom = levelTiles[j][i].pos[1] + 64;

                    Rectangle tileBounds = new Rectangle((int)tileLeft, (int)tileTop, 64, 64);
                    
                    if (tileBounds.intersects(player.hitbox)){

                        float DistTop = Math.abs(hitboxTop - tileBottom);
                        float DistBottom = Math.abs(hitboxBottom - tileTop);
                        float DistLeft = Math.abs(hitboxLeft - tileRight);
                        float DistRight = Math.abs(hitboxRight - tileLeft);
                        
                        //Detects hits on the top of a block and moves the player out of it
                        if (DistBottom < DistLeft && DistBottom < DistRight && DistBottom < DistTop){
                            //player_hitbox.bottom = block.rect.top + 1;
                            player.worldYPos = (tileTop * zoomFactor) - gamePanel.tileSize + 5*gamePanel.spriteScale - 1;
                            player.hitboxYPos = tileTop - 64 + 5*2 - 1;

                        }

                        //Detects hits on the bottom of a block and moves the player out of it
                        else if (DistTop < DistLeft && DistTop < DistRight && DistTop < DistBottom){
                            //self.player_hitbox.top = block.rect.bottom;
                            player.worldYPos = (tileBottom * zoomFactor) - 5*gamePanel.spriteScale + 1;
                            player.hitboxYPos = tileBottom - 5*2 + 1;

                        }

                        //Detects hits on the Left of a block and moves the player out of it
                        else if (DistRight < DistLeft && DistRight < DistTop && DistRight < DistBottom){
                            //self.player_hitbox.right = block.rect.left;
                            player.worldXPos = (tileLeft * zoomFactor) - gamePanel.tileSize + 5*gamePanel.spriteScale - 1;
                            player.hitboxXPos = tileLeft - 64 + 5*2 - 1;

                        }

                        //Detects hits on the right of a block and moves the player out of it
                        else if (DistLeft < DistRight && DistLeft < DistTop && DistLeft < DistBottom){
                            //self.player_hitbox.left = block.rect.right;
                            player.worldXPos = (tileRight * zoomFactor) - 5*gamePanel.spriteScale + 1;
                            player.hitboxXPos = tileRight - 5*2 + 1;

                        }           
                    }            
                }
            }
        }
    }   
}
