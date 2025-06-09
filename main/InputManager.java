package main;

//Imports needed for class
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class InputManager implements KeyListener {

    //Boolean for each direction
    public boolean up,down,left,right,space;

    //Key code for each key
    public int upKey,downKey,leftKey,rightKey,jumpKey,dashKey;

    //Sets each of the key codes
    public InputManager(int upKey, int downKey, int leftKey, int rightKey, int jumpKey, int dashKey){
        this.upKey = upKey;
        this.downKey = downKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.jumpKey = jumpKey;
        this.dashKey = dashKey;
    }

    //A hashmap with the state of each key
    public HashMap<String, Boolean> keyMap = new HashMap<String, Boolean>() {{
        put("Up", false);
        put("Down", false);
        put("Left", false);
        put("Right", false);
        put("Jump",false);
        put("Dash", false);
    }};

    //A hashmap with the direction the keys
    public HashMap<String, Integer> directionMap = new HashMap<String, Integer>() {{
        put("Vertical", 0);
        put("Horizontal", 0);
    }};

    //Updates the keys pressed
    @Override
    public void keyPressed(KeyEvent key) {
        //Get the key code of the pressed key
        int code = key.getKeyCode();

        //Update the state of each key
        keyMap.put("Up", code == upKey || keyMap.get("Up"));
        keyMap.put("Down", code == downKey || keyMap.get("Down"));
        keyMap.put("Left", code == leftKey || keyMap.get("Left"));
        keyMap.put("Right", code == rightKey || keyMap.get("Right"));
        keyMap.put("Jump", code == jumpKey || keyMap.get("Jump"));
        keyMap.put("Dash", code == dashKey || keyMap.get("Dash"));

        //Update the direction map
        updateDirectionMap();
    }

    //Updates the keys pressed when one is released
    @Override
    public void keyReleased(KeyEvent key) {
        //Get the key code of the pressed key
        int code = key.getKeyCode();

        //Update the state of each key
        keyMap.put("Up", code != upKey && keyMap.get("Up"));
        keyMap.put("Down", code != downKey && keyMap.get("Down"));
        keyMap.put("Left", code != leftKey && keyMap.get("Left"));
        keyMap.put("Right", code != rightKey && keyMap.get("Right"));
        keyMap.put("Jump", code != jumpKey && keyMap.get("Jump"));
        keyMap.put("Dash", code != dashKey && keyMap.get("Dash"));

        //Update the direction map
        updateDirectionMap();
    }

    //Updates the direciton map
    public void updateDirectionMap(){
        //If the player is holding up -1 if the player is holding down 1
        directionMap.put("Vertical", keyMap.get("Up") ? -1 : keyMap.get("Down") ? 1 : 0);

        //If the player is holding left -1 if the player is holding right 1
        directionMap.put("Horizontal", keyMap.get("Right") ? 1 : keyMap.get("Left") ? -1 : 0);
    }

    //Not used but required becuase of the KeyListener interface
    public void keyTyped(KeyEvent e) {}
}
