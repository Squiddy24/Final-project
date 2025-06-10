package main;

//Imports needed for class
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MenuManager implements MouseListener {

    //Sets the list of buttons
    Button[] buttons;

    public MenuManager(Button[] buttons){
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
        g2.drawString("SELECT A LEVEL", 320, 400);

        //Displays the controlls for player 1
        g2.setColor(new Color(135,229,163));
        g2.setFont(new Font("STIX Two Math", Font.PLAIN, 32));
        g2.drawString("Player 1", 120, 375);
        g2.drawString("WASD to Move", 70, 405);
        g2.drawString("Space to Jump", 80, 435);
        g2.drawString("Shift to Dash", 90, 465);

        //Displays the controlls for player 2
        g2.setColor(new Color(229,135,135));
        g2.setFont(new Font("STIX Two Math", Font.PLAIN, 32));
        g2.drawString("Player 2", 920, 375);
        g2.drawString("IJKL to Move", 870, 405);
        g2.drawString("} to Jump", 900, 435);
        g2.drawString("\\ to Dash", 905, 465);





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
