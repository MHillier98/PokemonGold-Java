import greenfoot.*;

/**
 * The Mover class, to both organise movers in the GameWorld, as well as provide some common variables between all movers.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class Movers extends Overworld
{
    protected String type = ""; //The type of mover
    protected String message = ""; //The mover's message
    protected String function = ""; //The mover's function
    protected int id = 0; //The mover's id
    
    /**
     * Returns the mover's type.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Returns the mover's message.
     */
    public String getMsg()
    {
        return message;
    }
    
    /**
     * Returns the mover's function.
     */
    public String getFunction()
    {
        return function;
    }
    
    /**
     * Returns the mover's id.
     */
    public int getID()
    {
        return id;
    }
}