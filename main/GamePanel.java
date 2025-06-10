package main;

//Imports needed for class
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.print.DocFlavor.STRING;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Font;

public class GamePanel extends JPanel implements Runnable{
    //Managers
    public TileManager tileManager = new TileManager(this); //Responsible for rendering the game tiles
    public AudioManager audioHandler = new AudioManager(); //Responsible playing audio
    public EndScreenManager endScreen = new EndScreenManager(); //Responsible for rendering the end screen
    public MenuManager menu = new MenuManager(setButtons()); //Responsible for rendering the start screen
    public CollisionChecker collisionChecker = new CollisionChecker(); //Responsible for checking collisions

    //Settings of the screen
    private final Dimension SCREENDIMENSIONS = new Dimension(1152, 768); //18 by 12 tiles times 64 pixels per tile
    public final int TILESIZE = 64;
    private final int CAMERAOFFSETY = 100;
    private final int FPS = 120;

    //Position of the end goal and the distance each player is to it
    public int[] endGoal = {0,0};
    private int distanceToGoalP1 = 100;
    private int distanceToGoalP2 =100;

    //The state of the game
    private String menuState = "MAIN";

    //The timer of the end screen
    private final int ENDSCREENTIMERMAX = 120;
    private int endScreenTimerCurrent;
    int time;

    //The input managers for the players
    private InputManager inputP1 = new InputManager(KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_SHIFT); 
    private InputManager inputP2 = new InputManager(KeyEvent.VK_I,KeyEvent.VK_K,KeyEvent.VK_J,KeyEvent.VK_L, KeyEvent.VK_CLOSE_BRACKET, KeyEvent.VK_BACK_SLASH); 

    //The two players
    public Player player1 = new Player(this, inputP1, 1);
    public Player player2 = new Player(this, inputP2, 2);
    public Point playerAverage = new Point(0,0);

    //The thread the game will run on so it can be parallel to other process
    private Thread gameThread;

    //The window the game panel sits on
    private JFrame window;

    //Constructor of the gamepanel
    public GamePanel(JFrame window){
        //Settings
        this.setPreferredSize(new Dimension((int)SCREENDIMENSIONS.getWidth(),(int)SCREENDIMENSIONS.getHeight()));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        //Enables keyboard and mouse input
        this.addKeyListener(inputP1);
        this.addKeyListener(inputP2);
        this.addMouseListener(menu);
        this.window = window;
        
        //Starts the main theme
        playMusic(0);
    }

    //Starts the thread
    public void initializeGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    //The tread runs this every frame
    @Override
    public void run(){
        double drawInterval = 1000000000/FPS; //nano seconds to seconds
        double deltaTime = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        //While the tread still exist
        while (gameThread != null){
            //Sets the game to a consistant 60 FPS
            currentTime = System.nanoTime();
            deltaTime += (currentTime - lastTime) /drawInterval;
            lastTime = currentTime;

            if (deltaTime > 1){
                update(); // Updates the game panel
                repaint(); // calls paintComponent
                deltaTime--;
            }


        }
    }

    //Checks if a player has reached the end of the level
    public void checkForEndOfLevel(){
        //Finds the distance of both players to the goal
        distanceToGoalP1 = (int)((Math.sqrt(Math.pow(player1.worldPos.x - endGoal[0],2) + Math.pow(player1.worldPos.y - endGoal[1],2))) / TILESIZE);
        distanceToGoalP2 = (int)((Math.sqrt(Math.pow(player2.worldPos.x - endGoal[0],2) + Math.pow(player2.worldPos.y - endGoal[1],2))) / TILESIZE);

        //If eather player is less than 1 tile away from the goal
        if (distanceToGoalP1 < 1 || distanceToGoalP2 < 1 ){
            //Display the ending screen
            menuState = "END";

            //Start the end screen timer
            endScreenTimerCurrent = ENDSCREENTIMERMAX;

            //Play a completion sound effect
            playSoundEffect(3);
        }
    }

    //Loads the buttons for the main menu
    public Button[] setButtons(){
        return new Button[]{
                new Button(new Rectangle(125,500,100,100),tileManager.tileImages[3],"1") {
                    @Override
                    public void click() {
                        //Loads level 1
                        if(menuState == "MAIN"){
                            menuState = "GAME";
                            tileManager.loadLevel("/main/LevelData/1.txt");
                            player1.worldPos = new Point(129,129);
                            player2.worldPos = new Point(129,129);
                        }
                    }
                },

                new Button(new Rectangle(325, 500, 100, 100),tileManager.tileImages[3],"2") {
                    @Override
                    public void click() {
                        if (menuState == "MAIN"){
                            //Loads level 2
                            menuState = "GAME";
                            tileManager.loadLevel("/main/LevelData/2.txt");
                            player1.worldPos = new Point(129,512);
                            player2.worldPos = new Point(129,512);
                        }
                    }
                },

                new Button(new Rectangle(525, 500, 100, 100),tileManager.tileImages[3],"3") {
                    @Override
                    public void click() {
                        if (menuState == "MAIN"){
                            //Loads level 3
                            menuState = "GAME";
                            tileManager.loadLevel("/main/LevelData/3.txt");
                            player1.worldPos = new Point(129,704);
                            player2.worldPos = new Point(129,704);
                        }
                    }
                        
                },

                new Button(new Rectangle(725, 500, 100, 100),tileManager.tileImages[3],"4") {
                    @Override
                    public void click() {
                        if (menuState == "MAIN"){
                            //Loads level 4
                            menuState = "GAME";
                            tileManager.loadLevel("/main/LevelData/4.txt");
                            player1.worldPos = new Point(129,1088);
                            player2.worldPos = new Point(129,1088);
                        }
                    }
                },

                new Button(new Rectangle(925, 500, 100, 100),tileManager.tileImages[3],"5") {
                    @Override
                    public void click() {
                        if (menuState == "MAIN"){
                            //Loads level 5
                            menuState = "GAME";
                            tileManager.loadLevel("/main/LevelData/5.txt");
                            player1.worldPos = new Point(129,1856);
                            player2.worldPos = new Point(129,1856);
                        }
                    }
                },

                new Button(new Rectangle(925, 650, 200, 100),tileManager.tileImages[3],"Exit") {
                    @Override
                    public void click() {
                        if (menuState == "MAIN"){  
                            //Exits the game                  
                            window.setVisible(false);
                            window.dispose();
                            System.exit(0);
                            
                        }
                    }
                },
            };
    }
    
    //Finds the adverage of players
    public Point getPlayerAverage(){
        //Get the distance between players
        int distanceBetweenPlayers = (int)((Math.sqrt(Math.pow(player1.worldPos.x - player2.worldPos.x,2) + Math.pow(player1.worldPos.y - player2.worldPos.y,2))));
        
        //If the distance is greater than the width of the screen
        if (distanceBetweenPlayers > SCREENDIMENSIONS.getWidth()){
            
            
            //Set the camera to which ever player is closer to the goal
            if (distanceToGoalP1 < distanceToGoalP2){
                return player1.worldPos;
            }else{
                return player2.worldPos;
            }
        }else{
            //Else set the camera to the average position of the two players
            return new Point((int)((player1.worldPos.x + player2.worldPos.x + TILESIZE) / 2),
                             (int)((player1.worldPos.y + player2.worldPos.y + TILESIZE) / 2));
        }
    }

    //Updates the ending timer
    private void tickEndTimer(){
        if (endScreenTimerCurrent <= 0) {
            //If the timer reaches 0 set the state to main menu
            menuState = "MAIN";
            time = 0;
            //Reset both players
            player1 = new Player(this, inputP1, 1);
            player2 = new Player(this, inputP2, 2);
        }
            endScreenTimerCurrent -= 1;
    }

    //Updates all elements of the game
    public void update(){
        if (menuState == "END"){
           tickEndTimer(); //Ticks the end timer
        }else if (menuState == "GAME"){
            checkForEndOfLevel(); //Checks if either player has reached the goal
            playerAverage = getPlayerAverage(); //Finds the average of both players

            //Updates both players
            player1.update(playerAverage.x, playerAverage.y + CAMERAOFFSETY);
            player2.update(playerAverage.x, playerAverage.y + CAMERAOFFSETY);
            
        }
    }

    //Draws all elements of the game
    public void paintComponent(Graphics g){
        //Casts graphics into graphics2d
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Draws the current state
        switch (menuState) {
            case "MAIN":
                menu.draw(g2,this, SCREENDIMENSIONS); //Main menu
                break;

            case "END":
                endScreen.draw(g2, distanceToGoalP1, distanceToGoalP2, this, SCREENDIMENSIONS); //End screen
                break;
        
            default:
                //main game

                tileManager.draw(g2, playerAverage.x, playerAverage.y - CAMERAOFFSETY); //Level tiles
                player1.drawPlayer(g2); //player 1
                player2.drawPlayer(g2); //player 2

                //Draws the timer
                drawTimer(g2);
                
                break;
        }

        g2.dispose(); //Removes unused stored data
    }

    //Displays a timer of the level
    public void drawTimer(Graphics2D g2){
        //Updates the timer
        time ++;

        //Renders the text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("STIX Two Math", Font.BOLD, 32));
        g2.drawString("TIME: " + time / 120 + ":" + String.format("%03d", (int)(time - (Math.floor(time/120) * 120))), 20,50);

    }

    //Plays the main theme
    public void playMusic(int i){
        audioHandler.SetFile(i);
        audioHandler.play();
        audioHandler.loop();
    }

    //Plays a given sound effect
    public void playSoundEffect(int i){
        audioHandler.SetFile(i);
        audioHandler.play();
    }

}