package main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;


public class Player {

    LinkedList<int[]> trail = new LinkedList<>();
    final int MAXTRAINLENGTH = 30;

    int playerNumber;

    public final int zoomPadding = 200;

    Point worldPos = new Point(100,100);

    int speed = 4;

    GamePanel gamePanel;
    InputHandler input;

    float screenX;
    float screenY;

    int playerScreenX;
    int playerScreenY;
    int facing = 1;
    boolean collided = false;

    float xVelocity;
    float yVelocity;

    ImageHandler ImageHandler = new ImageHandler();
    HashMap<String,BufferedImage> playerImages; 

    //Gravity
    float accelerationDueToGravity;
    final float GRAVITY = 0.1f;
    final float TERMINALVELOCITY = 5;

    //Jump Stuff
    final int jumpPower = 6;
    final int KYODIETIME = 30;
    final int maxJumpLength = 30; //frames
    float jumpVelocity;
    float currentJumpTime = 0;
    boolean jumping;
    boolean canJump;
    boolean grounded = true;
    int currentKyodieTime = 0;

    //Run
    float runSpeed = 0;
    final float maxRunspeed = 5;
    final float RUNACCELERATION = 0.05f;
    final float RUNDECELERATION = 0.2f;

    //Dashing
    int lastDirection = 1;
    boolean canDash = true;
    boolean dashing;
    int currentDashTime;
    int currentDashCooldown;
    final int MAXDASHTIME = 30;
    final int DASHCOOLDOWN = 40;
    final float DASHPOWER = 7;
    float xCarryoverMomentum = 0;
    float yCarryoverMomentum = 0;

    final int STUNDURATION = 60;
    final int IMMUNITYDURATION = 240;
    int currentStunTime = 0;
    int stunImmunity = 120;

    //Rectangle hitbox = new Rectangle(6 * gamePanel.spriteScale, 12 * gamePanel.spriteScale, 20 * gamePanel.spriteScale, 20 * gamePanel.spriteScale);
    Rectangle hitbox;

    //animation stuff to clean up TODO
    int blinkTimer = 240;
    int blinkDuration = 120;

    public Player(GamePanel gamePanel, TileManager tileManager, InputHandler input, int playerNumber){
        this.playerNumber = playerNumber;
        this.gamePanel = gamePanel;
        this.input = input;

        screenX = gamePanel.screenWidth/2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight/2 - (gamePanel.tileSize / 2);
        hitbox = new Rectangle((int)worldPos.x, (int)worldPos.x, 44, 44);
        getPlayerImages();
    }

    public void update(float playerAverageX, float playerAverageY){        

        //Projects to screenSpace
        playerScreenX = (int)((worldPos.x + screenX) - playerAverageX + gamePanel.tileSize);
        playerScreenY = (int)((worldPos.y + screenY) - playerAverageY + 4*gamePanel.tileSize + gamePanel.tileSize/8);

        if (currentStunTime > 0){
            currentStunTime -=1;
            if (currentStunTime == 0){
                stunImmunity = IMMUNITYDURATION;
            }
        }else{
            if (stunImmunity > 0){
                stunImmunity -=1;
            }
            applyForces(); 
        }
        //updateZoom(); //TODO this breaks everyhting
        
        hitbox.setRect(worldPos.x,worldPos.y,gamePanel.tileSize/2,gamePanel.tileSize/2);
        int[] trailtPos = {(int)(worldPos.x + 3),(int)(worldPos.y - 1.5*gamePanel.tileSize)};
        trail.add(trailtPos);
        if (trail.size() > MAXTRAINLENGTH){
            trail.removeFirst();
        }


        gamePanel.collisionChecker.checkTile(this, gamePanel); //TODO
    }

    public void applyGravity(){
        if (accelerationDueToGravity < TERMINALVELOCITY){
            accelerationDueToGravity += GRAVITY;
        }
        yVelocity += accelerationDueToGravity;
    }

    public void applyForces(){
        xVelocity = 0;
        yVelocity = 0;
        if (!dashing){
            lastDirection = dashDirection();
            applyGravity();
            horizontalMovment();
            jump();
        }

        dash();


        worldPos.x += xVelocity;
        worldPos.y += yVelocity + jumpVelocity;
    }

    public int dashDirection(){
        int direction;

        boolean up = input.keyMap.get("Up");
        boolean down = input.keyMap.get("Down");
        boolean left = input.keyMap.get("Left");
        boolean right = input.keyMap.get("Right");

        direction = 
        (!up  && !down && !left && !right) ? lastDirection : //Last
        (up  && !down && !left && !right) ? 1 :  //Up
        (up  && !down && !left && right)  ? 2 :  //Up-Right
        (!up && !down && !left && right)  ? 3 :  //Right
        (!up && down  && !left && right)  ? 4 :  //Down-Right
        (!up && down  && !left && !right) ? 5 :  //Down
        (!up && down  && left  && !right) ? 6 :  //Down-Left
        (!up && !down && left  && !right) ? 7 :  //Left
        (up  && !down && left  && !right) ? 8 :  //Up-Left
        2;                                       //More than 2 (Up-Right defult)

        return direction;
        
    }

    public void dash(){
        currentDashCooldown -=1;

        //Start Dash
        if (input.keyMap.get("Dash") && !dashing && currentDashCooldown < 0 && canDash){
            runSpeed = 0;
            dashing = true;
            accelerationDueToGravity = 0;
            jumpVelocity = 0;
            currentJumpTime = 0;
            canDash = false;
            jumping = false;
            currentDashTime = MAXDASHTIME;
            gamePanel.playSoundEffect(1);
        }

        if (dashing){
            currentDashTime -= 1;
            float dashVelocityVertical = 0;
            float dashVelocityHorizontal = 0;

            switch (lastDirection) {
                case 1:      
                    dashVelocityVertical = -DASHPOWER;
                    break;
                case 2: 
                    dashVelocityHorizontal = (float)(DASHPOWER * Math.cos(Math.toRadians(45)));
                    dashVelocityVertical = (float)(DASHPOWER * -Math.sin(Math.toRadians(45)));
                    break;
                case 3:
                    dashVelocityHorizontal = DASHPOWER;        
                    break;
                case 4:   
                    dashVelocityHorizontal = (float)(DASHPOWER * Math.cos(Math.toRadians(315)));
                    dashVelocityVertical = (float)(DASHPOWER * -Math.sin(Math.toRadians(315)));     
                    break;
                case 5:         
                    dashVelocityVertical = DASHPOWER;
                    break;
                case 6:      
                    dashVelocityHorizontal = (float)(DASHPOWER * Math.cos(Math.toRadians(225)));
                    dashVelocityVertical = (float)(DASHPOWER * -Math.sin(Math.toRadians(225)));    
                    break;
                case 7: 
                    dashVelocityHorizontal = -DASHPOWER;         
                    break;  
                case 8:      
                    dashVelocityHorizontal = (float)(DASHPOWER * Math.cos(Math.toRadians(135)));
                    dashVelocityVertical = (float)(DASHPOWER * -Math.sin(Math.toRadians(135)));      
                    break;  
                default:
                    break;
            }

            xVelocity = dashVelocityHorizontal;
            yVelocity = dashVelocityVertical;

            //End Dash
            if (currentDashTime <= 0){
                dashing = false;
                currentDashCooldown = DASHCOOLDOWN;


                if (lastDirection == 2 || lastDirection == 3 || lastDirection == 4){ //Right
                    xCarryoverMomentum = dashVelocityHorizontal/2;
                }
                if (lastDirection == 6 || lastDirection == 7 || lastDirection == 8){ //Left
                    xCarryoverMomentum = dashVelocityHorizontal/2;
                }
                if (lastDirection == 4 || lastDirection == 5 || lastDirection == 6){ //Down
                    yCarryoverMomentum = dashVelocityVertical / 2;
                }
                if (lastDirection == 1 || lastDirection == 2 || lastDirection == 8){ //Up
                    yCarryoverMomentum = dashVelocityVertical / 2;
                }

            }
        }

        xVelocity += xCarryoverMomentum;
        yVelocity += yCarryoverMomentum;
        
        if (xCarryoverMomentum > 0){
            xCarryoverMomentum = Math.max(xCarryoverMomentum - 0.03f, 0);
        }else{
            xCarryoverMomentum = Math.min(xCarryoverMomentum + 0.03f, 0);
        }

        if (yCarryoverMomentum > 0){
            yCarryoverMomentum = Math.max(yCarryoverMomentum - 0.03f, 0);
        }else{
            yCarryoverMomentum = Math.min(yCarryoverMomentum + 0.03f, 0);
        }
    }

    public void horizontalMovment(){
        //Horizontal movement
        if (input.directionMap.get("Horizontal") != 0) {
            //runSpeed = Math.min(runSpeed + RUNACCELERATION * input.directionMap.get("Horizontal"), maxRunspeed);
            runSpeed = input.directionMap.get("Horizontal") == 1 ? Math.min(runSpeed + RUNACCELERATION, maxRunspeed) : Math.max(runSpeed - RUNACCELERATION, -maxRunspeed);
        }else{
            runSpeed = runSpeed > 0 ? Math.max(runSpeed - RUNDECELERATION, 0) : Math.min(runSpeed + RUNDECELERATION, 0);
        }
                
        xVelocity += runSpeed;
    }

    public void jump(){
        if (grounded){
            currentKyodieTime = KYODIETIME;
        }

        canJump = (currentKyodieTime > 0) && !jumping;
    
        if (input.keyMap.get("Jump") && canJump){
            jumping = true;
            currentKyodieTime = 0;
            accelerationDueToGravity = 0;

        }
        if (jumping){
            if (!input.keyMap.get("Jump")){ //Early Terminate jump
                jumping = false;
                jumpVelocity = 0;
                currentJumpTime = 0;
                accelerationDueToGravity = TERMINALVELOCITY / 2;

            }

            currentJumpTime += 1;

            if (currentJumpTime < maxJumpLength){
                jumpVelocity = -jumpPower;

            }else{
                jumping = false;
                currentJumpTime = 0;
            }
        }else{
            jumpVelocity = Math.min(jumpVelocity += 0.1, 0);
        }

        currentKyodieTime -=1;
        grounded = false;
    }

    public void getPlayerImages(){
        playerImages = playerNumber == 1 ? ImageHandler.player1Images() : ImageHandler.player2Images();
    }

    public BufferedImage animationHandler(){
        blinkTimer -=1;
        if ((currentStunTime > 20 || currentStunTime < 10) && currentStunTime > 0){return playerImages.get(null);}
        if (!canDash){
            if (currentStunTime <= 0){
                if(dashing){
                    return playerImages.get("NDDash");
                }else if (blinkDuration > 0 && blinkTimer < 0){
                    blinkDuration -= 1;
                    if (blinkDuration == 0){
                        blinkTimer = 240;
                        blinkDuration = 120;
                    }
                    return playerImages.get("NDBlink");

                }else{
                    return playerImages.get("NDBase");
                }
            }else{
                return playerImages.get("NDDeath");
            }
        }else{
            if (currentStunTime <= 0){
                if(dashing){
                    return playerImages.get("Dash");
                }else if (blinkDuration > 0 && blinkTimer < 0){
                    blinkDuration -= 1;
                    if (blinkDuration == 0){
                        blinkTimer = 240;
                        blinkDuration = 120;
                    }
                    return playerImages.get("Blink");
                }else{
                    return playerImages.get("Base");
                }
            }else{
                return playerImages.get("Death");
            }
        }
    }

    public void drawPlayer(Graphics2D g2){ 
        
        if(currentStunTime == 0){
            for (int pos = 0; pos < trail.size(); pos++) {
                g2.setColor(new Color(255,255,255,2*pos));
                int trailScreenX = (int)((trail.get(pos)[0] + screenX) - gamePanel.playerAverageX + gamePanel.tileSize);
                int trailScreenY = (int)((trail.get(pos)[1] + screenY) - gamePanel.playerAverageY + 4*gamePanel.tileSize + gamePanel.tileSize/8);

                g2.fillRect(trailScreenX, trailScreenY, (int)(gamePanel.tileSize /2.5), (int)(gamePanel.tileSize /2.5));
            }
        }
        facing = input.directionMap.get("Horizontal") != 0 ? input.directionMap.get("Horizontal") : facing;
        g2.drawImage(animationHandler(), 
        (int)(facing == 1 ? playerScreenX : playerScreenX + gamePanel.tileSize/2),  //x
        (int)playerScreenY, //y
        (int)(gamePanel.tileSize / 2) * facing, (int)gamePanel.tileSize / 2,null);

    }
}

