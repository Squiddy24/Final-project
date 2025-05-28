package main;
import java.awt.image.BufferedImage;

public class Tile {
    
    public BufferedImage image;
    public boolean collision = false;
    public float[] pos;

    Tile(float[] pos, BufferedImage image){
        this.pos = pos;
        this.image = image;
    }


}
