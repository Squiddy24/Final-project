package main;

//Imports needed for class
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class ImageManager {

    public HashMap<String,BufferedImage> player1Images(){

        //Creates a hashmap to store player 1 images
        HashMap<String,BufferedImage> P1Images = new HashMap<String,BufferedImage>();

        //All images required for player 2
        try {
            P1Images.put("Base", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/BaseP1.png")));
            P1Images.put("Blink", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/BlinkP1.png")));
            P1Images.put("Dash", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/DashP1.png")));
            P1Images.put("Death", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/DeadP1.png")));
            P1Images.put("NDBase", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/NDBaseP1.png")));
            P1Images.put("NDBlink", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/NDBlinkP1.png")));
            P1Images.put("NDDash", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/NDDashP1.png")));
            P1Images.put("NDDeath", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/NDDeadP1.png")));

        } catch (Exception e) {
            System.out.println("Error Loading Images"); //Displays an error if an image cant be loaded
        }
        
        //returns the image set
        return P1Images;
    }

    public HashMap<String,BufferedImage> player2Images(){

        //Creates a hashmap to store player 2 images
        HashMap<String,BufferedImage> P1Images = new HashMap<String,BufferedImage>();

        //All images required for player 2
        try {
            P1Images.put("Base", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/BaseP2.png")));
            P1Images.put("Blink", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/BlinkP2.png")));
            P1Images.put("Dash", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/DashP2.png")));
            P1Images.put("Death", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/DeadP2.png")));
            P1Images.put("NDBase", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/NDBaseP2.png")));
            P1Images.put("NDBlink", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/NDBlinkP2.png")));
            P1Images.put("NDDash", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/NDDashP2.png")));
            P1Images.put("NDDeath", ImageIO.read(getClass().getResourceAsStream("/main/Images/PlayerSprites/NDDeadP2.png")));

        } catch (Exception e) {
            System.out.println("Error Loading Images"); //Displays an error if an image cant be loaded
        }
        
        //returns the image set
        return P1Images;
    }
}
