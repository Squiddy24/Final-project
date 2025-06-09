package main;

//Imports needed for class
import java.awt.Rectangle;

public class CollisionChecker {

    public void checkTile (Player player, GamePanel gamePanel){

        //If the tiles exist
        if (gamePanel.tileManager.levelTiles != null){
            //Loop over all tiles 
            for (int i = 0; i < gamePanel.tileManager.levelTiles[0].length; i++) {
                for (int j = 0; j < gamePanel.tileManager.levelTiles.length; j++) {
                    //If the tile has collision enabled
                    if (gamePanel.tileManager.levelTiles[j][i].collision == true){

                        //The sides of the tile
                        float tileLeft = gamePanel.tileManager.levelTiles[j][i].pos[0];
                        float tileRight = gamePanel.tileManager.levelTiles[j][i].pos[0] + 64;
                        float tileTop = gamePanel.tileManager.levelTiles[j][i].pos[1];
                        float tileBottom = gamePanel.tileManager.levelTiles[j][i].pos[1] + 64;

                        //The bounds of the tile
                        Rectangle tileBounds = new Rectangle((int)tileLeft, (int)tileTop, 64, 64);
                        
                        //If the tile intersects with the player hitbox
                        if (tileBounds.intersects(player.hitbox)){
                            
                            //The players distance to each side of the tile
                            float DistTop = Math.abs(player.hitbox.y - tileBottom);
                            float DistBottom = Math.abs(player.hitbox.y + player.hitbox.height - tileTop);
                            float DistLeft = Math.abs(player.hitbox.x - tileRight);
                            float DistRight = Math.abs(player.hitbox.x + player.hitbox.width - tileLeft);
                            
                            //Detects hits on the top of a tile and moves the player out of it
                            if (DistBottom < DistLeft && DistBottom < DistRight && DistBottom < DistTop){
                                player.worldPos.y = (int)Math.ceil(tileTop  - gamePanel.TILESIZE + 32);

                                //Resets gravity
                                player.accelerationDueToGravity = 0;
                                player.jumpVelocity = 0;

                                //Enables jumping and dashing
                                player.grounded = true;
                                player.canDash = true;
                            }

                            //Detects hits on the bottom of a tile and moves the player out of it
                            else if (DistTop < DistLeft && DistTop < DistRight && DistTop < DistBottom){
                                player.worldPos.y = (int)Math.ceil(tileBottom);

                                //Ends the players jump
                                player.jumping = false;
                                player.currentJumpTime = 0;
                                player.jumpVelocity = 0;


                            }

                            //Detects hits on the Left of a tile and moves the player out of it
                            else if (DistRight < DistLeft && DistRight < DistTop && DistRight < DistBottom){
                                player.worldPos.x = (int)Math.ceil(tileLeft - gamePanel.TILESIZE + 32);
                                player.runSpeed = -player.runSpeed / 3; //Bounces the player away

                            }

                            //Detects hits on the right of a tile and moves the player out of it
                            else if (DistLeft < DistRight && DistLeft < DistTop && DistLeft < DistBottom){
                                player.worldPos.x = (int)Math.ceil(tileRight);
                                player.runSpeed = -player.runSpeed / 3; //Bounces the player away

                            }           
                        }            
                    }

                    //If the tile has damage enabled
                    if (gamePanel.tileManager.levelTiles[j][i].damage == true){
                        
                        //The bounds of the tile
                        Rectangle tileBounds = new Rectangle((int)gamePanel.tileManager.levelTiles[j][i].pos[0], (int)gamePanel.tileManager.levelTiles[j][i].pos[1], 64, 64);

                        //If the tile intersects with the player and the player has no stun immunity
                        if (tileBounds.intersects(player.hitbox) && player.stunImmunity <= 0){
                            //Stun the player
                            player.currentStunTime = player.STUNDURATION;
                            gamePanel.playSoundEffect(2);
                        }
                    }          
                }
            }
        }
    }   
}
