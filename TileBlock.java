import greenfoot.*;

/**
 * The TileBlock class, to allow tiles that cannot be passed through in the GameWorld.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class TileBlock extends Tiles
{
    private int tileX; //The X coordinate of the image.
    private int tileY; //The Y coordinate of the image.

    /**
     * Creates a new TileBlock.
     * @param inName The door's name.
     * @param inSheetX The image's X coordinate.
     * @param inSheetY The image's Y coordinate.
     * @param inType The Block's type.
     * @param tileSetNum The tileSet id. 
     * @param inWorldX The X coordinate in the world.
     * @param inWorldY The Y coordinate in the world.
     */
    public TileBlock(String inName, int inSheetX, int inSheetY, String inType, int tileSetNum, int inWorldX, int inWorldY)
    {
        super(inName, inSheetX, inSheetY, tileSetNum, inWorldX, inWorldY);
        this.type = inType;

        tileX = inWorldX;
        tileY = inWorldY;
    }

    /**
     * Called when added to the world.
     * 
     * Overrides the default addedToWorld(World world).
     * @param world The world it has been added to.
     */
    protected void addedToWorld(World world)
    {
        if(type.equals("Messenger"))
        {
            signAct();
        }
    }

    /**
     * Assigns a message to a sign.
     */
    public void signAct()
    {
        switch(tileX)
        {
            case 9:
            if(tileY == 56)
            {
                this.message = "TRAINER TIPS\nNo stealing other people's\nPOKéMON! POKé BALLS are to be\nthrown only at wild POKéMON!";
            }
            break;

            case 11:
            if(tileY == 16)
            {
                this.message = "VIOLET CITY POKEMON GYM\nLEADER: FALKNER\nThe Elegant Master of\nFlying POKEMON";
            }
            break;

            case 15:
            if(tileY == 78)
            {
                this.message = "ROUTE 30\nVIOLET CITY -\nCHERRYGROVE CITY";
            }
            break;

            case 19:
            if(tileY == 64)
            {
                this.message = "MR.POKéMON'S HOUSE:\nStraight Ahead!";
            }
            else if(tileY == 98)
            {
                this.message = "GUIDE GENT'S HOUSE";
            }
            break;

            case 20:
            if(tileY == 19)
            {
                this.message = "VIOLET CITY\nThe City of Nostalgic Scents";
            }
            break;

            case 21:
            if(tileY == 40)
            {
                this.message = "MR.POKEMON'S HOUSE";
            }
            break;

            case 23:
            if(tileY == 16)
            {
                this.message = "EARL'S POKEMON ACADEMY";
            }
            break;

            case 26:
            if(tileY == 97)
            {
                this.message = "CHERRYGROVE CITY\nThe City of Cute, Fragrant Flowers";
            }
            break;

            case 39:
            if(tileY == 94)
            {
                this.message = "Route 29\nCHERRYGROVE CITY -\nNEW BARK TOWN";
            }
            break;

            case 87:
            if(tileY == 96)
            {
                this.message = "Route 29\nCHERRYGROVE CITY -\nNEW BARK TOWN";
            }
            break;

            case 101:
            if(tileY == 92)
            {
                this.message = "ELM POKEMON LAB";
            }
            break;

            case 106:
            if(tileY == 97)
            {
                this.message = "NEW BARK TOWN\nThe Town Where the Winds of a New\nBeginning Blow";
            }
            break;

            case 107:
            if(tileY == 102)
            {
                this.message = "ELM'S HOUSE";
            }
            break;

            case 109:
            if(tileY == 94)
            {
                GameWorld gameWorld = (GameWorld) getWorld();
                this.message = gameWorld.getPlayerName() + "'s House";
            }
            break;
        }
    }

    /**
     * Returns the name of the TileBlock.
     */
    public String getName()
    {
        return name;
    }
}