package main;
import java.awt.Rectangle;

public class CollisionChecker {
    GamePanel gamePanel;
    int leftCol;
    int rightCol;
    int topRow;
    int bottomRow;
    double zoomFactor;
    //Tile[][] levelTiles = gamePanel.tileManager.levelTiles;

    CollisionChecker(GamePanel gamePanel){
        this.gamePanel = gamePanel;

    }

    public void checkTile (Player player, GamePanel gamePanel){
        int hitboxLeft = (int)player.hitbox.x;
        int hitboxRight = (int)player.hitbox.x + player.hitbox.width;
        int hitboxTop = (int)player.hitbox.y;
        int hitboxBottom = (int)player.hitbox.y + player.hitbox.height;
        zoomFactor = ((gamePanel.tileSize/64)) ;

        for (int i = 0; i < gamePanel.tileManager.levelTiles[0].length; i++) {
            for (int j = 0; j < gamePanel.tileManager.levelTiles.length; j++) {
                
                if (gamePanel.tileManager.levelTiles[j][i].collision == true){

                    float tileLeft = gamePanel.tileManager.levelTiles[j][i].pos[0];
                    float tileRight = gamePanel.tileManager.levelTiles[j][i].pos[0] + 64;
                    float tileTop = gamePanel.tileManager.levelTiles[j][i].pos[1];
                    float tileBottom = gamePanel.tileManager.levelTiles[j][i].pos[1] + 64;

                    Rectangle tileBounds = new Rectangle((int)tileLeft, (int)tileTop, 64, 64);
                    
                    if (tileBounds.intersects(player.hitbox)){

                        float DistTop = Math.abs(hitboxTop - tileBottom);
                        float DistBottom = Math.abs(hitboxBottom - tileTop);
                        float DistLeft = Math.abs(hitboxLeft - tileRight);
                        float DistRight = Math.abs(hitboxRight - tileLeft);
                        
                        //Detects hits on the top of a block and moves the player out of it
                        if (DistBottom < DistLeft && DistBottom < DistRight && DistBottom < DistTop){
                            //player_hitbox.bottom = block.rect.top + 1;
                            player.worldYPos = Math.ceil((tileTop * zoomFactor) - gamePanel.tileSize + 32);
                            player.accelerationDueToGravity = 0;
                            player.jumpVelocity = 0;
                            player.grounded = true;
                            player.canDash = true;
                        }

                        //Detects hits on the bottom of a block and moves the player out of it
                        else if (DistTop < DistLeft && DistTop < DistRight && DistTop < DistBottom){
                            //self.player_hitbox.top = block.rect.bottom;
                            player.worldYPos = Math.ceil((tileBottom * zoomFactor));
                            player.jumping = false;
                            player.currentJumpTime = 0;
                            player.jumpVelocity = 0;


                        }

                        //Detects hits on the Left of a block and moves the player out of it
                        else if (DistRight < DistLeft && DistRight < DistTop && DistRight < DistBottom){
                            //self.player_hitbox.right = block.rect.left;
                            player.worldXPos = Math.ceil((tileLeft * zoomFactor) - gamePanel.tileSize + 32);
                            player.runSpeed = -player.runSpeed / 3;

                        }

                        //Detects hits on the right of a block and moves the player out of it
                        else if (DistLeft < DistRight && DistLeft < DistTop && DistLeft < DistBottom){
                            //self.player_hitbox.left = block.rect.right;
                            player.worldXPos = Math.ceil((tileRight * zoomFactor));
                            player.runSpeed = -player.runSpeed / 3;

                        }           
                    }            
                }
                if (gamePanel.tileManager.levelTiles[j][i].damage == true){

                    Rectangle tileBounds = new Rectangle((int)gamePanel.tileManager.levelTiles[j][i].pos[0], (int)gamePanel.tileManager.levelTiles[j][i].pos[1], 64, 64);
                    if (tileBounds.intersects(player.hitbox) && player.stunImmunity <= 0){
                        player.currentStunTime = player.STUNDURATION;
                    }
                }
            }
        }
    }   
}
