package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{

    public final int defaultWidth = 9;
    public final int defaultHeight = 6;

    public final int screenWidth = 1152; //screenTileWidth * tileSize; // 1152
    public final int screenHeight = 768; //screenTileHeight * tileSize; // 768

    int[] endGoal = {0,0};
    int FPS = 120;

    float spriteScale = 2;
    float tileSize = screenWidth / (defaultWidth * spriteScale); // 64
    float oldTileSize;
    double ratio = 1;
    //double cumulativeRatio = 1;

    final int CAMERAOFFSETY = 100;

    InputHandler inputP1 = new InputHandler(KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_SHIFT, this); 
    InputHandler inputP2 = new InputHandler(KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT, KeyEvent.VK_M, KeyEvent.VK_N, this); 

    Thread gameThread;

    TileManager tileManager = new TileManager(this);

    public Player player = new Player(0,0,this, tileManager, inputP1, 1);
    public Player player2 = new Player(0, 0,this, tileManager, inputP2, 2);

    Player[] players = {player ,player2};

    float playerAverageX;
    float playerAverageY;
        
    CollisionChecker collisionChecker = new CollisionChecker(this,tileManager.levelTiles);
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(inputP1);
        this.addKeyListener(inputP2);
        this.setFocusable(true);
    }

    public void initializeGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        double drawInterval = 1000000000/FPS; //nano seconds to seconds
        double deltaTime = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null){ //Gameloop
            currentTime = System.nanoTime();
            deltaTime += (currentTime - lastTime) /drawInterval; //clock stuff
            lastTime = currentTime;

            if (deltaTime > 1){
                update();
                repaint(); // calls paintComponent
                deltaTime--;
            }


        }
    }

    public void update(){

        int distanceToGoal = (int)((Math.sqrt(Math.pow(player.worldXPos - endGoal[0],2) + Math.pow(player.worldYPos - endGoal[1],2))) / tileSize);
        System.out.println( distanceToGoal);
        //Finds the adverage of players
        playerAverageX = tileSize;
        playerAverageY = tileSize;

        for (Player player : players) {
           playerAverageX += player.worldXPos;
           playerAverageY += player.worldYPos;
        }

        playerAverageX /= players.length;
        playerAverageY /= players.length;

        player.update(playerAverageX, playerAverageY + CAMERAOFFSETY);
        player2.update(playerAverageX, playerAverageY + CAMERAOFFSETY);


    }


    public void zoom(float zoomFactor){
        oldTileSize = tileSize;
        spriteScale += zoomFactor;
        tileSize = screenWidth / (defaultWidth * spriteScale);

        ratio = (double)tileSize/oldTileSize;
        //cumulativeRatio *= ratio;

        //TODO
        player.worldXPos *= ratio;
        player.worldYPos *= ratio;
        player2.worldXPos *= ratio;
        player2.worldYPos *= ratio;

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileManager.draw(g2, playerAverageX, playerAverageY - CAMERAOFFSETY);
        //g2.setColor(Color.RED);

        player.drawPlayer(g2);
        //g2.setColor(Color.BLUE);

        player2.drawPlayer(g2);
        //g2.setColor(Color.GREEN);


        //TODO DEBUG
        // for (int i = 0; i < collisionChecker.levelTiles[0].length; i++) {
        //     for (int j = 0; j < collisionChecker.levelTiles.length; j++) {
        //         if (collisionChecker.levelTiles[j][i].collision == true){
        //         g2.drawRect((int)collisionChecker.levelTiles[j][i].pos[0], (int)collisionChecker.levelTiles[j][i].pos[1], 64, 64);
        //         }
        //     }
        // }
        g2.dispose(); //removes stored data
    }

}