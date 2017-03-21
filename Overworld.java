import greenfoot.*;

/**
 * The OverWorld class, to allow some common methods to all objects in the GameWorld.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class Overworld extends Actor
{
    /**
     * Returns if there is an object of a certain class at the same location or not.
     * @param cls The class to check for.
     */
    public boolean isObjectOn(Class cls)
    {
        if(getOneObjectAtOffset(0, 0, cls) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * Returns if there is an object of a certain class above or not.
     * @param cls The class to check for.
     */
    public boolean isObjectAbove(Class cls)
    {
        if(getOneObjectAtOffset(0, -48, cls) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns if there is an object of a certain class below or not.
     * @param cls The class to check for.
     */
    public boolean isObjectBelow(Class cls)
    {
        if(getOneObjectAtOffset(0, 48, cls) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns if there is an object of a certain class left or not.
     * @param cls The class to check for.
     */
    public boolean isObjectLeft(Class cls)
    {
        if(getOneObjectAtOffset(-48, 0, cls) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns if there is an object of a certain class right or not.
     * @param cls The class to check for.
     */
    public boolean isObjectRight(Class cls)
    {
        if(getOneObjectAtOffset(47, 0, cls) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}