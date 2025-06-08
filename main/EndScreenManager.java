package main;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

public class EndScreenManager {
    public void draw(Graphics2D g2, int distanceToGoalP1, int distanceToGoalP2, GamePanel gamePanel){
        g2.drawImage(gamePanel.tileManager.tileImages[1], 0,0,gamePanel.SCREENWIDTH,gamePanel.SCREENHEIGHT,null);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("STIX Two Math", Font.BOLD, 64));

        if (distanceToGoalP1 < distanceToGoalP2){
            g2.drawString("Player 1 Wins", 390, 400);
        }else{
            g2.drawString("Player 2 Wins", 390, 400);
        }
    }
}
