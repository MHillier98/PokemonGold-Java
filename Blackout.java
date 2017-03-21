import greenfoot.*;

/**
 * The Blackout class, to cover the screen during world transitions.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class Blackout extends Overworld
{
    private String type = "Default"; //The type of blackout

    /**
     * Creates a new blackout to cover the screen.
     * @param inType The type of blackout.
     */
    public Blackout(String inType)
    {
        setImage(new GreenfootImage(480, 432));
        type = inType;
    }

    /**
     * Called when added to the world.
     * 
     * Overrides the default addedToWorld(World world).
     * @param world The world it has been added to.
     */
    @Override
    public void addedToWorld(World world) 
    {
        GreenfootImage blackout = new GreenfootImage(480, 432);
        GreenfootImage blackoutImg = new GreenfootImage("Blackout_" + type + ".png");
        for(int b = 0; b < (blackoutImg.getWidth() / 480); b++)
        {
            blackout.drawImage(blackoutImg, b * -480, 0);
            setImage(blackout);
            Greenfoot.delay(1);
        }
    }
}