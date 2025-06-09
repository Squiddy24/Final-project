package main;

//Imports needed for class
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class EndScreenManager {

    //Draws the end screen
    public void draw(Graphics2D g2, int distanceToGoalP1, int distanceToGoalP2, GamePanel gamePanel, Dimension screenDimension ){
        g2.drawImage(gamePanel.tileManager.tileImages[1], 0, 0, (int)screenDimension.getWidth(), (int)screenDimension.getHeight(), null);

        //Renders the text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("STIX Two Math", Font.BOLD, 64));

        System.out.println(distanceToGoalP1 + " " + distanceToGoalP2);
        //Changes the text based on which player reached the goal
        if (distanceToGoalP1 < distanceToGoalP2){
            g2.drawString("Player 1 Wins", 390, 400);
        }else{
            g2.drawString("Player 2 Wins", 390, 400);
        }
    }
}
