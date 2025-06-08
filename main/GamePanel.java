package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    TileManager tileManager = new TileManager(this);
    AudioHandler audioHandler = new AudioHandler();
    EndScreen endScreen = new EndScreen();

    Button[] buttons = {
        new Button(new Rectangle(125,500,100,100),tileManager.tileImages[3],"1") {
            @Override
            public void click() {
                if(menuState == "MAIN"){
                    menuState = "GAME";
                    tileManager.loadLevel("/main/LevelData/1.txt");
                    player.worldXPos = 129;
                    player.worldYPos = 129;
                    player2.worldXPos = 129;
                    player2.worldYPos = 129;
            }
            }
        },

        new Button(new Rectangle(325, 500, 100, 100),tileManager.tileImages[3],"2") {
            @Override
            public void click() {
                if (menuState == "MAIN"){
                    menuState = "GAME";
                    tileManager.loadLevel("/main/LevelData/2.txt");
                    player.worldXPos = 129;
                    player.worldYPos = 512;
                    player2.worldXPos = 129;
                    player2.worldYPos = 512;
                }
            }
        },

        new Button(new Rectangle(525, 500, 100, 100),tileManager.tileImages[3],"3") {
            @Override
            public void click() {
                if (menuState == "MAIN"){
                    menuState = "GAME";
                    System.out.println("click");
                    tileManager.loadLevel("/main/LevelData/3.txt");
                    player.worldXPos = 129;
                    player.worldYPos = 704;
                    player2.worldXPos = 129;
                    player2.worldYPos = 704;
                }
            }
                
        },

        new Button(new Rectangle(725, 500, 100, 100),tileManager.tileImages[3],"4") {
            @Override
            public void click() {
                if (menuState == "MAIN"){
                    menuState = "GAME";
                    tileManager.loadLevel("/main/LevelData/4.txt");
                    player.worldXPos = 129;
                    player.worldYPos = 1088;
                    player2.worldXPos = 129;
                    player2.worldYPos = 1088;
                }
            }
        },

        new Button(new Rectangle(925, 500, 100, 100),tileManager.tileImages[3],"5") {
            @Override
            public void click() {
                if (menuState == "MAIN"){
                    menuState = "GAME";
                    tileManager.loadLevel("/main/LevelData/5.txt");
                    player.worldXPos = 129;
                    player.worldYPos = 1856;
                    player2.worldXPos = 129;
                    player2.worldYPos = 1856;
                }
            }
        },

        new Button(new Rectangle(925, 650, 200, 100),tileManager.tileImages[3],"Exit") {
            @Override
            public void click() {
                if (menuState == "MAIN"){                    
                    window.setVisible(false);
                    window.dispose();
                    System.exit(0);
                    
                }
            }
        },
    };


    MenuManger menu = new MenuManger(buttons);

    public final int screenWidth = 1152; //18 tiles wide times 64 pixels per tile
    public final int screenHeight = 768; //18 tiles tall times 64 pixels per tile
    private final int FPS = 120;

    int[] endGoal = {0,0};
    int distanceToGoalP1= 100;
    int distanceToGoalP2=100;

    String menuState = "MAIN";

    // boolean inMenu = true;
    // boolean inEndScreen;
    int endScreenTimerCurrent;
    final int ENDSCREENTIMERMAX = 120;
    float spriteScale = 2;
    float tileSize = 64;
    float oldTileSize;
    double ratio = 1;
    //double cumulativeRatio = 1;

    final int CAMERAOFFSETY = 100;

    InputHandler inputP1 = new InputHandler(KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_SHIFT, this); 
    InputHandler inputP2 = new InputHandler(KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT, KeyEvent.VK_M, KeyEvent.VK_N, this); 

    Thread gameThread;

    public Player player = new Player(100,100,this, tileManager, inputP1, 1);
    public Player player2 = new Player(100, 100,this, tileManager, inputP2, 2);

    Player[] players = {player ,player2};

    float playerAverageX;
    float playerAverageY;
    JFrame window;
    CollisionChecker collisionChecker = new CollisionChecker(this);
    public GamePanel(JFrame window){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(inputP1);
        this.addKeyListener(inputP2);
        this.addMouseListener(menu);
        this.setFocusable(true);
        this.window = window;
        playMusic(0);
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
        if (menuState == "END"){
            if (endScreenTimerCurrent <= 0) {
                menuState = "MAIN";
            }
            endScreenTimerCurrent -= 1;

        }else if (menuState == "GAME"){
            distanceToGoalP1 = (int)((Math.sqrt(Math.pow(player.worldXPos - endGoal[0],2) + Math.pow(player.worldYPos - endGoal[1],2))) / tileSize);
            distanceToGoalP2 = (int)((Math.sqrt(Math.pow(player2.worldXPos - endGoal[0],2) + Math.pow(player2.worldYPos - endGoal[1],2))) / tileSize);

            if (distanceToGoalP1 < 1 || distanceToGoalP2 < 1 ){
                System.out.println(distanceToGoalP1 + " " +distanceToGoalP2);
                menuState = "END";
                endScreenTimerCurrent = ENDSCREENTIMERMAX;
                player = new Player(0,0,this, tileManager, inputP1, 1);
                player2 = new Player(0, 0,this, tileManager, inputP2, 2);
                players = new Player[]{player ,player2};
                playSoundEffect(3);
            }
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
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        switch (menuState) {
            case "MAIN":
                menu.draw(g2,this);
                break;

            case "END":
                endScreen.draw(g2, distanceToGoalP1, distanceToGoalP2, this);
                break;
        
            default:
                tileManager.draw(g2, playerAverageX, playerAverageY - CAMERAOFFSETY);
                player.drawPlayer(g2);
                player2.drawPlayer(g2);
                break;
        }

        g2.dispose(); //removes stored data
    }

    public void playMusic(int i){
        audioHandler.SetFile(i);
        audioHandler.play();
        audioHandler.loop();
    }

    public void stopMusic(){
        audioHandler.stop();
    }

    public void playSoundEffect(int i){
        audioHandler.SetFile(i);
        audioHandler.play();
    }

}