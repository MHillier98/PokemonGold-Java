import greenfoot.*;

/**
 * The Start World for the game.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class StartWorld extends Worlds
{
    TextInfoBox menuBox = new TextInfoBox("StartMenu", "StartMenu", 0); //The menu box that covers the whole screen.

    /**
     * Constructs a new StartWorld.
     */
    public StartWorld()
    {
        setBackground(0);        
        addObject(menuBox, 240, 216);
        newMusic("Start");
    }
}