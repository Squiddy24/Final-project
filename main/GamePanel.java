package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    //Managers
    TileManager tileManager = new TileManager(this);
    AudioManager audioHandler = new AudioManager();
    EndScreenManager endScreen = new EndScreenManager();
    MenuManger menu = new MenuManger(setButtons());

    public final int SCREENWIDTH = 1152; //18 tiles wide times 64 pixels per tile
    public final int SCREENHEIGHT = 768; //18 tiles tall times 64 pixels per tile
    public final int TILESIZE = 64;
    private final int CAMERAOFFSETY = 100;
    private final int FPS = 120;

    int[] endGoal = {0,0};
    int distanceToGoalP1 = 100;
    int distanceToGoalP2 =100;

    String menuState = "MAIN";
    int endScreenTimerCurrent;
    final int ENDSCREENTIMERMAX = 120;


    private InputManager inputP1 = new InputManager(KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_SHIFT, this); 
    private InputManager inputP2 = new InputManager(KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT, KeyEvent.VK_M, KeyEvent.VK_N, this); 

    Thread gameThread;

    public Player player = new Player(this, tileManager, inputP1, 1);
    public Player player2 = new Player(this, tileManager, inputP2, 2);
    public Point playerAverage = new Point(0,0);

    JFrame window;
    CollisionChecker collisionChecker = new CollisionChecker(this);
    public GamePanel(JFrame window){
        this.setPreferredSize(new Dimension(SCREENWIDTH,SCREENHEIGHT));
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

    public void checkForEndOfLevel(){
        int distanceToGoalP1 = (int)((Math.sqrt(Math.pow(player.worldPos.x - endGoal[0],2) + Math.pow(player.worldPos.y - endGoal[1],2))) / TILESIZE);
        int distanceToGoalP2 = (int)((Math.sqrt(Math.pow(player2.worldPos.x - endGoal[0],2) + Math.pow(player2.worldPos.y - endGoal[1],2))) / TILESIZE);

        if (distanceToGoalP1 < 1 || distanceToGoalP2 < 1 ){
            System.out.println(distanceToGoalP1 + " " +distanceToGoalP2);
            menuState = "END";
            endScreenTimerCurrent = ENDSCREENTIMERMAX;
            player = new Player(this, tileManager, inputP1, 1);
            player2 = new Player(this, tileManager, inputP2, 2);
            playSoundEffect(3);
        }
    }
    public Button[] setButtons(){
        return new Button[]{
                new Button(new Rectangle(125,500,100,100),tileManager.tileImages[3],"1") {
                    @Override
                    public void click() {
                        if(menuState == "MAIN"){
                            menuState = "GAME";
                            tileManager.loadLevel("/main/LevelData/1.txt");
                            player.worldPos = new Point(129,129);
                            player2.worldPos = new Point(129,129);
                        }
                    }
                },

                new Button(new Rectangle(325, 500, 100, 100),tileManager.tileImages[3],"2") {
                    @Override
                    public void click() {
                        if (menuState == "MAIN"){
                            menuState = "GAME";
                            tileManager.loadLevel("/main/LevelData/2.txt");
                            player.worldPos = new Point(129,512);
                            player2.worldPos = new Point(129,512);
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
                            player.worldPos = new Point(129,704);
                            player2.worldPos = new Point(129,704);
                        }
                    }
                        
                },

                new Button(new Rectangle(725, 500, 100, 100),tileManager.tileImages[3],"4") {
                    @Override
                    public void click() {
                        if (menuState == "MAIN"){
                            menuState = "GAME";
                            tileManager.loadLevel("/main/LevelData/4.txt");
                            player.worldPos = new Point(129,1088);
                            player2.worldPos = new Point(129,1088);
                        }
                    }
                },

                new Button(new Rectangle(925, 500, 100, 100),tileManager.tileImages[3],"5") {
                    @Override
                    public void click() {
                        if (menuState == "MAIN"){
                            menuState = "GAME";
                            tileManager.loadLevel("/main/LevelData/5.txt");
                            player.worldPos = new Point(129,1856);
                            player2.worldPos = new Point(129,1856);
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
    }
    
    public Point getPlayerAverage(){
        //Finds the adverage of players
        return new Point((int)((player.worldPos.x + player2.worldPos.x + TILESIZE) / 2),
                         (int)((player.worldPos.y + player2.worldPos.y + TILESIZE) / 2));
    }

    public void update(){
        if (menuState == "END"){
            if (endScreenTimerCurrent <= 0) {
                menuState = "MAIN";
            }
            endScreenTimerCurrent -= 1;

        }else if (menuState == "GAME"){
            checkForEndOfLevel();
            playerAverage = getPlayerAverage();
            player.update(playerAverage.x, playerAverage.y + CAMERAOFFSETY);
            player2.update(playerAverage.x, playerAverage.y + CAMERAOFFSETY);
            
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
                tileManager.draw(g2, playerAverage.x, playerAverage.y - CAMERAOFFSETY);
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