import greenfoot.*;

/**
 * The DoorBlock class, to make doors in the overworld.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class DoorBlock extends Tiles
{
    private String coords; //The coordinates of the Door.

    /**
     * Creates a new Door.
     * @param id The door's ID.
     * @param imageX The image's X coordinate.
     * @param imageY The image's Y coordinate.
     * @param inWorldX The X coordinate in the world.
     * @param inWorldY The Y coordinate in the world.
     */
    public DoorBlock(String id, int imageX, int imageY, int inWorldX, int inWorldY)
    {
        super("Door", imageX, imageY, 1, inWorldX, inWorldY);
        coords = id;
    }

    /**
     * Returns the door's coordinates.
     */
    public String getCoords() 
    {
        return coords;
    }
}