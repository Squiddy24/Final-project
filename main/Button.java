package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public abstract class Button {
    Rectangle box;
    BufferedImage image;
    String text;

    public Button(Rectangle box, BufferedImage image, String text){
        this.box = box;
        this.image = image;
        this.text = text;
    }

    public abstract void click();

    public void draw(Graphics2D g2){
        g2.drawImage(image, box.x,box.y,(int)box.getWidth(),(int)box.getHeight(),null);
        //g2.setColor(new Color(60, 50, 62));
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("STIX Two Math", Font.BOLD, 64));
        g2.drawString(text, (int)((box.x + box.getWidth() / 3) - 5 * text.length()), (int)(box.y + box.getHeight() / 1.5) + 5);
        //g2.fillRect(box.x, box.y, (int)box.getWidth(), (int)box.getHeight());
    }
}
