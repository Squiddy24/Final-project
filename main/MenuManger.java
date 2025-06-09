package main;

//Imports needed for class
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MenuManger implements MouseListener {

    //Sets the list of buttons
    Button[] buttons;

    public MenuManger(Button[] buttons){
        this.buttons = buttons;
    }

    //Draws the menu
    public void draw(Graphics2D g2, GamePanel gamePanel, Dimension screenDimension){
        //Draws the background
        g2.drawImage(gamePanel.tileManager.tileImages[1], 0, 0, (int)screenDimension.getWidth(), (int)screenDimension.getHeight(),null);

        //Draws the games title
        g2.drawImage(gamePanel.tileManager.tileImages[0], 100,-100,124 * 8, 64 * 8,null);

        //Draws the text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("STIX Two Math", Font.BOLD, 64));
        g2.drawString("SELECT A LEVEL", 340, 400);

        //Draws each button
        for (Button button : buttons) {
            button.draw(g2);
        }
    }
    
    //Clicks the button when the mouse is released
    @Override
    public void mouseReleased(MouseEvent mouse) {
        //Gets the point the mouse was released from
        Point point = mouse.getPoint();

        //Checks if that point is within any button
        for (Button button : buttons) {
            if (button.box.contains(point)){
                //Click that button
                button.click();
            }
        }
    }

    //Not used but required becuase of the MouseListener interface
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
}
