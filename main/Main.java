/* 
*   Aidan McLeod
*   ICS4U Final Assignment
*   June 9 2025
*   Allows two users to play a racing game against eachother
*/

package main;

//Imports needed for class
import javax.swing.JFrame;

public class Main{
    public static void main(String[] args) {
        //Creats the window the Game Panel will sit on
        JFrame window = new JFrame();

        //Sets the settings of the window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Higher Velocity Hijinks");

        //Creates the screen the game will be rendered onto
        GamePanel gamePanel = new GamePanel(window);
        window.add(gamePanel);
        window.pack();

        //Renders the window
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        //Starts the game thread
        gamePanel.initializeGameThread();
    }
}