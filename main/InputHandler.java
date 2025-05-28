package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class InputHandler implements KeyListener{
    public boolean up,down,left,right;
    public int upKey,downKey,leftKey,rightKey;
    public GamePanel panel;

    public InputHandler(int upKey, int downKey, int leftKey, int rightKey, GamePanel panel){
        this.upKey = upKey;
        this.downKey = downKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.panel = panel;
    }
    public HashMap<String, Boolean> keyMap = new HashMap<String, Boolean>() {{
        put("Up", false);
        put("Down", false);
        put("Left", false);
        put("Right", false);
    }};

    public HashMap<String, Integer> directionMap = new HashMap<String, Integer>() {{
        put("Vertical", 0);
        put("Horizontal", 0);
    }};

    @Override
    public void keyPressed(KeyEvent key) {
        int code = key.getKeyCode();

        keyMap.put("Up", code == upKey || keyMap.get("Up"));
        keyMap.put("Down", code == downKey || keyMap.get("Down"));
        keyMap.put("Left", code == leftKey || keyMap.get("Left"));
        keyMap.put("Right", code == rightKey || keyMap.get("Right"));

        if (code == KeyEvent.VK_EQUALS) {
            panel.zoom((float) -0.1);
        }
        if (code == KeyEvent.VK_MINUS) {
            panel.zoom((float)0.1);
        }

        updateDirectionMap();
    }

    @Override
    public void keyReleased(KeyEvent key) {
        int code = key.getKeyCode();

        keyMap.put("Up", code != upKey && keyMap.get("Up"));
        keyMap.put("Down", code != downKey && keyMap.get("Down"));
        keyMap.put("Left", code != leftKey && keyMap.get("Left"));
        keyMap.put("Right", code != rightKey && keyMap.get("Right"));

        updateDirectionMap();
    }

    public void updateDirectionMap(){
        directionMap.put("Vertical", keyMap.get("Up") ? -1 : keyMap.get("Down") ? 1 : 0);
        directionMap.put("Horizontal", keyMap.get("Right") ? 1 : keyMap.get("Left") ? -1 : 0);
    }

    @Override
    public void keyTyped(KeyEvent e) { //Not used
    }
    
}
