package main;

//Imports needed for class
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public abstract class Button {
    //The dimentions of the button
    Rectangle box;

    //The image the button is drawn with
    BufferedImage image;

    //The text on the button
    String text;

    public Button(Rectangle box, BufferedImage image, String text){
        this.box = box;
        this.image = image;
        this.text = text;
    }

    //The abstract clicking method
    public abstract void click();


    public void draw(Graphics2D g2){
        //Draws the image of the button
        g2.drawImage(image, box.x,box.y,(int)box.getWidth(),(int)box.getHeight(),null);

        //Displays the text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("STIX Two Math", Font.BOLD, 64));
        g2.drawString(text, (int)((box.x + box.getWidth() / 3) - 5 * text.length()), (int)(box.y + box.getHeight() / 1.5) + 5);
    }
}
