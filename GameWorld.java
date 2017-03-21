import greenfoot.*;
import java.io.FileWriter;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The overworld of the game.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class GameWorld extends Worlds
{
    //Default world variables
    private String worldName = "World_Full";
    GreenfootImage imageMap = new GreenfootImage(worldName + ".png");
    GreenfootImage npcMap = new GreenfootImage("NPC_" + worldName + ".png");
    private String areaName = "";

    //The starting offset of the objects in the world
    private int worldOffsetX = 0;
    private int worldOffsetY = 0;

    //The current offset of the objects in the world
    private int currentWorldOffsetX = 0;
    private int currentWorldOffsetY = 0;

    //Default player variables
    private String playerName = "GOLD";
    private String playerDir = "Down";
    Player player = new Player(playerName, playerDir);

    //The coordinates of the player in the world
    private int playerX = 0;
    private int playerY = 0;

    //If keys are being pressed
    private boolean keyCheckingI = false;
    private boolean keyCheckingO = false;

    private boolean randomPokemon = false; //If the user is in tall grass
    private boolean isActing = true; //If the user can do anything
    TextInfoBox menuBox = null; //The in-game menu

    /**
     * Constructs a GameWorld from saved data.
     */
    public GameWorld()
    {
        worldName = Reader.readStringFromFile("GameInformation", 0, 1);
        imageMap = new GreenfootImage(worldName + ".png");
        npcMap = new GreenfootImage("NPC_" + worldName + ".png");

        int worldX = Reader.readIntFromFile("GameInformation", 0, 2);
        int worldY = Reader.readIntFromFile("GameInformation", 0, 3);

        currentWorldOffsetX = worldX;
        currentWorldOffsetY = worldY;

        int currentOffsetX = worldX - 4;
        int currentOffsetY = worldY - 4;

        worldOffsetX = -48 * currentOffsetX;
        worldOffsetY = -48 * currentOffsetY;

        int xLoc = worldOffsetX + 24 + worldX * 48;
        int yLoc = worldOffsetY + 24 + worldY * 48;

        playerName = Reader.readStringFromFile("GameInformation", 0, 0);
        playerDir = "Down";
        player = new Player(playerName, playerDir);
        addObject(player, xLoc, yLoc);

        gameWorldSetup();
    }

    /**
     * Constructs a GameWorld from passed variables.
     * @param inWorldName The name of the new world.
     * @param worldX The X coordinate of the new world.
     * @param worldY The Y coordinate of the new world.
     * @param newPlayerDir The direction of the player.
     */
    public GameWorld(String inWorldName, int worldX, int worldY, String newPlayerDir)
    {
        worldName = inWorldName;
        imageMap = new GreenfootImage(worldName + ".png");
        npcMap = new GreenfootImage("NPC_" + worldName + ".png");

        currentWorldOffsetX = worldX;
        currentWorldOffsetY = worldY;

        int currentOffsetX = worldX - 4;
        int currentOffsetY = worldY - 4;

        worldOffsetX = -48 * currentOffsetX;
        worldOffsetY = -48 * currentOffsetY;

        int xLoc = worldOffsetX + 24 + worldX * 48;
        int yLoc = worldOffsetY + 24 + worldY * 48;

        playerDir = newPlayerDir;
        player = new Player(playerName, playerDir);
        addObject(player, xLoc, yLoc);
        player.setDirection(playerDir);

        gameWorldSetup();
    }

    /**
     * Constructs a new GameWorld, overwriting previously saved data.
     * @param newPlayerName The name of the new player.
     */
    public GameWorld(String newPlayerName)
    {
        worldName = "World_NewBarkTown_PlayerHouse2";
        imageMap = new GreenfootImage(worldName + ".png");
        npcMap = new GreenfootImage("NPC_" + worldName + ".png");

        int worldX = 4;
        int worldY = 4;

        currentWorldOffsetX = worldX;
        currentWorldOffsetY = worldY;

        int currentOffsetX = worldX - 4;
        int currentOffsetY = worldY - 4;

        worldOffsetX = -48 * currentOffsetX;
        worldOffsetY = -48 * currentOffsetY;

        int xLoc = worldOffsetX + 24 + worldX * 48;
        int yLoc = worldOffsetY + 24 + worldY * 48;

        playerName = newPlayerName;
        playerDir = "Down";
        player = new Player(playerName, playerDir);
        addObject(player, xLoc, yLoc);
        player.setDirection(playerDir);

        healPokemon();
        gameWorldSetup();
        
        addObject(new Display(), getWidth() / 2, getHeight() / 2);
    }

    /**
     * Sets up the new world.
     * 
     * <p> Adds:
     * <p> -NPC's
     * <p> -world tiles
     * <p> -act and paint order
     * <p> -saves the world
     */
    public void gameWorldSetup()
    {
        setBackground(0);
        npcInit();
        initWorld();
        setup();
        saveWorld();
    }

    /**
     * Adds NPC's to the world.
     * 
     * Runs off RGB colour values.
     */
    public void npcInit()
    {
        for(int xCoord = 0; xCoord < npcMap.getWidth(); xCoord++)
        {
            for(int yCoord = 0; yCoord < npcMap.getHeight(); yCoord++)
            {
                int xLoc = worldOffsetX + 24 + xCoord * 48;
                int yLoc = worldOffsetY + 24 + yCoord * 48;

                switch(npcMap.getColorAt(xCoord, yCoord).getRGB())
                {
                    case -65536: addObject(new NPC(8, 2, "......\nSo this is the famous ELM POKéMON\nLAB...\n...What are you staring at?"), xLoc, yLoc);
                    break;
                    case -256: addObject(new NPC(0, 3, "Oh! Your POKéMON is adorable!\nI wish I had one!"), xLoc, yLoc);
                    break;
                    case -65281: addObject(new NPC(1, 4, "Yo, " + playerName + "!\nI hear PROF.ELM discovered some\nnew POKéMON."), xLoc, yLoc);
                    break;

                    case -16735512: addObject(new NPC(0, 1, "Oh, " + playerName +"! Our neighbour, PROF.\nElm, was looking for you.\nHave a rest!", "Heal", 0), xLoc, yLoc);
                    break;

                    case -10240: addObject(new NPC(5, 1, "I'm the PkMn Professor!\n I need special instructions. . ."), xLoc, yLoc);
                    break;
                    case -16753017: addObject(new NPC(3, 2, "There are only two of us, so\nwe're always busy."), xLoc, yLoc);
                    break;

                    case -16711681: addObject(new NPC(0, 1, "Hi, " + playerName + "! My husband's always\nso busy- I hope he's OK.\nWhen he's caught up in his POKéMON\nresearch, he even forgets\nto eat."), xLoc, yLoc);
                    break;
                    case -16776961: addObject(new NPC(7, 1, "When I grow up, I'm going to help\nmy Dad!\nI'm going to be a great POKéMON\nprofessor!"), xLoc, yLoc);
                    break;

                    case -16711936: addObject(new NPC(2, 1, "POKéMON hide in the grass. Who\nknows when they'll pop out..."), xLoc, yLoc);
                    break;
                    case -14503604: addObject(new NPC(1, 5, "Yo. How are your POKéMON?\nIf they're weak and not ready for\nbattle, keep out of the grass."), xLoc, yLoc);
                    break;
                    case -4856291: addObject(new NPC(2, 3, "See those ledges? It's scary\nto jump off them.\nBut you can go to NEW BARK without\nwalking through the grass."), xLoc, yLoc);
                    break;
                    case -8590058: addObject(new NPC(1, 1, "I'm waiting for a special POKéMON!"), xLoc, yLoc);
                    break;

                    case -12566464: addObject(new NPC(5, 1, "You're a rookie trainer, aren't\nyou? I can tell!\nThat's OK! Everyone is a\nrookie at some point!\nI'll teach you a few things!\nPOKéCENTERS.\nThey heal your POKéMON in no\ntime at all.\nYou'll be relying on them a\nlot, so you better learn\nabout them.\nPOKéMART.\nThey sell BALLS for catching wild\nPOKéMON and other useful\nitems.\nROUTE 30 is out to the north.\nTrainers will be battling their\nprized POKéMON there."), xLoc, yLoc);
                    break;
                    case -1055568: addObject(new NPC(0, 3, "Did you talk to the old man by\nthe POKéMON CENTER?"), xLoc, yLoc);
                    break;
                    case -7753709: addObject(new NPC(7, 3, "MR.POKéMON's house is still\nfarther up ahead."), xLoc, yLoc);
                    break;

                    case -10805473: addObject(new NPC(1, 3, "Everyone's having fun\nbattling!\nYou should too!"), xLoc, yLoc);
                    break;

                    case -48577: addObject(new NPC(4, 1, "Becoming a good trainer is really\ntough.\nI'm going to battle lots of\npeople to get better.", "Battle", 1), xLoc, yLoc);
                    break;
                    case -12433153: addObject(new NPC(3, 1, "Instead of finding bug\nPOKéMON, I found a trainer!", "Battle", 2), xLoc, yLoc);
                    break;
                    case -4440000: addObject(new NPC(5, 1, "FALKNER, from the VIOLET POKéMON\nGYM, is a fine trainer!\nHe inherited his father's gym and\nhas done a great job with it."), xLoc, yLoc);
                    break;

                    case -46331: addObject(new NPC(9, 7, "Pokemon Center Lady"), xLoc, yLoc);
                    break;
                    case -288247: addObject(new NPC(9, 8, "Your POKéMON have been healed.\nHave a nice day!", "Heal", 0), xLoc, yLoc);
                    break;

                    case -12255243: addObject(new NPC(9, 2, "Pokemon Mart Lady"), xLoc, yLoc);
                    break;

                    case -15434059: addObject(new NPC(7, 6, "I'm taking notes of the teacher's\nlecture.\nI'd better copy the stuff on the\nblackboard too."), xLoc, yLoc);
                    break;
                    case -15830422: addObject(new NPC(7, 6, "It sure is tough taking notes..."), xLoc, yLoc);
                    break;

                    case -8651008: addObject(new NPC(6, 7, "I'm FALKNER, the VIOLET POKéMON GYM\n leader!\nPeople say you can clip\nflying-type\nPOKéMON's wings with a jolt of\nelectricity...\nI won't allow such insults to\nbird POKéMON!\nI'll show you the real power of the\nmagnificent bird POKéMON!", "Battle", 5), xLoc, yLoc);
                    break;
                    case -18944: addObject(new NPC(7, 8, "They keyword is guts!\nThose here are training night and\nday to become bird POKéMON\nmasters. Come on!", "Battle", 4), xLoc, yLoc);
                    break;
                    case -3342592: addObject(new NPC(7, 2, "Let me see if you are good enough \nto face FALKNER!", "Battle", 3), xLoc, yLoc);
                    break;
                    case -12386321: addObject(new NPC(3, 7, "Hey! I'm no trainer but I can\ngive some advice!\nBelieve me! If you believe, a\nchampionship dream can come\ntrue.\nYou believe? Then listen.\nThe grass-type is weak against\nthe flying-type.\nKeep this in mind."), xLoc, yLoc);
                    break;

                    case -55611: addObject(new NPC(1, 1, "Null"), xLoc, yLoc);
                    break;
                }
            }
        }
    }

    /**
     * Adds NPC's to the world.
     * 
     * Runs off RGB colour values.
     * ColorCodes:
     * -1        | White
     * -16777216 | Black
     * -65536    | Red
     * -16711936 | Green
     * -16776961 | Blue
     * -256      | Yellow
     * -16711681 | Cyan
     * -65281    | Magenta
     */
    public void initWorld()
    {
        for(int xCoord = 0; xCoord < imageMap.getWidth(); xCoord++)
        {
            for(int yCoord = 0; yCoord < imageMap.getHeight(); yCoord++)
            {
                int xLoc = worldOffsetX + 24 + xCoord * 48;
                int yLoc = worldOffsetY + 24 + yCoord * 48;

                switch(imageMap.getColorAt(xCoord, yCoord).getRGB())
                {
                    case -16777216: addObject(new TileBlock("Void", 0, 7, "Void", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -16711936: addObject(new TileBlock("Tree_Top", 49, 2, "Block", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -14503604: addObject(new TileBlock("Tree_Bottom", 49, 3, "Block", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -8590058: addObject(new TileBlock("Small_Tree", 48, 1, "Block", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -7753709: addObject(new Tiles("TallGrass", 46, 1, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -1055568: addObject(new Tiles("Grass1", 46, 0, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4856291: addObject(new Tiles("Grass2", 45, 1, 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -683299: addObject(new Tiles("PinkFlowers1", 40, 0, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -20791: addObject(new Tiles("PinkFlowers2", 40, 1, 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -15503599: addObject(new TileBlock("GreenRoofTile0", 45, 26, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -14033371: addObject(new TileBlock("GreenRoofTile1", 46, 26, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -15035882: addObject(new TileBlock("GreenRoofTile2", 47, 26, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -14221512: addObject(new TileBlock("GreenRoofTile3", 49, 26, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -14549215: addObject(new TileBlock("GreenRoofTile4", 49, 27, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -12714118: addObject(new TileBlock("GreenRoofTile5", 48, 26, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -12714179: addObject(new TileBlock("GreenRoofTile6", 48, 27, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -12452056: addObject(new TileBlock("GreenRoofTile7", 50, 26, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -10420363: addObject(new TileBlock("GreenRoofTile8", 50, 27, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -3584: addObject(new TileBlock("Wall0", 45, 11, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -256: addObject(new TileBlock("Wall1", 46, 11, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7936: addObject(new TileBlock("Wall2", 47, 11, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -39655: addObject(new TileBlock("Wall_3", 46, 10, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2464498: addObject(new TileBlock("Wall_4", 45, 10, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -20214: addObject(new TileBlock("Wall_Window0", 45, 12, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -25273: addObject(new TileBlock("Wall_Window1", 46, 12, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -1080253: addObject(new TileBlock("Wall_Window2", 47, 12, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -3385583: addObject(new TileBlock("Wall_Window2", 48, 12, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -10240: addObject(new DoorBlock(xCoord + "_" + yCoord, 50, 9, xCoord, yCoord), xLoc, yLoc); //Default door
                    break;
                    case -3638001: addObject(new DoorBlock(xCoord + "_" + yCoord, 7, 1, xCoord, yCoord), xLoc, yLoc); //Player home level 1
                    break;
                    case -7450610: addObject(new DoorBlock(xCoord + "_" + yCoord, 8, 5, xCoord, yCoord), xLoc, yLoc); //Player home level 2
                    break;
                    case -4815870: addObject(new DoorBlock(xCoord + "_" + yCoord, 51, 9, xCoord, yCoord), xLoc, yLoc); //Wood door
                    break;
                    case -8421505: addObject(new DoorBlock(xCoord + "_" + yCoord, 0, 7, xCoord, yCoord), xLoc, yLoc); //Black
                    break;

                    case -12566464: addObject(new TileBlock("Sign", 47, 0, "Messenger", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -10609824: addObject(new TileBlock("PokeMartSign", 49, 9, "PokeMartSign", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -11520930: addObject(new TileBlock("PokeCenterSign", 48, 9, "PokeCenterSign", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -6275824: addObject(new TileBlock("GymSign", 49, 12, "Messenger", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -6571288: addObject(new TileBlock("WaterLeftBlockCorner", 55, 0, "WaterBlock", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -16748385: addObject(new TileBlock("WaterLeftBlock", 55, 1, "WaterBlock", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -6694422: addObject(new TileBlock("WaterTopBlock", 54, 2, "WaterBlock", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -16735512: addObject(new Tiles("Water", 35, 1, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -16753017: addObject(new TileBlock("Water", 35, 1, "WaterBlock", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -10805473: addObject(new TileBlock("Boulder_Rock1", 34, 4, "Boulder_Rock", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -8443093: addObject(new TileBlock("Boulder_Rock2", 35, 4, "Boulder_Rock", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4440000: addObject(new TileBlock("Boulder_Rock3", 34, 5, "Boulder_Rock", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -6408907: addObject(new TileBlock("Boulder_Rock4", 35, 5, "Boulder_Rock", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -3494357: addObject(new TileBlock("GrassRightBlock", 12, 37, "GrassBlock", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2506936: addObject(new TileBlock("GrassLeftBlock", 10, 37, "GrassBlock", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2045079: addObject(new TileBlock("GrassDownBlock", 11, 38, "GrassBlock", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2636214: addObject(new TileBlock("GrassBottomBlock", 10, 38, "GrassBlock", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4011477: addObject(new TileBlock("GrassBottomRightBlock", 12, 38, "GrassBlock", 2, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -5111553: addObject(new TileBlock("PinkRoofTile0", 48, 14, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -3145473: addObject(new TileBlock("PinkRoofTile1", 48, 15, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -1769217: addObject(new TileBlock("PinkRoofTile3", 49, 14, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -65281: addObject(new TileBlock("PinkRoofTile4", 49, 15, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -65316: addObject(new TileBlock("PinkRoofTile5", 50, 14, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -65358: addObject(new TileBlock("PinkRoofTile6", 50, 15, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -46663: addObject(new TileBlock("PinkWall0", 48, 10, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -55145: addObject(new TileBlock("PinkWall1", 48, 11, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -505857: addObject(new TileBlock("PinkWall2", 49, 10, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -38673: addObject(new TileBlock("PinkWall3", 50, 10, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -53014: addObject(new TileBlock("PinkWall4", 50, 11, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -6422397: addObject(new TileBlock("PinkRoofTileSmall0", 45, 14, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -6408330: addObject(new TileBlock("PinkRoofTileSmall1", 46, 14, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2621253: addObject(new TileBlock("PinkRoofTileSmall2", 47, 14, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -5677042: addObject(new TileBlock("WallTile0", 0, 0, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7187956: addObject(new TileBlock("WallTile1", 5, 0, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2918382: addObject(new TileBlock("WallTile2", 6, 0, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -1984209: addObject(new TileBlock("WallTile3", 8, 0, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -3234018: addObject(new TileBlock("WallTile4", 7, 0, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -7119861: addObject(new TileBlock("TV_Top1", 2, 0, "TV", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7112949: addObject(new TileBlock("TV_Bottom1", 2, 1, "TV", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4811762: addObject(new TileBlock("Cabinet1", 3, 4, "Cabinet", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -736888: addObject(new TileBlock("Sink1", 3, 2, "Cabinet", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -1072565: addObject(new TileBlock("Sink2", 4, 2, "Cabinet", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -1140162: addObject(new TileBlock("Fridge_Top1", 5, 2, "Fridge", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -938137: addObject(new TileBlock("Fridge_Bottom1", 5, 3, "Fridge", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -11255034: addObject(new TileBlock("PC_Top1", 4, 0, "PC", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9940736: addObject(new TileBlock("PC_Bottom1", 4, 1, "PC", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -6718442: addObject(new TileBlock("Radio1", 5, 1, "Radio", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -5263958: addObject(new TileBlock("Bed_Top1", 10, 2, "Bed", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7435126: addObject(new TileBlock("Bed_Bottom1", 10, 3, "Bed", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -11128825: addObject(new TileBlock("Wall_Painting1", 11, 2, "Painting", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7440546: addObject(new TileBlock("Bookshelf_Top1", 3, 0, "Bookshelf", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7444934: addObject(new TileBlock("Bookshelf_Bottom1", 3, 1, "Bookshelf", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4621737: addObject(new TileBlock("Bookshelf_Top2", 16, 6, "Bookshelf", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9091023: addObject(new TileBlock("Bookshelf_Bottom2", 16, 7, "Bookshelf", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7237231: addObject(new TileBlock("TrashCan1", 15, 8, "TrashCan", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -6381922: addObject(new TileBlock("PokeMachinePC1", 20, 7, "TrashCan", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -5131855: addObject(new TileBlock("PokeMachinePC2", 21, 7, "TrashCan", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9079435: addObject(new TileBlock("PokeMachineP1", 22, 7, "TrashCan", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7697782: addObject(new TileBlock("PokeMachineP2", 23, 7, "TrashCan", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -10395295: addObject(new TileBlock("PokeMachineP3", 22, 8, "TrashCan", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -12500671: addObject(new TileBlock("PokeMachineP4", 23, 8, "TrashCan", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -5292537: addObject(new Tiles("Floor1", 0, 1, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -41199: addObject(new Tiles("Carpet1", 6, 1, 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -11119018: addObject(new Tiles("Floor1", 15, 7, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -773276: addObject(new Tiles("Carpet2", 20, 8, 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -3752557: addObject(new Tiles("Stool1", 1, 1, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -1913230: addObject(new TileBlock("Table1", 0, 4, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2242985: addObject(new TileBlock("Table2", 1, 4, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2441143: addObject(new TileBlock("Table3", 2, 15, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2770897: addObject(new TileBlock("Table4", 5, 15, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4217562: addObject(new TileBlock("Table5", 0, 6, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -6321888: addObject(new TileBlock("Table6", 1, 6, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7899865: addObject(new TileBlock("Table7", 0, 2, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7439841: addObject(new TileBlock("Table8", 1, 2, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -2046891: addObject(new TileBlock("Table9", 17, 6, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4412576: addObject(new TileBlock("Table10", 18, 6, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2376912: addObject(new TileBlock("Table11", 19, 6, "Table", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -10464749: addObject(new Tiles("Table_Legs1", 0, 3, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -11253999: addObject(new Tiles("Table_Legs2", 1, 3, 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -1866234: addObject(new Tiles("Table_Legs3", 17, 7, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4426214: addObject(new Tiles("Table_Legs4", 18, 7, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -6922731: addObject(new Tiles("Table_Legs5", 19, 7, 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -9609450: addObject(new Tiles("Table_Legs6", 2, 3, 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -5743869: addObject(new TileBlock("Roof_Brown1", 45, 6, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9290464: addObject(new TileBlock("Roof_Brown2", 45, 7, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -8961790: addObject(new TileBlock("Roof_Brown3", 46, 6, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -11129087: addObject(new TileBlock("Roof_Brown4", 47, 6, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9751806: addObject(new TileBlock("Roof_Brown5", 47, 7, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -12639743: addObject(new TileBlock("Roof_Brown6", 47, 8, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -3248124: addObject(new TileBlock("Roof_Brown7", 45, 8, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4832766: addObject(new TileBlock("Roof_Brown8", 46, 8, "Roof", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -48512: addObject(new Tiles("PkCenter_Floor1", 17, 2, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2686906: addObject(new Tiles("PkCenter_Floor2", 17, 1, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -1444853: addObject(new Tiles("PkCenter_Cushion", 21, 3, 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -10071711: addObject(new TileBlock("PkCenter_Wall1", 17, 0, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -15968804: addObject(new TileBlock("PkCenter_Bench1", 15, 3, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -288247: addObject(new Tiles("PkCenter_Bench1", 15, 3, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9592845: addObject(new TileBlock("PkCenter_Bench2", 20, 0, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -15506456: addObject(new TileBlock("PkCenter_Bench3", 20, 1, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -15908197: addObject(new TileBlock("PkCenter_Bench4", 20, 2, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -5980226: addObject(new TileBlock("PkCenter_Machine1.1", 15, 0, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -6048576: addObject(new TileBlock("PkCenter_Machine1.2", 16, 0, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7556183: addObject(new TileBlock("PkCenter_Machine1.3", 15, 1, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7621491: addObject(new TileBlock("PkCenter_Machine1.4", 16, 1, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -5458295: addObject(new TileBlock("PkCenter_Machine2.1", 15, 2, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -8221097: addObject(new TileBlock("PkCenter_Machine2.1", 16, 2, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -7442041: addObject(new TileBlock("PkCenter_PC1", 18, 0, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4478793: addObject(new TileBlock("PkCenter_PC2", 18, 1, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -13509593: addObject(new Tiles("PkMart_Floor", 20, 16, 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -6974059: addObject(new TileBlock("PkMart_Shelf1.1", 22, 12, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9474193: addObject(new TileBlock("PkMart_Shelf1.2", 23, 12, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -11053225: addObject(new TileBlock("PkMart_Shelf1.3", 22, 13, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -12040120: addObject(new TileBlock("PkMart_Shelf1.4", 23, 13, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -6332322: addObject(new TileBlock("PkMart_Shelf2.1", 24, 12, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -3628126: addObject(new TileBlock("PkMart_Shelf2.2", 24, 13, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -15237834: addObject(new TileBlock("PkMart_Shelf3.1", 21, 12, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -15638946: addObject(new TileBlock("PkMart_Bench1", 19, 12, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -8197912: addObject(new TileBlock("PkMart_Bench2", 18, 13, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -14165800: addObject(new TileBlock("PkMart_Bench3", 19, 13, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -15037300: addObject(new TileBlock("PkMart_Bench4", 17, 14, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -16480596: addObject(new Tiles("School_Floor", 18, 9, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -16296791: addObject(new Tiles("School_Stool", 18, 10, 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -10203648: addObject(new TileBlock("School_Bench1", 19, 10, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -5997056: addObject(new TileBlock("School_Bench2", 20, 10, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -2118400: addObject(new TileBlock("School_Bench3", 21, 10, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -16144335: addObject(new TileBlock("School_Wall1", 20, 9, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -16013724: addObject(new TileBlock("School_Wall2", 21, 9, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -16396906: addObject(new TileBlock("School_Wall3", 22, 9, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -3536550: addObject(new TileBlock("School_Wall4", 19, 9, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -13211946: addObject(new Tiles("Gym_Floor1", 17, 19, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -14993021: addObject(new TileBlock("Gym_Floor2", 17, 20, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -8024343: addObject(new Tiles("Gym_Floor3", 19, 20, 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -13356477: addObject(new TileBlock("Gym_Wall", 19, 19, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -11053212: addObject(new TileBlock("Gym_Statue1", 23, 18, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7498336: addObject(new TileBlock("Gym_Statue2.1", 19, 21, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -12433585: addObject(new TileBlock("Gym_Statue2.2", 19, 22, "Wall", 1, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -11377737: addObject(new Tiles("MrPkmn_House_Floor", 8, 29, 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -6748157: addObject(new Tiles("MrPkmn_House_Wires", 8, 30, 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9347012: addObject(new Tiles("MrPkmn_House_Stool", 8, 31, 2, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -12077220: addObject(new TileBlock("MrPkmn_HouseWall", 8, 28, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -9747394: addObject(new TileBlock("MrPkmn_House_Bookshelf1.1", 10, 28, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9747347: addObject(new TileBlock("MrPkmn_House_Bookshelf1.2", 10, 29, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -8289664: addObject(new TileBlock("MrPkmn_House_Bookshelf2.1", 9, 28, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9868460: addObject(new TileBlock("MrPkmn_House_Bookshelf2.2", 9, 29, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -13930446: addObject(new TileBlock("MrPkmn_House_Tree1.1", 11, 30, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -10915790: addObject(new TileBlock("MrPkmn_House_Tree1.2", 11, 31, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -45056: addObject(new TileBlock("MrPkmn_House_Table1.1", 9, 32, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -4441344: addObject(new TileBlock("MrPkmn_House_Table1.2", 10, 32, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -10281216: addObject(new TileBlock("MrPkmn_House_Table1.3", 9, 33, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -7459584: addObject(new TileBlock("MrPkmn_House_Table1.4", 10, 33, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;

                    case -20734: addObject(new TileBlock("MrPkmn_House_Table2.1", 9, 30, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -3897855: addObject(new TileBlock("MrPkmn_House_Table2.2", 10, 30, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -9943295: addObject(new TileBlock("MrPkmn_House_Table2.3", 9, 31, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                    case -6920447: addObject(new TileBlock("MrPkmn_House_Table2.4", 10, 31, "Wall", 2, xCoord, yCoord), xLoc, yLoc);
                    break;
                }
            }
        }
    }

    /**
     * Sets the game speed, as well as the act and paint orders.
     */
    public void setup()
    {
        Greenfoot.setSpeed(30);
        setActOrder(Player.class, NPC.class, GameWorld.class);
        setPaintOrder(Blackout.class, Display.class, TextInfoBox.class, InfoBoxes.class, Player.class, NPC.class, WorldBlock.class, Tiles.class);
    }

    /**
     * The act method of the world.
     * 
     * Does the following:
     * <p> -Checks for the current area.
     * <p> -Moves the player in the world.
     * <p> -Opens and closes the in-game menu.
     * <p> -Checks for wild pokemon (if in tall grass).
     */
    public void act()
    {
        areaCheck();

        if(isActing)
        {
            if(Greenfoot.isKeyDown("W"))
            {
                player.setLastDir("Up");
                player.setDirNum(0);

                if(player.isObjectAbove(TileBlock.class) == false && player.isObjectAbove(NPC.class) == false)
                {
                    for(Object obj : getObjects(Movers.class))
                    {
                        Movers mover = (Movers) obj;
                        mover.setLocation(mover.getX(), mover.getY() + 48);
                    }
                    currentWorldOffsetY--;
                }
                else
                {
                    try
                    {
                        TileBlock tileBlock = (TileBlock) getObjectsAt(216, 168, TileBlock.class).get(0);
                        if(tileBlock.getName().contains("Up"))
                        {
                            for(Object obj : getObjects(Movers.class))
                            {
                                Movers mover = (Movers) obj;
                                mover.setLocation(mover.getX(), mover.getY() + 96);
                            }
                            currentWorldOffsetY -= 2;
                        }
                    }
                    catch(Exception error)
                    {
                    }
                }

                randomPokemon = true;
            }
            else if(Greenfoot.isKeyDown("S"))
            {
                player.setLastDir("Down");
                player.setDirNum(1);

                if(player.isObjectBelow(TileBlock.class) == false && player.isObjectBelow(NPC.class) == false)
                {
                    for(Object obj : getObjects(Movers.class))
                    {
                        Movers mover = (Movers) obj;
                        mover.setLocation(mover.getX(), mover.getY() - 48);
                    }
                    currentWorldOffsetY++;
                }
                else
                {
                    try
                    {
                        TileBlock tileBlock = (TileBlock) getObjectsAt(216, 264, TileBlock.class).get(0);
                        if(tileBlock.getName().contains("Down"))
                        {
                            for(Object obj : getObjects(Movers.class))
                            {
                                Movers mover = (Movers) obj;
                                mover.setLocation(mover.getX(), mover.getY() - 96);
                            }
                            currentWorldOffsetY += 2;
                        }
                    }
                    catch(Exception error)
                    {
                    }
                }

                randomPokemon = true;
            }
            else if(Greenfoot.isKeyDown("A"))
            {
                player.setLastDir("Left");
                player.setDirNum(2);

                if(player.isObjectLeft(TileBlock.class) == false && player.isObjectLeft(NPC.class) == false)
                {
                    for(Object obj : getObjects(Movers.class))
                    {
                        Movers mover = (Movers) obj;
                        mover.setLocation(mover.getX() + 48, mover.getY());
                    }
                    currentWorldOffsetX--;
                }
                else
                {
                    try
                    {
                        TileBlock tileBlock = (TileBlock) getObjectsAt(168, 216, TileBlock.class).get(0);
                        if(tileBlock.getName().contains("Left"))
                        {
                            for(Object obj : getObjects(Movers.class))
                            {
                                Movers mover = (Movers) obj;
                                mover.setLocation(mover.getX() + 96, mover.getY());
                            }
                            currentWorldOffsetX -= 2;
                        }
                    }
                    catch(Exception error)
                    {
                    }
                }

                randomPokemon = true;
            }
            else if(Greenfoot.isKeyDown("D"))
            {
                player.setLastDir("Right");
                player.setDirNum(3);

                if(player.isObjectRight(TileBlock.class) == false && player.isObjectRight(NPC.class) == false)
                {
                    for(Object obj : getObjects(Movers.class))
                    {
                        Movers mover = (Movers) obj;
                        mover.setLocation(mover.getX() - 48, mover.getY());
                    }
                    currentWorldOffsetX++;
                }
                else
                {
                    try
                    {
                        TileBlock tileBlock = (TileBlock) getObjectsAt(264, 216, TileBlock.class).get(0);
                        if(tileBlock.getName().contains("Right"))
                        {
                            for(Object obj : getObjects(Movers.class))
                            {
                                Movers mover = (Movers) obj;
                                mover.setLocation(mover.getX() - 96, mover.getY());
                            }
                            currentWorldOffsetX += 2;
                        }
                    }
                    catch(Exception error)
                    {
                    }
                }

                randomPokemon = true;
            }
        }

        if(Greenfoot.isKeyDown("I") && keyCheckingI == true)
        {
            if(menuBox == null)
            {
                menuBox = new TextInfoBox("MenuBox", "MenuBox", 0);
                addObject(menuBox, 240, 216);//360, 216);
                isActing = false;
            }
        }        
        else if(!Greenfoot.isKeyDown("I"))
        {
            keyCheckingI = true;
        }

        if(Greenfoot.isKeyDown("L"))
        {
            if(menuBox != null)
            {
                if(menuBox.getCurrentMenu().equals("Default"))
                {
                    removeMenuBox();
                    keyCheckingI = false;
                }
            }
        }

        if(randomPokemon)
        {
            randomPokemon();
            randomPokemon = false;
        }
    }

    /**
     * Checks for the current area of the world.
     */
    public void areaCheck()
    {
        String oldAreaName = areaName;

        areaName = "Unknown";
        if(worldName.equals("World_Full"))
        {
            if(currentWorldOffsetY >= 86 && currentWorldOffsetY < 108)
            {
                if(currentWorldOffsetX >= 94 && currentWorldOffsetX < 120)
                {
                    areaName = "NewBarkTown";
                }
                else if(currentWorldOffsetX >= 34 && currentWorldOffsetX < 94)
                {
                    areaName = "Route_29";
                }
                else if(currentWorldOffsetX >= 6 && currentWorldOffsetX < 34)
                {
                    areaName = "Cherrygrove_City";
                }
            }
            else if(currentWorldOffsetY < 88 && currentWorldOffsetY >= 30)
            {
                if(currentWorldOffsetX > 4 && currentWorldOffsetX < 30)
                {
                    areaName = "Route_30";
                }
            }
            else if(currentWorldOffsetY < 30 && currentWorldOffsetY > 9)
            {
                if(currentWorldOffsetX > 0 && currentWorldOffsetX < 32)
                {
                    areaName = "Violet_City";
                }
            }
        }
        else
        {
            switch(worldName)
            {
                case "World_MrPkmn_House": areaName = "Route_30";
                break;

                case "World_NewBarkTown_ElmHouse": areaName = "NewBarkTown";
                break;

                case "World_NewBarkTown_ElmLab": areaName = "NewBarkTown";
                break;

                case "World_NewBarkTown_PlayerHouse1": areaName = "NewBarkTown";
                break;

                case "World_NewBarkTown_PlayerHouse2": areaName = "NewBarkTown";
                break;

                case "World_Pokecenter1": areaName = "Cherrygrove_City";
                break;

                case "World_Pokecenter2": areaName = "Violet_City";
                break;

                case "World_Pokemart1": areaName = "Cherrygrove_City";
                break;

                case "World_Pokemart2": areaName = "Violet_City";
                break;

                case "World_Violet_Gym": areaName = "Violet_City";
                break;

                case "World_Violet_School": areaName = "Violet_City";
                break;
            }
        }

        if(!areaName.equals(oldAreaName))
        {
            newMusic(areaName);
        }
    }

    /**
     * Removes the in-game menu.
     */
    public void removeMenuBox()
    {
        removeObject(menuBox);
        menuBox = null;
        isActing = true;
    }

    /**
     * Checks to see if there is a wild Pokemon in tall grass.
     */
    public void randomPokemon()
    {
        if(!(getObjectsAt(216, 216, Tiles.class).isEmpty()))
        {
            if((getObjectsAt(216, 216, Tiles.class).get(0)).getName().equals("TallGrass"))
            {
                int randomChance = Greenfoot.getRandomNumber(16);
                if(randomChance == 1) //1 in 15 chance of finding a pkmn
                {
                    randomLoop:
                    for(int i = 0; i < Reader.getFileLength("pokemonRoutesRates"); i++)
                    {
                        if(("World_" + areaName).equals(Reader.readStringFromFile("pokemonRoutesRates", i, 0)))
                        {
                            String tempStr = "";

                            try
                            {
                                saveWorld();

                                stopMusic();
                                blackout("Spiral");

                                List<String> pkmnRatesFile = Files.readAllLines(Paths.get("pokemonRoutesRates.txt"), StandardCharsets.UTF_8);
                                String[] pkmnRatesFileArr = pkmnRatesFile.toArray(new String[pkmnRatesFile.size()]);

                                tempStr = pkmnRatesFileArr[i];
                                String[] tempStrArr = tempStr.split("\\s*,\\s*");

                                List<String> pkmnRates = new ArrayList<>();

                                for(int z = 0; z < tempStrArr.length; z++)
                                {
                                    if(!tempStr.equals("-"))
                                    {
                                        if(!tempStrArr[z].startsWith("World_") && !tempStrArr[z].equals("-"))
                                        {
                                            pkmnRates.add(tempStrArr[z]);
                                        }
                                    }
                                }

                                String[] pkmnRateArr = pkmnRates.toArray(new String[pkmnRates.size()]);

                                Random randomGenerator = new Random();
                                int randomPokemonNum = randomGenerator.nextInt(100);

                                int oldPokemonChance = 0;
                                for(int q = 0; q < pkmnRateArr.length; q++)
                                {
                                    int currentPokemonChance = Integer.parseInt(pkmnRateArr[q].substring(0, 3));
                                    if(randomPokemonNum >= oldPokemonChance && randomPokemonNum < currentPokemonChance)
                                    {
                                        String pokemonName = pkmnRateArr[q].substring(8);
                                        int pokemonLevel = Greenfoot.getRandomNumber(Integer.parseInt(pkmnRateArr[q].substring(4, 7)));
                                        newBattle(pokemonName, pokemonLevel + 2);
                                        break randomLoop;
                                    }

                                    oldPokemonChance = currentPokemonChance;
                                }
                            }
                            catch(Exception error)
                            {
                                System.out.println(error.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates a new wild Pokemon battle.
     * @param pokemonName The name of the wild Pokemon.
     * @param pokemonLevel The level of the wild Pokemon.
     */
    public void newBattle(String pokemonName, int pokemonLevel)
    {
        stopMusic();
        Greenfoot.setWorld(new BattleWorld(pokemonName, pokemonLevel));
    }

    /**
     * Shows text, and saves the game.
     */
    public void saveGame()
    {
        if(menuBox != null)
        {
            removeObject(menuBox);
        }

        showText("SAVING...\nDON'T TURN OFF THE POWER.");
        saveWorld();
        showText(playerName + " saved the game.");
    }

    /**
     * Heals the pokemon of the player to their max hp, and full PP's.
     */
    public void healPokemon()
    {
        saveWorld();
        saveHealLocation();

        try
        {
            for(int p = 1; p < Reader.getFileLength("playerPokemon"); p++)
            {
                FileWriter fw = new FileWriter("tempPlayerPokemon.txt");

                List<String> list = Files.readAllLines(Paths.get("playerPokemon.txt"), StandardCharsets.UTF_8);
                String[] pokemonList = list.toArray(new String[list.size()]);

                String currentPokemonStr = pokemonList[p];
                String[] currentPokemonArray = currentPokemonStr.split("\\s*,\\s*");

                int newHp = StatCalculator.maxHealthCalc(currentPokemonArray[0], Integer.parseInt(currentPokemonArray[1]));
                currentPokemonArray[3] = Integer.toString(newHp);

                String[] currentMovesList = new String[4];
                for(int i = 5; i < 9; i++)
                {
                    if(!(currentPokemonArray[i]).equals("-"))
                    {
                        currentMovesList[i -5] = currentPokemonArray[i];
                    }
                }

                for(int l = 0; l < 4; l++)
                {
                    if(currentMovesList[l] != null &&!(currentMovesList[l].equals("null")) && !(currentMovesList[l].equals("-")))
                    {
                        for(int m = 1; m < Reader.getFileLength("movesList") - 1; m++)
                        {
                            if((currentMovesList[l].toUpperCase()).equals(Reader.readStringFromFile("movesList", m, 0).toUpperCase()))
                            {
                                currentPokemonArray[9 + l] = Integer.toString(Reader.readIntFromFile("movesList", m, 4));
                            }
                        }
                    }
                }

                String newPokemon = currentPokemonArray[0];
                for(int j = 0; j < 12; j++)
                {
                    newPokemon += ", ";
                    newPokemon += currentPokemonArray[j + 1];
                }

                pokemonList[p] = newPokemon;

                for(int f = 0; f < pokemonList.length; f++)
                {
                    fw.write(pokemonList[f]);
                    fw.write("\n");
                }
                fw.close();

                Writer.overwriteFile("tempPlayerPokemon", "playerPokemon");
            }
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
    }

    /**
     * Saves the current world data.
     */
    public void saveWorld()
    {
        try
        {
            FileWriter gameInfo = new FileWriter("tempGameInformation.txt");

            List<String> list = Files.readAllLines(Paths.get("gameInformation.txt"), StandardCharsets.UTF_8);
            String[] infoList = list.toArray(new String[list.size()]);
            String currentInfo = infoList[0];
            String[] infoArray = currentInfo.split("\\s*,\\s*");

            int newX = 0;
            int newY = 0;

            if(!(getObjectsAt(216, 216, Tiles.class).isEmpty()))
            {
                Tiles tile = (Tiles) getObjectsAt(216, 216, Tiles.class).get(0);
                newX = tile.getWorldX();
                newY = tile.getWorldY();
            }

            infoArray[0] = playerName;
            infoArray[1] = worldName;
            infoArray[2] = Integer.toString(newX);
            infoArray[3] = Integer.toString(newY);

            String toWrite = infoArray[0];
            for(int i = 1; i < infoArray.length; i++)
            {
                toWrite += ", ";
                toWrite += infoArray[i];
            }

            gameInfo.write(toWrite);
            gameInfo.close();

            Writer.overwriteFile("tempGameInformation", "gameInformation");
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
    }

    /**
     * Saves the location of the last heal.
     */
    public void saveHealLocation()
    {
        try
        {
            FileWriter gameInfo = new FileWriter("tempGameInformation.txt");

            List<String> list = Files.readAllLines(Paths.get("gameInformation.txt"), StandardCharsets.UTF_8);
            String[] infoList = list.toArray(new String[list.size()]);
            String currentInfo = infoList[0];
            String[] infoArray = currentInfo.split("\\s*,\\s*");

            int newX = 0;
            int newY = 0;

            if(!(getObjectsAt(216, 216, Tiles.class).isEmpty()))
            {
                Tiles tile = (Tiles) getObjectsAt(216, 216, Tiles.class).get(0);
                newX = tile.getWorldX();
                newY = tile.getWorldY();
            }

            infoArray[4] = worldName;
            infoArray[5] = Integer.toString(newX);
            infoArray[6] = Integer.toString(newY);

            String toWrite = infoArray[0];
            for(int i = 1; i < infoArray.length; i++)
            {
                toWrite += ", ";
                toWrite += infoArray[i];
            }

            gameInfo.write(toWrite);
            gameInfo.close();

            Writer.overwriteFile("tempGameInformation", "gameInformation");
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
    }

    /**
     * Creats a new trainer Pokemon battle.
     * @param pokemonID The id of the NPC's pokemon.
     */
    public void newBattle(int pokemonID)
    {
        stopMusic();
        Greenfoot.setWorld(new BattleWorld(pokemonID));
    }

    /**
     * Returns the RGB colour of a pixel of a map.
     * @param mapName The name of the map.
     * @param x The X coordinate of the map.
     * @param y The Y coordinate of the map.
     */
    public void getColourAt(String mapName, int x, int y)
    {
        System.out.println("-----");
        System.out.println(new GreenfootImage("World_" + mapName + ".png").getColorAt(x, y).getRGB());
        System.out.println("-----");
    }

    /**
     * Returns if the world is running.
     */
    public boolean getIsActing()
    {
        return isActing;
    }

    /**
     * Returns the name of the player.
     */
    public String getPlayerName()
    {
        return playerName;        
    }

    /**
     * Returns the name of the world.
     */
    public String getWorldName()
    {
        return worldName;
    }
}