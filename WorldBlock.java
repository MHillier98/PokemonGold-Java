import greenfoot.*;

/**
 * The WorldBlock class to make solid blocks in the GameWorld.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class WorldBlock extends Tiles
{
    private String blockName; //The name of the block

    /**
     * Creates a new WorldBlock.
     * @param inBlockName The name of the block.
     * @param tileSetNum The tileSet id.
     * @param inWorldX The X coordinate in the world.
     * @param inWorldY The Y coordinate in the world.
     */
    public WorldBlock(String inBlockName, int tileSetNum, int inWorldX, int inWorldY)
    {
        super(inBlockName, 46, 0, tileSetNum, inWorldX, inWorldY);
        blockName = inBlockName;
    }

    /**
     * Returns the name of the block.
     */
    public String getBlockName()
    {
        return blockName;
    }
}