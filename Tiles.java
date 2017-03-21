import greenfoot.*;

/**
 * The Tiles class, to allow tiles to walk on in the GameWorld.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class Tiles extends Movers
{
    protected String name; //The Tile's name
    protected int worldX; //The Tile's X coordinate
    protected int worldY; //The Tile's Y coordinate

    /**
     * Creates a new Tile.
     * @param inName The name of the tile
     * @param sheetX The image's X coordinate.
     * @param sheetY The image's Y coordinate.
     * @param tileSetNum The tileSet id.
     * @param inWorldX The X coordinate in the world.
     * @param inWorldY The Y coordinate in the world.
     */
    public Tiles(String inName, int sheetX, int sheetY, int tileSetNum, int inWorldX, int inWorldY)
    {
        name = inName;
        GreenfootImage currentImage = new GreenfootImage(48, 48);
        currentImage.drawImage(new GreenfootImage("TileSet" + tileSetNum + ".png"), sheetX * -48, sheetY * -48);
        setImage(currentImage);

        worldX = inWorldX;
        worldY = inWorldY;
    }

    /**
     * Returns the name of the Tile.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the X coordinate of the Tile.
     */
    public int getWorldX()
    {
        return worldX;
    }

    /**
     * Returns the Y coordinate of the Tile.
     */
    public int getWorldY()
    {
        return worldY;
    }
}