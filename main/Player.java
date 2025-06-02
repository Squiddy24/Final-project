package main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    public final int zoomPadding = 200;

    double worldXPos;
    double worldYPos;

    double hitboxXPos;
    double hitboxYPos;

    int speed = 4;

    GamePanel gamePanel;
    InputHandler input;

    float screenX;
    float screenY;

    int playerScreenX;
    int playerScreenY;
    int facing = 1;

    double zoomFactor;
    boolean collided = false;

    //Rectangle hitbox = new Rectangle(6 * gamePanel.spriteScale, 12 * gamePanel.spriteScale, 20 * gamePanel.spriteScale, 20 * gamePanel.spriteScale);
    Rectangle hitbox;
    public BufferedImage base, blink, dash, dead, dashlessBase, dashlessBlink, dashlessDash, dashlessDead;

    //animation stuff to clean up TODO
    int blinkTimer = 240;
    int blinkDuration = 30;

    public Player(int startX, int startY, GamePanel gamePanel, TileManager tileManager, InputHandler input){
        this.gamePanel = gamePanel;
        this.input = input;

        worldXPos = startX;
        worldYPos = startY;

        hitboxXPos = startX;
        hitboxYPos = startY;

        screenX = gamePanel.screenWidth/2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight/2 - (gamePanel.tileSize / 2);

        zoomFactor = (gamePanel.tileSize / 64.0);

        hitbox = new Rectangle((int)worldXPos, (int)worldYPos, 44, 44);
        getPlayerImages();
    }

    public void update(float playerAverageX, float playerAverageY){
        float scaledSpeed = gamePanel.tileSize / (4*speed);

        
        //Updates Hitbox
        //hitbox.setRect((int)worldXPos, (int)worldYPos, (int)gamePanel.tileSize, (int)gamePanel.tileSize);

        
        worldYPos += input.directionMap.get("Vertical") * scaledSpeed;
        worldXPos += input.directionMap.get("Horizontal") * scaledSpeed;

        hitboxYPos += input.directionMap.get("Vertical") * speed;
        hitboxXPos += input.directionMap.get("Horizontal") * speed;

        playerScreenX = (int)(playerAverageX - worldXPos + screenX);
        playerScreenY = (int)(playerAverageY - worldYPos + screenY);
        
        float dynamicZoom;

        hitbox.setRect((double)(hitboxXPos) + 10, (double)(hitboxYPos) + 10,44,44);

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
        
        gamePanel.collisionChecker.checkTile(this, gamePanel);

    }

    public void getPlayerImages(){
        try {
            //Loads images
            base = ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/Base.png"));
            blink = ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/Blink.png"));
            dash = ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/Dash.png"));
            dead = ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/Dead.png"));
            dashlessBase = ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/DashlessBase.png"));
            dashlessBlink = ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/DashlessBlink.png"));
            dashlessDash = ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/DashlessDash.png"));
            dashlessDead = ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/DashlessDead.png"));

        } catch (IOException e) {
            System.out.println("FILE NOT FOUND");
        }
    }

    public BufferedImage animationHandler(){
        if (blinkTimer <= 0){
            blinkDuration -=1;
            if (blinkDuration <= 0){
                blinkTimer = 240;
                blinkDuration = 30;
            }
            return blink;

        }
        blinkTimer -= 1;

        return base;
    }

    public void drawPlayer(Graphics2D g2, InputHandler otherInput){ 
        facing = otherInput.directionMap.get("Horizontal") != 0 ? otherInput.directionMap.get("Horizontal") : facing;
        g2.drawImage(animationHandler(), 
        (int)(facing == 1 ? playerScreenX : playerScreenX + gamePanel.tileSize),  //x
        (int)playerScreenY, //y
        (int)gamePanel.tileSize * facing, (int)gamePanel.tileSize,null);
        
        g2.drawRect(hitbox.x,hitbox.y,hitbox.width,hitbox.height);

        g2.setColor(Color.CYAN);
        g2.drawRect((int)hitboxXPos + 10, (int)hitboxYPos + 10, 44, 44);



    }
}

