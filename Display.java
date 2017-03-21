import greenfoot.*;

/**
 * The Display class, to display instructions to the player when they first start the game.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class Display extends Overworld
{
    Integer displayNum = 0;

    /**
     * Simply displays the instruction screen.
     */
    public Display()
    {
        setImage("Help.png");
    }

    /**
     * Counts up, and then removes itself.
     */
    public void act() 
    {
        if(displayNum < 40)
        {
            Greenfoot.delay(1);
            displayNum++;
        }
        else
        {
            getWorld().removeObject(this);
        }
    }
}