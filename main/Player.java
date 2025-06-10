package main;

//Imports needed for class
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;


public class Player {

    //Trail
    LinkedList<int[]> trail = new LinkedList<>();
    final int MAXTRAINLENGTH = 30;

    //Position of player
    public Point worldPos = new Point(100,100); //Players position in the world
    public Point screenPos = new Point(); //Players position on the screen

    //Game panel and input manager
    GamePanel gamePanel;
    InputManager input;

    //Images
    ImageManager ImageHandler = new ImageManager();
    HashMap<String,BufferedImage> playerImages; 

    //The diraction the player is facing
    int facing = 1;

    //The velocity of the player
    public Point velocity;

    //Gravity
    final float GRAVITY = 0.1f;
    final float TERMINALVELOCITY = 5;
    public float accelerationDueToGravity; //current acceleration


    //Jump Stuff
    private final int JUMPPOWER = 6;
    private final int KYODIETIME = 30;
    private final int MAXJUMPLENGTH = 40;
    public float jumpVelocity;
    public float currentJumpTime = 0;
    public boolean jumping;
    private boolean canJump;
    public boolean grounded = true;
    private int currentKyodieTime = 0; //Time a play can run off a tile before they are no longer able to jump

    //Runing
    private final float MAXRUNSPEED = 4;
    private final float RUNACCELERATION = 0.1f;
    private final float RUNDECELERATION = 0.2f;
    public float runSpeed = 0; //Current running speed

    //Dashing
    final int MAXDASHTIME = 20;
    final int DASHCOOLDOWN = 40;
    final float DASHPOWER = 8;
    private int lastDirection = 1; //Last direction the player was pressing 1-8
    public boolean dashing;
    private int currentDashTime;
    public int currentDashCooldown;
    public boolean canDash = true;
    private float xCarryoverMomentum = 0;// X momentum carryed over from a dash
    private float yCarryoverMomentum = 0;// Y momentum carryed over from a dash

    //Stun
    public final int STUNDURATION = 60;
    private final int IMMUNITYDURATION = 60; // The duration in which a player cannot be damaged again
    public int currentStunTime = 0;
    public int stunImmunity = 120;

    //The part of the player that interacts with objects
    Rectangle hitbox;

    //Animation timer
    int blinkTimer = 240;
    int blinkDuration = 120;

    public Player(GamePanel gamePanel, InputManager input, int playerNumber){
        this.gamePanel = gamePanel;
        this.input = input;

        //Initializes the hitbox
        hitbox = new Rectangle((int)worldPos.x, (int)worldPos.x, 44, 44);

        //Initializes the images
        playerImages = getPlayerImages(playerNumber);
    }

    //Updates the players trail
    private void updateTrail(){
        //Adds the current possition to the trail list
        int[] trailtPos = {(int)(worldPos.x + 3),(int)(worldPos.y - 1.5*gamePanel.TILESIZE)};
        trail.add(trailtPos);

        //If the trail exeeds the max trail length remove the first element
        if (trail.size() > MAXTRAINLENGTH){
            trail.removeFirst();
        }
        
    }

    //Renders the players trail
    private void renderTrail(Graphics2D g2){

        //If the player is not being stunned
        if(currentStunTime == 0){

            //Loops over each position in the trail
            for (int pos = 0; pos < trail.size(); pos++) {

                //Change alpha based on position in trail (fade out) 
                g2.setColor(new Color(255,255,255,2*pos));

                //Project to screen space
                int trailScreenX = (int)((trail.get(pos)[0] + 544) - gamePanel.playerAverage.x + gamePanel.TILESIZE);
                int trailScreenY = (int)((trail.get(pos)[1] + 352) - gamePanel.playerAverage.y + 4*gamePanel.TILESIZE + gamePanel.TILESIZE/8);

                //Draw to screen space
                g2.fillRect(trailScreenX, trailScreenY, (int)(gamePanel.TILESIZE /2.5), (int)(gamePanel.TILESIZE /2.5));
            }
        }
    }

    //Updates the player
    public void update(float playerAverageX, float playerAverageY){        

        //Projects to screenSpace
        screenPos.x = (int)((worldPos.x + 544) - playerAverageX + gamePanel.TILESIZE);
        screenPos.y = (int)((worldPos.y + 352) - playerAverageY + 4*gamePanel.TILESIZE + gamePanel.TILESIZE/8);

        //Updates stun 
        if (currentStunTime > 0){
            currentStunTime -=1;
            if (currentStunTime == 0){
                stunImmunity = IMMUNITYDURATION;
            }
        }else{
            if (stunImmunity > 0){
                stunImmunity -=1;
            }
            applyForces(); //applys the forces
        }
        
        //Updates the hitbos
        hitbox.setRect(worldPos.x,worldPos.y,gamePanel.TILESIZE/2,gamePanel.TILESIZE/2);

        //Render the trail
        updateTrail();

        //Check for collision
        gamePanel.collisionChecker.checkTile(this, gamePanel);
    }

    //Applys gravity
    private void applyGravity(){
        //If the player has not exeeded terminal velocity add acceleration
        if (accelerationDueToGravity < TERMINALVELOCITY){
            accelerationDueToGravity += GRAVITY;
        }

        //Apply velocity
        velocity.y += accelerationDueToGravity;
    }

    //applys forces to the player
    private void applyForces(){
        //Resets this frames velocity
        velocity = new Point();

        //If the player is not dashing
        if (!dashing){
            //update last direction
            lastDirection = dashDirection();

            //Apply gravity
            applyGravity();

            //Apply horizontal movment
            horizontalMovment();

            //Apply jump forces
            jump();
        }

        //Apply dash forces
        dash();


        //Update player position
        worldPos.x += velocity.x;
        worldPos.y += velocity.y + jumpVelocity;
    }

    //Finds the direction the player is dashing in
    private int dashDirection(){
        //Direction player is dashing (1-8)
        int direction;

        //Gets currently pressed keys
        boolean up = input.keyMap.get("Up");
        boolean down = input.keyMap.get("Down");
        boolean left = input.keyMap.get("Left");
        boolean right = input.keyMap.get("Right");

        //Sets direction (1-8) starting upwards and moving clockwise by 45 degrees
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

        //Returns direction
        return direction;
        
    }

    //Applies dash forces
    public void dash(){
        //Reduces dath cooldown
        currentDashCooldown -=1;

        //Start Dash
        if (input.keyMap.get("Dash") && !dashing && currentDashCooldown < 0 && canDash){
            //Resets velocities
            runSpeed = 0;
            accelerationDueToGravity = 0;
            jumpVelocity = 0;
            currentJumpTime = 0;

            //Sets booleans
            dashing = true;
            canDash = false;
            jumping = false;

            //Starts dash timer
            currentDashTime = MAXDASHTIME;

            //Plays sound effect
            gamePanel.playSoundEffect(1);
        }

        //During Dash
        if (dashing){
            //Reduces dash timer
            currentDashTime -= 1;

            //Creates Dash velocities
            float dashVelocityVertical = 0;
            float dashVelocityHorizontal = 0;

            //Sets dash velocities
            switch (lastDirection) {
                case 1:      
                    dashVelocityVertical = -DASHPOWER;
                    break;
                case 2: 
                    //Trig is used to keep the velocity bound to a circle
                    dashVelocityHorizontal = (float)(DASHPOWER * Math.cos(Math.toRadians(45)));
                    dashVelocityVertical = (float)(DASHPOWER * -Math.sin(Math.toRadians(45)));
                    break;
                case 3:
                    dashVelocityHorizontal = DASHPOWER;        
                    break;
                case 4:
                    //Trig is used to keep the velocity bound to a circle 
                    dashVelocityHorizontal = (float)(DASHPOWER * Math.cos(Math.toRadians(315)));
                    dashVelocityVertical = (float)(DASHPOWER * -Math.sin(Math.toRadians(315)));     
                    break;
                case 5:         
                    dashVelocityVertical = DASHPOWER;
                    break;
                case 6:      
                    //Trig is used to keep the velocity bound to a circle
                    dashVelocityHorizontal = (float)(DASHPOWER * Math.cos(Math.toRadians(225)));
                    dashVelocityVertical = (float)(DASHPOWER * -Math.sin(Math.toRadians(225)));    
                    break;
                case 7: 
                    dashVelocityHorizontal = -DASHPOWER;         
                    break;  
                case 8:
                    //Trig is used to keep the velocity bound to a circle 
                    dashVelocityHorizontal = (float)(DASHPOWER * Math.cos(Math.toRadians(135)));
                    dashVelocityVertical = (float)(DASHPOWER * -Math.sin(Math.toRadians(135)));      
                    break;  
                default:
                    break;
            }

            //Applies dash velocities to total velocity
            velocity.x = (int)dashVelocityHorizontal;
            velocity.y = (int)dashVelocityVertical;

            //End Dash
            if (currentDashTime <= 0){
                //Ends dashing
                dashing = false;
                currentDashCooldown = DASHCOOLDOWN;

                //Give a carry over force based on the direction dashed
                if (lastDirection == 1 || lastDirection == 2 || lastDirection == 8){yCarryoverMomentum = dashVelocityVertical / 2;} //Up
                if (lastDirection == 4 || lastDirection == 5 || lastDirection == 6){yCarryoverMomentum = dashVelocityVertical / 2;} //Down
                if (lastDirection == 6 || lastDirection == 7 || lastDirection == 8){xCarryoverMomentum = dashVelocityHorizontal/2;} //Left
                if (lastDirection == 2 || lastDirection == 3 || lastDirection == 4){xCarryoverMomentum = dashVelocityHorizontal/2;} //Right
            }
        }

        //Applies carryover momentum to velocity
        velocity.x += xCarryoverMomentum;
        velocity.y += yCarryoverMomentum;
        
        //Decreeses carryover momentum on the x axis
        if (xCarryoverMomentum > 0){xCarryoverMomentum = Math.max(xCarryoverMomentum - 0.03f, 0);
        }else{xCarryoverMomentum = Math.min(xCarryoverMomentum + 0.03f, 0);}

        //Decreeses carryover momentum on the y axis
        if (yCarryoverMomentum > 0){yCarryoverMomentum = Math.max(yCarryoverMomentum - 0.03f, 0);
        }else{yCarryoverMomentum = Math.min(yCarryoverMomentum + 0.03f, 0);}
    }

    //Applies horrizontal velocity
    public void horizontalMovment(){
        //Sets horizontal acceleration
        if (input.directionMap.get("Horizontal") != 0) {
            runSpeed = input.directionMap.get("Horizontal") == 1 ? Math.min(runSpeed + RUNACCELERATION, MAXRUNSPEED) : Math.max(runSpeed - RUNACCELERATION, -MAXRUNSPEED);
        }else{
            runSpeed = runSpeed > 0 ? Math.max(runSpeed - RUNDECELERATION, 0) : Math.min(runSpeed + RUNDECELERATION, 0);
        }
        
        //Applies horizontal velocity
        velocity.x += runSpeed;
    }

    //Applies jump velocity
    public void jump(){
        //If the player is on the ground reset kyodie time
        if (grounded){currentKyodieTime = KYODIETIME;}

        //The player can jump if they still have kyodie time and are not currently jumping
        canJump = (currentKyodieTime > 0) && !jumping;
    
        //If the player presses the jump key and can jump
        if (input.keyMap.get("Jump") && canJump){
            //Start jump
            jumping = true;
            currentKyodieTime = 0;
            accelerationDueToGravity = 0;

        }

        //If the player is currently jumping
        if (jumping){
            //If the player stops pressing jump terminate the jump earlt
            if (!input.keyMap.get("Jump")){
                jumping = false;
                jumpVelocity = 0;
                currentJumpTime = 0;
                accelerationDueToGravity = TERMINALVELOCITY / 2;

            }

            //Increment jump timer
            currentJumpTime += 1;

            //If the jump timer has not reached its max
            if (currentJumpTime < MAXJUMPLENGTH){
                //Apply jump force
                jumpVelocity = -JUMPPOWER;
            }else{
                //End the jump
                jumping = false;
                currentJumpTime = 0;
            }
        //If the player is not jumping
        }else{
            //Apply falloff jumping velocity
            jumpVelocity = Math.min(jumpVelocity += 0.1, 0);
        }

        //Decrement jump timer
        currentKyodieTime -=1;

        //Reset grounded check
        grounded = false;
    }

    //Gets player images based off of the idNumber
    private HashMap<String,BufferedImage> getPlayerImages(int idNum){
        HashMap<String,BufferedImage> imgSet = idNum == 1 ? ImageHandler.player1Images() : ImageHandler.player2Images();
        return imgSet;
    }

    //Sets the players image to the correct animation
    public BufferedImage animationHandler(){
        //Decrement blink timer
        blinkTimer -=1;

        //If the plater is in a stun flicker the player
        if ((currentStunTime > 20 || currentStunTime < 10) && currentStunTime > 0){return playerImages.get(null);}

        //If the player cant dash 
        if (!canDash){

            //If the player is being hit
            if (currentStunTime <= 0){

                //If the player is dashing
                if(dashing){
                    return playerImages.get("NDDash");
                }else if (blinkDuration > 0 && blinkTimer < 0){
                    //Animate blink
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

        //If the player can dash
        }else{

            //If the player is being hit
            if (currentStunTime <= 0){

                //If the player is dashing
                if(dashing){
                    return playerImages.get("Dash");
                }else if (blinkDuration > 0 && blinkTimer < 0){
                    //Animate blink
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

    //Draws the player
    public void drawPlayer(Graphics2D g2){ 
        
        //Render the trail
        renderTrail(g2);

        //Update the direction the player is facing
        facing = input.directionMap.get("Horizontal") != 0 ? input.directionMap.get("Horizontal") : facing;
        
        //Draw the player
        g2.drawImage(animationHandler(), 
        (int)(facing == 1 ? screenPos.x : screenPos.x + gamePanel.TILESIZE/2),  //x
        (int)screenPos.y, //y
        (int)(gamePanel.TILESIZE / 2) * facing, (int)gamePanel.TILESIZE / 2,null);

    }
}

