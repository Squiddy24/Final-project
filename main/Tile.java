package main;

//Imports needed for class
import java.awt.image.BufferedImage;

public class Tile {
    //The image of the tile
    public BufferedImage image;

    //The collision and damage effects of the tile
    public boolean collision = false;
    public boolean damage = false;

    //The position of the tile
    public float[] pos;

    //Constructor that sets values
    Tile(float[] pos, BufferedImage image){
        this.pos = pos;
        this.image = image;
    }


}
