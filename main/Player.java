package main;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;


public class Player {

    int playerNumber;

    public final int zoomPadding = 200;

    double worldXPos;
    double worldYPos;

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

    


    //Rectangle hitbox = new Rectangle(6 * gamePanel.spriteScale, 12 * gamePanel.spriteScale, 20 * gamePanel.spriteScale, 20 * gamePanel.spriteScale);
    Rectangle hitbox;

    //animation stuff to clean up TODO
    int blinkTimer = 240;
    int blinkDuration = 30;

    public Player(int startX, int startY, GamePanel gamePanel, TileManager tileManager, InputHandler input, int playerNumber){
        this.playerNumber = playerNumber;
        this.gamePanel = gamePanel;
        this.input = input;

        worldXPos = startX;
        worldYPos = startY;

        screenX = gamePanel.screenWidth/2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight/2 - (gamePanel.tileSize / 2);
        hitbox = new Rectangle((int)worldXPos, (int)worldYPos, 44, 44);
        getPlayerImages();
    }

    public void update(float playerAverageX, float playerAverageY){        
        
        //Projects to screenSpace
        playerScreenX = (int)((playerAverageX - (worldXPos * gamePanel.ratio) + screenX));
        playerScreenY = (int)((playerAverageY - (worldYPos * gamePanel.ratio) + screenY));

        applyForces(); 

        //updateZoom(); //TODO this breaks everyhting
        
        hitbox.setRect(worldXPos,worldYPos,gamePanel.tileSize/2,gamePanel.tileSize/2);
        gamePanel.collisionChecker.checkTile(this, gamePanel);
    }

    public void updateZoom(){
        float dynamicZoom;

        //Zoom padding
        if (playerScreenX > gamePanel.screenWidth - zoomPadding || playerScreenX < zoomPadding){
            dynamicZoom = (float)(-1/Math.ceil(((playerScreenX-gamePanel.screenWidth)*3))) * gamePanel.spriteScale;
            if (dynamicZoom > 0.003){ //stops stutter as zoom comes to an end
                gamePanel.zoom(dynamicZoom);
            }
        }else if (gamePanel.spriteScale > 2){
            gamePanel.zoom((float)(1/Math.ceil(((playerScreenX-gamePanel.screenWidth)*3))) * gamePanel.spriteScale);
        }
        
        if(playerScreenY > gamePanel.screenHeight - zoomPadding || playerScreenY < zoomPadding){
            dynamicZoom = (float)(-1/Math.ceil(((playerScreenY-gamePanel.screenHeight))*3)) * gamePanel.spriteScale;
            if (dynamicZoom > 0.006){ //stops stutter as zoom comes to an end
                gamePanel.zoom(dynamicZoom);
            } 
        }else if (gamePanel.spriteScale > 2){
            gamePanel.zoom((float)(1/Math.ceil(((playerScreenY-gamePanel.screenHeight))*3)) * gamePanel.spriteScale);
        }
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

        applyGravity();
        horizontalMovment();
        jump();

        worldXPos += xVelocity;
        worldYPos += yVelocity + jumpVelocity;
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
        if (blinkTimer <= 0){
            blinkDuration -=1;
            if (blinkDuration <= 0){
                blinkTimer = 240;
                blinkDuration = 30;
            }
            return playerImages.get("Blink");

        }
        blinkTimer -= 1;

        return playerImages.get("Base");
    }

    public void drawPlayer(Graphics2D g2, InputHandler otherInput){ 
        facing = otherInput.directionMap.get("Horizontal") != 0 ? otherInput.directionMap.get("Horizontal") : facing;
        g2.drawImage(animationHandler(), 
        (int)(facing == 1 ? playerScreenX : playerScreenX + gamePanel.tileSize/2),  //x
        (int)playerScreenY, //y
        (int)(gamePanel.tileSize / 2) * facing, (int)gamePanel.tileSize / 2,null);
        
        //g2.drawRect(hitbox.x,hitbox.y,hitbox.width,hitbox.height);
        //g2.setColor(Color.GREEN);


    }
}

