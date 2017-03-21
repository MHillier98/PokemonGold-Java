import greenfoot.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The NPC class, to allow NPC's in the GameWorld.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class NPC extends Movers
{
    /**
     * Possible directions:
     * -1 = Move
     *  0 = Wait
     *  1 = Look Up
     *  2 = Look Down
     *  3 = Look Left
     *  4 = Look Right
     */
    private int[] directions = {}; //An array of directions and movements
    private int dirCounter = 0; //A counter to go through the directions
    private String lastDir = "Down"; //The last direction of the NPC

    private int imageX = 0; //The image X coordinate of the NPC

    private int moveCounter = 0; //A counter to go through the movements

    private int imgCounter = 1; //A counter for the image
    private int INIT_IMG_COUNTER = imgCounter; //The initial image counter

    GreenfootImage mainImage = new GreenfootImage("NPC_Sprites.png"); //The image of the NPC

    /**
     * Creates a new NPC.
     * @param imageID The NPC's image id.
     * @param directionsID The NPC's direction id.
     * @param npcMessage The NPC's message.
     */
    public NPC(int imageID, int directionsID, String npcMessage)
    {
        npcInit(imageID, directionsID, npcMessage);
    }

    /**
     * Creates an NPC with a function.
     * @param imageID The NPC's image id.
     * @param directionsID The NPC's direction id.
     * @param npcMessage The NPC's message.
     * @param newFunction The NPC's function.
     * @param newID The NPC's ID.
     */
    public NPC(int imageID, int directionsID, String npcMessage, String newFunction, int newID)
    {
        npcInit(imageID, directionsID, npcMessage);

        function = newFunction;
        id = newID;
    }

    /**
     * Initialises the NPC.
     * @param imageID The NPC's image id.
     * @param directionsID The NPC's direction id.
     * @param npcMessage The NPC's message.
     */
    public void npcInit(int imageID, int directionsID, String npcMessage)
    {
        try
        {
            List<String> list = Files.readAllLines(Paths.get("npcDirections.txt"), StandardCharsets.UTF_8);
            String[] newDirectionsArr1 = list.toArray(new String[list.size()]);
            String newDirectionsStr = newDirectionsArr1[directionsID];
            String[] newDirectionsArr2 = newDirectionsStr.split("\\s*,\\s*");

            int[] newDirections = new int[newDirectionsArr2.length];
            for(int i = 0; i < newDirections.length; i++)
            {
                newDirections[i] = Integer.parseInt(newDirectionsArr2[i]);
            }

            this.directions = newDirections;
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }

        GreenfootImage currentImg = new GreenfootImage(48, 48);
        imageX = imageID * -192;

        int currentX = 0;
        switch(directions[0])
        {
            case 1: lastDir = "Up";
            currentX = 3;
            break;

            case 2: lastDir = "Down";
            currentX = 0;
            break;

            case 3: lastDir = "Left";
            currentX = 2;
            break;

            case 4: lastDir = "Right";
            currentX = 1;
            break;
        }
        currentImg.drawImage(mainImage, imageX + (currentX * -48), 0);
        setImage(currentImg);

        type = "Messenger";
        message = npcMessage;
    }

    /**
     * The act method of the NPC.
     * 
     * This makes it move and animate.
     */
    public void act() 
    {
        if(moveCounter < directions.length * 2)
        {
            if(moveCounter % 2 == 0)
            {
                if(dirCounter < directions.length)
                {
                    switch(directions[dirCounter])
                    {
                        case -1:
                        if(lastDir.equals("Up") && !isObjectAbove(TileBlock.class) && !isObjectAbove(Player.class))
                        {
                            setLocation(getX(), getY() - 24);
                        }
                        else if(lastDir.equals("Down") && !isObjectBelow(TileBlock.class) && !isObjectBelow(Player.class))
                        {
                            setLocation(getX(), getY() + 24);
                        }
                        else if(lastDir.equals("Left") && !isObjectLeft(TileBlock.class) && !isObjectLeft(Player.class))
                        {
                            setLocation(getX() - 24, getY());
                        }
                        else if(lastDir.equals("Right") && !isObjectRight(TileBlock.class) && !isObjectRight(Player.class))
                        {
                            setLocation(getX() + 24, getY());
                        }
                        updateImage();
                        break;

                        case 1: lastDir = "Up";
                        break;

                        case 2: lastDir = "Down";
                        break;

                        case 3: lastDir = "Left";
                        break;

                        case 4: lastDir = "Right";
                        break;
                    }

                    dirCounter++;
                }
            }
            moveCounter++;
        }
        else
        {
            moveCounter = 0;
            dirCounter = 0;
        }
    }

    /**
     * Updates the NPC's image.
     */
    public void updateImage()
    {
        GreenfootImage currentImage = this.getImage();
        currentImage.clear();

        int currentX = 0;

        switch(lastDir)
        {
            case "Down":
            currentX = 0;
            break;

            case "Right":
            currentX = 1;
            break;

            case "Left":
            currentX = 2;
            break;

            case "Up":
            currentX = 3;
            break;

            default:
            currentX = 0;
            break;
        }

        if(imgCounter > 0)
        {
            imgCounter--;
            currentImage.drawImage(mainImage, imageX + (currentX * -48), 0);
        }
        else
        {
            imgCounter = INIT_IMG_COUNTER;
            currentImage.drawImage(mainImage, imageX + (currentX * -48), -48);
        }
    }
}