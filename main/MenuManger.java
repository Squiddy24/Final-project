package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MenuManger implements MouseListener {

    Button[] buttons;
    public MenuManger(Button[] buttons){
        this.buttons = buttons;
    }

    public void update(){

    }
    public void draw(Graphics2D g2, GamePanel gamePanel){
        
        g2.drawImage(gamePanel.tileManager.tileImages[1], 0,0,gamePanel.screenWidth,gamePanel.screenHeight,null);
        g2.drawImage(gamePanel.tileManager.tileImages[0], 100,-100,124 * 8, 64 * 8,null);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("STIX Two Math", Font.BOLD, 64));
        g2.drawString("SELECT A LEVEL", 340, 400);

        g2.setColor(Color.RED);
        for (Button button : buttons) {
            button.draw(g2);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent mouse) {
        // TODO Auto-generated method stub
        Point point = mouse.getPoint();
        for (Button button : buttons) {
            if (button.box.contains(point)){
                button.click();
            }
        }
    }
}
