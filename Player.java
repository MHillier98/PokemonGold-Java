import greenfoot.*;

/**
 * The Player class, to make a player in the GameWorld.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class Player extends Overworld
{
    String name = ""; //The player's name

    private String lastDir = "Down"; //The player's last direction
    private int dir = 1; //A direction counter

    GreenfootImage mainImage = new GreenfootImage("Player_Sheet.png"); //The player's image
    private int imgCounter = 1; //An image counter
    private final int INIT_IMG_COUNTER = imgCounter; //The initial image counter

    /**
     * Creates a new Player.
     * @param name The Player's name.
     * @param startDir The Player's starting direction.
     */
    public Player(String name, String startDir)
    {
        this.name = name;
        lastDir = startDir;
        
        int startDirNum;
        switch(startDir)
        {
            case "Up": startDirNum = 0;
            break;
            case "Down": startDirNum = -48;
            break;
            case "Left": startDirNum = -96;
            break;
            case "Right": startDirNum = -144;
            break;
            default: startDirNum = -48;
            break;
        }

        GreenfootImage currentImg = new GreenfootImage(48, 48);
        currentImg.drawImage(mainImage, startDirNum, 0);
        setImage(currentImg);
    }

    /**
     * The act method of the Player.
     * 
     * <p> This makes it:
     * <p> -Update the image
     * <p> -Check for control inputs
     * <p> -Check for doors
     */
    public void act() 
    {
        if(((GameWorld) getWorld()).getIsActing())
        {
            updateImage();
            controls();
            doorCheck();
        }
    }

    /**
     * Updates the Player's image.
     */
    public void updateImage()
    {
        this.getImage().clear();
        if(Greenfoot.isKeyDown("W"))
        {
            dir = 0;
            lastDir = "Up";
            animateImage();
        }
        else if(Greenfoot.isKeyDown("S"))
        {
            dir = 1;
            lastDir = "Down";
            animateImage();
        }
        else if(Greenfoot.isKeyDown("A"))
        {
            dir = 2;
            lastDir = "Left";
            animateImage();
        }
        else if(Greenfoot.isKeyDown("D"))
        {
            dir = 3;
            lastDir = "Right";
            animateImage();
        }
        else
        {
            GreenfootImage currentImage = this.getImage();
            currentImage.drawImage(mainImage, dir * -48, 0);
            setImage(currentImage);
        }
    }

    /**
     * This animates the player.
     */
    public void animateImage()
    {
        GreenfootImage currentImage = this.getImage();
        currentImage.clear();

        if(imgCounter > 0)
        {
            imgCounter--;
            currentImage.drawImage(mainImage, dir * -48, 0);
        }
        else
        {
            imgCounter = INIT_IMG_COUNTER;
            currentImage.drawImage(mainImage, (4 + dir) * -48, 0);
        }
    }

    /**
     * Checks for control inputs.
     */
    public void controls()
    {
        if(Greenfoot.isKeyDown("K"))
        {
            GameWorld gameWorld = (GameWorld) getWorld();
            if(lastDir.equals("Up"))
            {
                if(isObjectAbove(TileBlock.class) == true)
                {
                    TileBlock tileBlock = (TileBlock) getOneObjectAtOffset(0, -48, TileBlock.class);
                    tileBlockText(tileBlock);
                }
                else if(isObjectAbove(NPC.class) == true)
                {
                    NPC npc = (NPC) getOneObjectAtOffset(0, -48, NPC.class);
                    npcText(npc);
                }
            }
            else if(lastDir.equals("Down"))
            {
                if(isObjectBelow(TileBlock.class) == true)
                {
                    TileBlock tileBlock = (TileBlock) getOneObjectAtOffset(0, 48, TileBlock.class);
                    tileBlockText(tileBlock);
                }
                else if(isObjectBelow(NPC.class) == true)
                {
                    NPC npc = (NPC) getOneObjectAtOffset(0, 48, NPC.class);
                    npcText(npc);
                }
            }
            else if(lastDir.equals("Left"))
            {
                if(isObjectLeft(TileBlock.class) == true)
                {
                    TileBlock tileBlock = (TileBlock) getOneObjectAtOffset(-48, 0, TileBlock.class);
                    tileBlockText(tileBlock);
                }
                else if(isObjectLeft(NPC.class) == true)
                {
                    NPC npc = (NPC) getOneObjectAtOffset(-48, 0, NPC.class);
                    npcText(npc);
                }
            }
            else if(lastDir.equals("Right"))
            {
                if(isObjectRight(TileBlock.class) == true)
                {
                    TileBlock tileBlock = (TileBlock) getOneObjectAtOffset(48, 0, TileBlock.class);
                    tileBlockText(tileBlock);
                }
                else if(isObjectRight(NPC.class) == true)
                {
                    NPC npc = (NPC) getOneObjectAtOffset(48, 0, NPC.class);
                    npcText(npc);
                }
            }
        }
    }

    /**
     * Shows text of TileBlocks.
     */
    public void tileBlockText(TileBlock tileBlock)
    {
        GameWorld gameWorld = (GameWorld) getWorld();
        switch(tileBlock.getType())
        {
            case "PokeMartSign": gameWorld.showText("For All Your POKéMON Needs\nPOKéMON MART");
            break;
            case "PokeCenterSign": gameWorld.showText("Heal Your POKéMON!\nPOKéMON CENTER");
            break;
            case "Messenger": gameWorld.showText(tileBlock.getMsg());
            break;
        }
    }

    /**
     * Shows text of NPC's.
     */
    public void npcText(NPC npc)
    {
        GameWorld gameWorld = (GameWorld) getWorld();
        gameWorld.showText(npc.getMsg());
        String tempFunc = npc.getFunction();
        if(tempFunc.equals("Heal"))
        {
            gameWorld.healPokemon();
        }
        else if(tempFunc.equals("Battle"))
        {
            int id = npc.getID();
            gameWorld.blackout("Spiral");
            gameWorld.saveWorld();
            gameWorld.newBattle(id);
        }
    }

    /**
     * Checks to see if the player is standing on a door.
     */
    public void doorCheck()
    {
        if(isObjectOn(DoorBlock.class) == true)
        {
            DoorBlock doorBlock = (DoorBlock) getOneObjectAtOffset(0, 0, DoorBlock.class);
            String doorCoords = doorBlock.getCoords();
            String[] doorCoordsArr = doorCoords.split("_");

            int doorX = Integer.parseInt(doorCoordsArr[0]);
            int doorY = Integer.parseInt(doorCoordsArr[1]);

            GameWorld gameWorld = (GameWorld) getWorld();
            String worldArea = gameWorld.getWorldName();

            if(worldArea.equals("World_Full"))
            {
                switch(doorX)
                {
                    case 5:
                    if(doorY == 16)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_Pokemart2", 4, 7, "Up"));
                    }
                    break;

                    case 14:
                    if(doorY == 16)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_Violet_Gym", 5, 15, "Up"));
                    }
                    break;

                    case 19:
                    if(doorY == 92)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_Pokemart1", 4, 7, "Up"));
                    }
                    break;

                    case 21:
                    if(doorY == 98)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_Cherrygrove_GuideHouse", 3, 7, "Up"));
                    }
                    break;

                    case 23:
                    if(doorY == 40)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_MrPkmn_House", 3, 7, "Up"));
                    }
                    break;

                    case 25:
                    if(doorY == 92)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_Pokecenter1", 4, 7, "Up"));
                    }
                    break;

                    case 26:
                    if(doorY == 16)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_Violet_School", 4, 15, "Up"));
                    }
                    break;

                    case 27:
                    if(doorY == 24)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_Pokecenter2", 4, 7, "Up"));
                    }
                    break;

                    case 104:
                    if(doorY == 92)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_NewBarkTown_ElmLab", 5, 12, "Up"));
                    }
                    break;

                    case 109:
                    if(doorY == 102)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_NewBarkTown_ElmHouse", 3, 8, "Up"));
                    }
                    break;

                    case 111: 
                    if(doorY == 94)
                    {
                        fadeOut();
                        Greenfoot.setWorld(new GameWorld("World_NewBarkTown_PlayerHouse1", 7, 8, "Up"));
                    }
                    break;
                }
            }
            else
            {
                switch(worldArea)
                {
                    case "World_NewBarkTown_PlayerHouse1":
                    switch(doorY)
                    {
                        case 1: 
                        if(doorX == 10)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_NewBarkTown_PlayerHouse2", 8, 2, "Down"));
                        }
                        break;

                        case 9: 
                        if(doorX == 7 || doorX == 8)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 111, 95, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_NewBarkTown_PlayerHouse2":
                    switch(doorX)
                    {
                        case 8: 
                        if(doorY == 1)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_NewBarkTown_PlayerHouse1", 10, 2, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_NewBarkTown_ElmLab":
                    switch(doorY)
                    {
                        case 13: 
                        if(doorX == 5 || doorX == 6)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 104, 93, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_NewBarkTown_ElmHouse":
                    switch(doorY)
                    {
                        case 9: 
                        if(doorX == 3 || doorX == 4)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 109, 103, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_Cherrygrove_GuideHouse":
                    switch(doorY)
                    {
                        case 8: 
                        if(doorX == 3 || doorX == 4)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 21, 99, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_Pokecenter1":
                    switch(doorY)
                    {
                        case 8: 
                        if(doorX == 4 || doorX == 5)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 25, 93, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_Pokecenter2":
                    switch(doorY)
                    {
                        case 8: 
                        if(doorX == 4 || doorX == 5)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 27, 25, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_Pokemart1":
                    switch(doorY)
                    {
                        case 8: 
                        if(doorX == 3 || doorX == 4)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 19, 93, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_Pokemart2":
                    switch(doorY)
                    {
                        case 8: 
                        if(doorX == 3 || doorX == 4)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 5, 17, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_MrPkmn_House":
                    switch(doorY)
                    {
                        case 8: 
                        if(doorX == 3 || doorX == 4)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 23, 41, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_Violet_School":
                    switch(doorY)
                    {
                        case 16: 
                        if(doorX == 4 || doorX == 5)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 26, 17, "Down"));
                        }
                        break;
                    }
                    break;

                    case "World_Violet_Gym":
                    switch(doorY)
                    {
                        case 16: 
                        if(doorX == 5 || doorX == 6)
                        {
                            fadeOut();
                            Greenfoot.setWorld(new GameWorld("World_Full", 14, 17, "Down"));
                        }
                        break;
                    }
                    break;
                }
            }
        }
    }

    /**
     * Fades the world out.
     */
    public void fadeOut()
    {
        GameWorld gameWorld = (GameWorld) getWorld();
        gameWorld.stopMusic();
        gameWorld.blackout("Fade");
    }

    /**
     * Sets the direction of the player.
     * @param direction The new direction.
     */
    public void setDirection(String direction)
    {
        int dirNum = 0;
        switch(direction)
        {
            case "Up": dirNum = 0;
            dir = 0;
            break;
            case "Down": dirNum = -48;
            dir = 1;
            break;
            case "Left": dirNum = -96;
            dir = 2;
            break;
            case "Right": dirNum = -144;
            dir = 3;
            break;
        }
        lastDir = direction;

        GreenfootImage currentImg = new GreenfootImage(48, 48);
        currentImg.drawImage(mainImage, dirNum, 0);
        setImage(currentImg);
    }

    /**
     * Sets the last direction of the player.
     * @param newDir The new direction of the player
     */
    public void setLastDir(String newDir)
    {
        lastDir = newDir;
    }

    /**
     * Sets the last direction number of the player.
     * @param newDirNum The new direction counter of the player
     */
    public void setDirNum(int newDirNum)
    {
        dir = newDirNum;
    }

    /**
     * Returns the last direction of the player.
     */
    public String getLastDirection()
    {
        return lastDir;
    }

    /**
     * Returns the last name of the player.
     */
    public String getName()
    {
        return name;
    }
}