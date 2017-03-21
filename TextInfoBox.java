import greenfoot.*;
import java.io.FileWriter;
import java.io.FileReader;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The Pokemon class, to allow make a GUI in the BattleMenu. This provides all movement of cursors, as well as selection during the battles.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class TextInfoBox extends InfoBoxes
{
    //Cursor coordinates for various menus
    private int defaultCursorXCoord;
    private int defaultCursorYCoord;
    private int fightCursorXCoord;
    private int fightCursorYCoord;
    private int pkmnCursorXCoord;
    private int pkmnCursorYCoord;
    private int packCursorXCoord;
    private int packCursorYCoord;
    private int packSelectionCursorXCoord;
    private int packSelectionCursorYCoord;

    private int fightCursorMovementLength;
    private int pkmnCursorMovementLength;
    private int packCursorMovementLength;

    //Key checking variables
    private boolean keyCheckingW = true;
    private boolean keyCheckingA = true;
    private boolean keyCheckingS = true;
    private boolean keyCheckingD = true;
    private boolean keyCheckingK = true;
    private boolean keyCheckingL = true;

    //Various display data
    private String type;
    private String battleMenu;
    private String currentMovesList[] = new String[4];
    private String packMenu;
    private final String[] PACK_MENUS = {"Items", "Balls", "KeyItems", "TMHM"};

    private boolean canUpdate = true; //If the box can update

    private boolean packSelection = false; //If the box is selecting something in a menu
    private boolean packSelectionUpdating = true; //If the box is updating while in a menu

    private boolean canRunAway = false; //If the user can run away

    private int currentPokemon; //The current Pokemon's id

    //If the program has checked for all Pokemon's max healths
    private boolean hasCheckedMaxHealths = true;
    private int[] pokemonPartyMaxHealths = new int[Reader.getFileLength("playerPokemon") + 1];

    /**
     * The default constructor for the TextInfoBox.
     * @param newBoxName The name of the box.
     */
    public TextInfoBox(String newBoxName)
    {
        super(newBoxName);  
    }

    /**
     * The constructor for the TextInfoBoxes in battles.
     * @param newBoxName The name of the box.
     * @param inType The type of box.
     * @param inCurrentPokemon The current Pokemon id.
     */
    public TextInfoBox(String newBoxName, String inType, int inCurrentPokemon)
    {
        super(newBoxName);
        type = inType;

        currentPokemon = inCurrentPokemon;

        if(type.equals("BattleBar"))
        {
            defaultCursorXCoord = 219;
            defaultCursorYCoord = 45;

            setLocation(240, 363);
            battleMenu = "Default";
            drawWord("FIGHT", 240, 45);
            drawWord("PACK", 240, 93);
            drawLetter("PK", 384, 45);
            drawLetter("MN", 408, 45);
            drawWord("RUN", 384, 93);
            drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);

            for(int i = 5; i < 9; i++)
            {
                if(!(Reader.readStringFromFile("playerPokemon", currentPokemon, i)).equals("-"))
                {
                    currentMovesList[i - 5] = (Reader.readStringFromFile("playerPokemon", currentPokemon, i)).toUpperCase();
                }
                else
                {
                    break;
                }
            }
        }
        else if(type.equals("MenuBox"))
        {
            defaultCursorXCoord = 267;
            defaultCursorYCoord = 48;

            setLocation(260, 216);
            battleMenu = "Default";

            drawWord("POKéMON", 288, 48);
            drawWord("PACK", 288, 96);
            drawWord("SAVE", 288, 144);
            drawWord("EXIT", 288, 192);

            drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
        }
        else if(type.equals("StartMenu"))
        {
            defaultCursorXCoord = 27;
            defaultCursorYCoord = 48;

            setLocation(260, 216);

            drawWord("CONTINUE", 48, 48);
            drawWord("NEW GAME", 48, 96);
            drawWord("QUIT", 48, 144);

            drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
        }
    }

    /**
     * The act method for the box.
     * Allows the user to interact during a battle (make selections, move cursor, etc).
     */
    public void act() 
    {
        if(type.equals("BattleBar"))
        {
            try
            {
                BattleWorld battleWorld = (BattleWorld) getWorld();
                if(battleWorld.getPlayerPokemon().getTurn())
                {
                    selection();

                    if(battleMenu.equals("Default") || battleMenu.equals("Fight") || battleMenu.equals("PkMn") || battleMenu.equals("Pack"))
                    {
                        moveCursor();
                    }

                    if(battleMenu.equals("Run"))
                    {
                        if(battleWorld.getWildPokemon())
                        {
                            int playerPkmnLevel = battleWorld.getPlayerPokemon().getLevel();
                            int enemyPkmnLevel = battleWorld.getEnemyPokemon().getLevel();

                            if(playerPkmnLevel > enemyPkmnLevel)
                            {
                                canRunAway = true;
                            }
                            else if(playerPkmnLevel == enemyPkmnLevel)
                            {
                                int randomNum1 = Greenfoot.getRandomNumber(25);
                                int randomNum2 = Greenfoot.getRandomNumber(25);

                                if(randomNum1 == randomNum2)
                                {
                                    canRunAway = true;
                                }
                                else
                                {
                                    canRunAway = false;
                                }
                            }
                            else if((playerPkmnLevel < enemyPkmnLevel))
                            {
                                canRunAway = false;
                            }
                        }
                        else
                        {
                            canRunAway = false;
                        }
                        canUpdate = true;
                    }

                    if(canUpdate)
                    {
                        updateImage();
                        canUpdate = false;
                    }
                }
            }
            catch(Exception error)
            {
            }
        }
        else if(type.equals("MenuBox"))
        {
            selection();

            if(battleMenu.equals("Default") || battleMenu.equals("PkMn") || battleMenu.equals("Pack"))
            {
                moveCursor();
            }

            if(canUpdate)
            {
                updateImage();
                canUpdate = false;
            }
        }
        else if(type.equals("StartMenu"))
        {
            selection();
            moveCursor();

            if(canUpdate)
            {
                updateImage();
                canUpdate = false;
            }
        }
    }

    /**
     * Make a selection.
     */
    public void selection()
    {
        if(Greenfoot.isKeyDown("K") && keyCheckingK == true)//aka 'A' button
        {
            if(type.equals("BattleBar"))
            {
                if(canUpdate == false)
                {
                    if(battleMenu.equals("Default"))
                    {
                        if(defaultCursorXCoord == 219)
                        {
                            if(defaultCursorYCoord == 45)
                            {
                                fightCursorXCoord = 120;
                                fightCursorYCoord = 117;
                                battleMenu = "Fight";
                                canUpdate = true;
                            }
                            else if(defaultCursorYCoord == 93)
                            {
                                packCursorXCoord = 171;
                                packCursorYCoord = 48;
                                battleMenu = "Pack";
                                packMenu = "Items";
                                canUpdate = true;
                            }
                        }
                        else if(defaultCursorXCoord == 363)
                        {
                            if(defaultCursorYCoord == 45)
                            {
                                pkmnCursorXCoord = 3;
                                pkmnCursorYCoord = 24;
                                battleMenu = "PkMn";

                                BattleWorld battleWorld = (BattleWorld) getWorld();
                                battleWorld.savePokemonData();
                                canUpdate = true;
                            }
                            else if(defaultCursorYCoord == 93)
                            {
                                battleMenu = "Run";
                                canUpdate = true;
                            }                        
                        }
                    }
                    else if(battleMenu.equals("Fight"))
                    {
                        fightSelection();
                    }
                    else if(battleMenu.equals("PkMn"))
                    {
                        pkmnSelection(true);
                    }
                    else if(battleMenu.equals("Pack"))
                    {
                        packSelection(true);
                    }
                }
            }
            else if(type.equals("MenuBox"))
            {
                if(canUpdate == false)
                {
                    if(battleMenu.equals("Default"))
                    {
                        switch(defaultCursorYCoord)
                        {
                            case 48: battleMenu = "PkMn";
                            pkmnCursorXCoord = 3;
                            pkmnCursorYCoord = 24;
                            break;

                            case 96: battleMenu = "Pack";
                            packMenu = "Items";
                            packCursorXCoord = 171;
                            packCursorYCoord = 45;
                            break;

                            case 144: battleMenu = "Save";
                            ((GameWorld) getWorld()).saveGame();
                            break;

                            case 192: battleMenu = "Exit";
                            ((GameWorld) getWorld()).removeMenuBox();
                            break;
                        }
                        canUpdate = true;
                    }
                    else if(battleMenu.equals("PkMn"))
                    {
                        pkmnSelection(false);
                    }
                    else if(battleMenu.equals("Pack"))
                    {
                        packSelection(false);
                    }
                }
            }
            else if(type.equals("StartMenu"))
            {
                StartWorld startWorld = (StartWorld) getWorld();
                switch(defaultCursorYCoord)
                {
                    case 48: startWorld.blackout("Fade");
                    startWorld.stopMusic();
                    Greenfoot.setWorld(new GameWorld());
                    break;

                    case 96: String name = Greenfoot.ask("What is your name? ");
                    String starterPkmn = Greenfoot.ask("Which POKéMON do you want? Chikorita, Totodile, or Cyndaquil?");
                    while(!starterPkmn.equals("Chikorita") && !starterPkmn.equals("Totodile") && !starterPkmn.equals("Cyndaquil"))
                    {
                        starterPkmn = Greenfoot.ask("Which POKéMON do you want? Chikorita, Totodile, or Cyndaquil?");
                    }

                    Writer.overwriteFile("startPlayerBalls", "playerBalls");
                    Writer.overwriteFile("startPlayerItems", "playerItems");
                    Writer.overwriteFile("startPlayerPokemon", "playerPokemon");

                    Pokemon newPokemon = new Pokemon(1, starterPkmn, 5, true, 9999, 0, true);
                    newPokemon.newPokemonMoves();
                    startWorld.savePokemonData(newPokemon, 1);

                    startWorld.blackout("Fade");
                    startWorld.stopMusic();
                    Greenfoot.setWorld(new GameWorld(name));
                    break;

                    case 144: startWorld.blackout("Fade");
                    startWorld.stopMusic();
                    System.exit(0);
                    break;
                }
                canUpdate = true;
            }
            keyCheckingK = false; //Move is over
        }
        else if(!Greenfoot.isKeyDown("K"))
        {
            keyCheckingK = true;
        }

        if(Greenfoot.isKeyDown("L") && keyCheckingL == true)//aka 'B' button
        {
            if(type.equals("BattleBar"))
            {
                if(battleMenu.equals("Fight") || battleMenu.equals("PkMn") || battleMenu.equals("Run"))
                {
                    battleMenu = "Default";
                    canUpdate = true;
                    updateImage();
                }

                if(battleMenu.equals("Pack"))
                {
                    if(!packSelection)
                    {
                        battleMenu = "Default";
                        canUpdate = true;
                    }
                    else
                    {
                        packSelection = false;
                        canUpdate = true;
                    }
                }
            }
            else if(type.equals("MenuBox"))
            {
                if(battleMenu.equals("PkMn") || battleMenu.equals("Save"))
                {
                    battleMenu = "Default";
                    canUpdate = true;
                    updateImage();
                }

                if(battleMenu.equals("Pack"))
                {
                    if(!packSelection)
                    {
                        battleMenu = "Default";
                        canUpdate = true;
                    }
                    else
                    {
                        packSelection = false;
                        canUpdate = true;
                    }
                }
            }
            keyCheckingL = false;
        }
        else if(!Greenfoot.isKeyDown("L"))
        {
            keyCheckingL = true;
        }
    }

    public void fightSelection()
    {
        String moveName = "";

        switch(fightCursorYCoord)
        {
            case 117: moveName = currentMovesList[0];
            break;
            case 141: moveName = currentMovesList[1];
            break;
            case 165: moveName = currentMovesList[2];
            break;
            case 189: moveName = currentMovesList[3];
            break;
        }

        int movePP = 0;

        movesRead:
        for(int m = 0; m < currentMovesList.length; m++)
        {
            if(currentMovesList != null)
            {
                if(moveName.equals(currentMovesList[m]))
                {
                    movePP = Reader.readIntFromFile("playerPokemon", currentPokemon, 9 + m);

                    BattleWorld battleWorld = (BattleWorld) getWorld();
                    Pokemon playerPokemon = battleWorld.getPlayerPokemon();
                    if(movePP > 0)
                    {
                        playerPokemon.inflictDamage(moveName);
                        movePP--;

                        try
                        {
                            FileWriter fw = new FileWriter("tempPlayerPokemon.txt");

                            List<String> list = Files.readAllLines(Paths.get("playerPokemon.txt"), StandardCharsets.UTF_8);
                            String[] pokemonList = list.toArray(new String[list.size()]);

                            String currentPokemonStr = pokemonList[currentPokemon];
                            String[] currentPokemonArray = currentPokemonStr.split("\\s*,\\s*");

                            currentPokemonArray[(currentPokemonArray.length - 4) + m] = Integer.toString(movePP);

                            String arrayAdd = currentPokemonArray[0];
                            for(int i = 1; i <  currentPokemonArray.length; i++)
                            {
                                arrayAdd = arrayAdd.concat(", " + currentPokemonArray[i]);
                            }

                            pokemonList[currentPokemon] = arrayAdd;

                            for(int f = 0; f < pokemonList.length; f++)
                            {
                                fw.write(pokemonList[f]);
                                fw.write("\n");
                            }
                            fw.close();

                            Writer.overwriteFile("tempPlayerPokemon", "playerPokemon");
                            canUpdate = true;
                        }
                        catch(Exception error)
                        {
                            System.out.println(error.getMessage());
                        }
                        battleMenu = "Default";
                        canUpdate = true;

                        battleWorld.changeTurn();
                    }
                    break movesRead;
                }
            }
        }
    }

    public void pkmnSelection(boolean selecting)
    {
        pkmnCursorMovementLength = ((Reader.getFileLength("playerPokemon") - 1) * 48) + 24;
        if(pkmnCursorYCoord == pkmnCursorMovementLength)
        {
            pkmnCursorYCoord = 24;
            battleMenu = "Default";
            canUpdate = true;
            updateImage();
        }

        if(selecting)
        {
            BattleWorld battleWorld = (BattleWorld) getWorld();
            if(pkmnCursorYCoord != ((battleWorld.getCurrentPlayerPokemon() - 1) * 48 + 24))
            {
                int tempPkmnNum = ((pkmnCursorYCoord - 24) / 48);
                tempPkmnNum += 1; //Do this because the file has a descriptor line
                if(Reader.readIntFromFile("pokeMonListBattle", tempPkmnNum, 3) > 0)
                {
                    battleWorld.setCurrentPlayerPokemon(tempPkmnNum);
                    battleWorld.addNewPokemon("Player", tempPkmnNum, "", 0);
                    currentPokemon = battleWorld.getCurrentPlayerPokemon();
                    battleMenu = "Default";
                    canUpdate = true;
                    updateImage();
                    battleWorld.changeTurn();
                }
            }
        }
    }

    public void packSelection(boolean selecting)
    {
        packCursorMovementLength = (Reader.getFileLength("player" + packMenu) * 48) + 48;
        if(packCursorYCoord == packCursorMovementLength)
        {
            packCursorYCoord = 48;
            battleMenu = "Default";
            canUpdate = true;
        }
        else
        {
            if(selecting)
            {
                if(!packSelection)
                {
                    packSelection = true;
                    packSelectionCursorXCoord = 27;
                    packSelectionCursorYCoord = 192;
                    canUpdate = true;
                    packSelectionUpdating = false;
                }
                else
                {
                    packSelectionUpdating = true;
                }

                if(packSelectionUpdating)
                {
                    if(packSelectionCursorYCoord == 192)
                    {                                    
                        try
                        {
                            int readLength = Reader.getFileLength("player" + packMenu);

                            packLoop:
                            for(int i = 0; i < readLength; i++)
                            {
                                String tempItemName = Reader.readStringFromFile("player" + packMenu, i, 0);
                                int tempItemAmount = Reader.readIntFromFile("player" + packMenu, i, 1);

                                FileWriter tempPlayerPack = new FileWriter("tempPlayer" + packMenu + ".txt");

                                if(packCursorYCoord == 48 + 48 * i)
                                {
                                    BattleWorld battleWorld = (BattleWorld) getWorld();

                                    if(tempItemName.equals("Potion"))
                                    {
                                        // System.out.println("Potion");
                                        List<String> list = Files.readAllLines(Paths.get("player" + packMenu + ".txt"), StandardCharsets.UTF_8);
                                        String[] itemList = list.toArray(new String[list.size()]);

                                        String currentItem = itemList[0];
                                        String[] currentItemArray = currentItem.split("\\s*,\\s*");

                                        int potionNum = Integer.parseInt(currentItemArray[1]);

                                        if(potionNum > 0)
                                        {
                                            battleWorld.getPlayerPokemon().heal(20);
                                            potionNum--;

                                            itemList[0] = "Potion, " + potionNum + ", Restores Pokemon HP by 20.";
                                            for(int f = 0; f < itemList.length; f++)
                                            {
                                                tempPlayerPack.write(itemList[f]);
                                            }
                                            tempPlayerPack.close();

                                            Writer.overwriteFile("tempPlayer" + packMenu, "player" + packMenu);
                                            canUpdate = true;
                                            updateImage();
                                        }
                                        break packLoop;
                                    }
                                    else if(tempItemName.equals("Poke Ball"))
                                    {
                                        // System.out.println("Poke Ball");
                                        List<String> packList = Files.readAllLines(Paths.get("player" + packMenu + ".txt"), StandardCharsets.UTF_8);
                                        String[] itemList = packList.toArray(new String[packList.size()]);

                                        String currentItem = itemList[0];
                                        String[] currentItemArray = currentItem.split("\\s*,\\s*");

                                        int ballNum = Integer.parseInt(currentItemArray[1]);

                                        if(ballNum > 0)
                                        {
                                            Pokemon enemyPokemon = battleWorld.getEnemyPokemon();
                                            PkmnInfoBox enemyPkmnInfoBox = battleWorld.getEnemyInfoBox();

                                            String tempName = enemyPokemon.getName();
                                            int tempLevel = enemyPokemon.getLevel();
                                            Boolean tempGender = enemyPokemon.getGender();
                                            int tempCurrentHp = enemyPokemon.getCurrentHealth();
                                            int tempCurrentXp = enemyPokemon.getCurrentExperience();

                                            String tempMoveNames[] = enemyPokemon.getMovesNames();
                                            String tempMoveName1 = tempMoveNames[0];
                                            String tempMoveName2 = tempMoveNames[1];
                                            String tempMoveName3 = tempMoveNames[2];
                                            String tempMoveName4 = tempMoveNames[3];

                                            int tempMovePP[] = enemyPokemon.getMovesPP();
                                            int tempMovePP1 = tempMovePP[0];
                                            int tempMovePP2 = tempMovePP[1];
                                            int tempMovePP3 = tempMovePP[2];
                                            int tempMovePP4 = tempMovePP[3];

                                            String tempLine = "";
                                            tempLine += tempName + ", ";
                                            tempLine += tempLevel + ", ";
                                            tempLine += tempGender + ", ";
                                            tempLine += tempCurrentHp + ", ";
                                            tempLine += tempCurrentXp + ", ";
                                            tempLine += tempMoveName1 + ", ";
                                            tempLine += tempMoveName2 + ", ";
                                            tempLine += tempMoveName3 + ", ";
                                            tempLine += tempMoveName4 + ", ";
                                            tempLine += tempMovePP1 + ", ";
                                            tempLine += tempMovePP2 + ", ";
                                            tempLine += tempMovePP3 + ", ";
                                            tempLine += tempMovePP4;

                                            List<String> pokemonList = Files.readAllLines(Paths.get("playerPokemon.txt"), StandardCharsets.UTF_8);
                                            String[] currentPokemonList = pokemonList.toArray(new String[pokemonList.size() + 1]);

                                            String message = "";

                                            if(currentPokemonList.length < 8)
                                            {
                                                currentPokemonList[currentPokemonList.length - 1] = tempLine;
                                                FileWriter tempPlayerPkmn = new FileWriter("tempPlayerPokemon.txt");
                                                for(int k = 0; k < currentPokemonList.length; k++)
                                                {
                                                    tempPlayerPkmn.write(currentPokemonList[k] + "\n");
                                                }
                                                tempPlayerPkmn.close();

                                                Writer.overwriteFile("tempPlayerPokemon", "playerPokemon");

                                                String enemyPkmnName = enemyPokemon.getName();
                                                getWorld().removeObject(enemyPokemon);
                                                getWorld().removeObject(enemyPkmnInfoBox);

                                                message = "Gotcha! " +  enemyPkmnName + " was\ncaught!";
                                            }
                                            else
                                            {
                                                List<String> pcList = Files.readAllLines(Paths.get("pcPokemon.txt"), StandardCharsets.UTF_8);
                                                String[] currentPcList = pcList.toArray(new String[pcList.size() + 1]);

                                                currentPcList[currentPcList.length - 1] = tempLine;

                                                FileWriter tempPcPkmn = new FileWriter("tempPcPokemon.txt");
                                                for(int k = 0; k < currentPcList.length; k++)
                                                {
                                                    tempPcPkmn.write(currentPcList[k] + "\n");
                                                }
                                                tempPcPkmn.close();

                                                Writer.overwriteFile("tempPcPokemon", "pcPokemon");

                                                String enemyPkmnName = enemyPokemon.getName();
                                                getWorld().removeObject(enemyPokemon);
                                                getWorld().removeObject(enemyPkmnInfoBox);

                                                message = "Caught enemy POKéMON " +  enemyPkmnName + "\n" + enemyPkmnName + " was sent to\nthe PC!";
                                                message = "Gotcha! HOOTHOOT was caught!";
                                            }

                                            ballNum--;
                                            itemList[0] = "Poke Ball, " + ballNum + ", An item for catching Pokemon.";
                                            for(int f = 0; f < itemList.length; f++)
                                            {
                                                tempPlayerPack.write(itemList[f]);
                                            }
                                            tempPlayerPack.close();
                                            Writer.overwriteFile("tempPlayer" + packMenu, "player" + packMenu);

                                            canUpdate = true;
                                            updateImage();

                                            displayText(message, true, false);

                                            battleWorld.savePokemonData();
                                            battleWorld.stopMusic();
                                            battleWorld.newGameWorld();
                                        }
                                        break packLoop;
                                    }
                                }
                            }

                            packSelection = false;
                            battleMenu = "Default";
                        }
                        catch(Exception error)
                        {
                            System.out.println(error.getMessage());
                        }
                    }
                    else
                    {
                        packSelection = false;
                        canUpdate = true;
                    }
                }
            }
        }
    }

    /**
     * Move the cursor in a menu.
     */
    public void moveCursor()
    {
        if(type.equals("BattleBar"))
        {
            if(Greenfoot.isKeyDown("W") && keyCheckingW == true)
            {
                if(battleMenu.equals("Default"))
                {
                    if(defaultCursorYCoord == 93)
                    {
                        defaultCursorYCoord = defaultCursorYCoord - 48;
                        drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                        canUpdate = true;
                    }
                }
                else if(battleMenu.equals("Fight"))
                {
                    fightCursorMovementLength = 0;
                    for(int i = 0; i < currentMovesList.length; i++)
                    {
                        if(currentMovesList[i] != null)
                        {
                            fightCursorMovementLength++;
                        }
                    }

                    if(fightCursorYCoord > 117)
                    {
                        fightCursorYCoord = fightCursorYCoord - 24;
                        drawCursor("Black", fightCursorXCoord, fightCursorYCoord);
                        canUpdate = true;
                    }
                    else if(fightCursorYCoord == 117)
                    {
                        fightCursorYCoord = fightCursorYCoord + (fightCursorMovementLength * 24) - 24;
                        drawCursor("Black", fightCursorXCoord, fightCursorYCoord);
                        canUpdate = true;
                    }
                }
                else if(battleMenu.equals("PkMn"))
                {
                    movePkmnCursorUp();
                }
                else if(battleMenu.equals("Pack"))
                {
                    movePackCursorUp();
                }
                keyCheckingW = false;
            }
            else if(Greenfoot.isKeyDown("S") && keyCheckingS == true)
            {
                if(battleMenu.equals("Default"))
                {
                    if(defaultCursorYCoord == 45)
                    {
                        defaultCursorYCoord = defaultCursorYCoord + 48;
                        drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                        canUpdate = true;
                    }
                }
                else if(battleMenu.equals("Fight"))
                {
                    fightCursorMovementLength = 0;
                    for(int i = 0; i < currentMovesList.length; i++)
                    {
                        if(currentMovesList[i] != null)
                        {
                            fightCursorMovementLength++;
                        }
                    }

                    if(fightCursorYCoord < 117 + (fightCursorMovementLength * 24) - 24)
                    {
                        fightCursorYCoord = fightCursorYCoord + 24;
                        drawCursor("Black", fightCursorXCoord, fightCursorYCoord);
                        canUpdate = true;
                    }
                    else if(fightCursorYCoord == 117 + (fightCursorMovementLength * 24) - 24)
                    {
                        fightCursorYCoord = 117;
                        drawCursor("Black", fightCursorXCoord, fightCursorYCoord);
                        canUpdate = true;
                    }
                }
                else if(battleMenu.equals("PkMn"))
                {
                    movePkmnCursorDown();
                }
                else if(battleMenu.equals("Pack"))
                {
                    movePackCursorDown();
                }
                keyCheckingS = false;
            }
            else if(Greenfoot.isKeyDown("A") && keyCheckingA == true)
            {
                if(battleMenu.equals("Default"))
                {
                    if(defaultCursorXCoord == 363)
                    {
                        defaultCursorXCoord = defaultCursorXCoord - 144;
                        drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                        canUpdate = true;
                    }
                }
                else if(battleMenu.equals("Pack"))
                {
                    movePackCursorLeft();
                }
                keyCheckingA = false;
            }
            else if(Greenfoot.isKeyDown("D") && keyCheckingD == true)
            {
                if(battleMenu.equals("Default"))
                {
                    if(defaultCursorXCoord == 219)
                    {
                        defaultCursorXCoord = defaultCursorXCoord + 144;
                        drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                        canUpdate = true;
                    }
                }
                else if(battleMenu.equals("Pack"))
                {
                    movePackCursorRight();
                    packCursorYCoord = 48;
                }
                keyCheckingD = false;
            }
        }
        else if(type.equals("MenuBox"))
        {
            if(Greenfoot.isKeyDown("W") && keyCheckingW == true)
            {
                if(battleMenu.equals("Default"))
                {
                    if(defaultCursorYCoord > 48)
                    {
                        defaultCursorYCoord = defaultCursorYCoord - 48;
                        drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                        canUpdate = true;
                    }
                    else
                    {
                        defaultCursorYCoord = 192;
                        drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                        canUpdate = true;
                    }
                }
                else if(battleMenu.equals("PkMn"))
                {
                    movePkmnCursorUp();
                }
                else if(battleMenu.equals("Pack"))
                {
                    movePackCursorUp();
                }
                keyCheckingW = false;
            }
            else if(Greenfoot.isKeyDown("S") && keyCheckingS == true)
            {
                if(battleMenu.equals("Default"))
                {
                    if(defaultCursorYCoord < 192)
                    {
                        defaultCursorYCoord = defaultCursorYCoord + 48;
                        drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                        canUpdate = true;
                    }
                    else
                    {
                        defaultCursorYCoord = 48;
                        drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                        canUpdate = true;
                    }
                }
                else if(battleMenu.equals("PkMn"))
                {
                    movePkmnCursorDown();
                }
                else if(battleMenu.equals("Pack"))
                {
                    movePackCursorDown();
                }
                keyCheckingS = false;
            }
            else if(Greenfoot.isKeyDown("A") && keyCheckingA == true)
            {
                if(battleMenu.equals("Pack"))
                {
                    movePackCursorLeft();
                }
                keyCheckingA = false;
            }
            else if(Greenfoot.isKeyDown("D") && keyCheckingD == true)
            {
                if(battleMenu.equals("Pack"))
                {
                    movePackCursorRight();
                }
                keyCheckingD = false;
            }
        }
        else if(type.equals("StartMenu"))
        {
            if(Greenfoot.isKeyDown("W"))
            {
                if(defaultCursorYCoord > 48)
                {
                    defaultCursorYCoord = defaultCursorYCoord - 48;
                    drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                    canUpdate = true;
                }
                else
                {
                    defaultCursorYCoord = 144;
                    drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                    canUpdate = true;
                }
            }
            else if(Greenfoot.isKeyDown("S"))
            {
                if(defaultCursorYCoord < 144)
                {
                    defaultCursorYCoord = defaultCursorYCoord + 48;
                    drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                    canUpdate = true;
                }
                else
                {
                    defaultCursorYCoord = 48;
                    drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
                    canUpdate = true;
                }
            }
        }

        if(!Greenfoot.isKeyDown("W"))
        {
            keyCheckingW = true;
        }

        if(!Greenfoot.isKeyDown("A"))
        {
            keyCheckingA = true;
        }

        if(!Greenfoot.isKeyDown("S"))
        {
            keyCheckingS = true;
        }

        if(!Greenfoot.isKeyDown("D"))
        {
            keyCheckingD = true;
        }
    }

    /**
     * Move the cursor in the pkmn menu up.
     */
    public void movePkmnCursorUp()
    {
        if(pkmnCursorYCoord > 24)
        {
            pkmnCursorYCoord = pkmnCursorYCoord - 48;
            drawCursor("Black", pkmnCursorXCoord, pkmnCursorYCoord);
            canUpdate = true;
        }
        else if(pkmnCursorYCoord == 24)
        {
            pkmnCursorYCoord = ((Reader.getFileLength("playerPokemon") - 1) * 48) + 24;
            drawCursor("Black", pkmnCursorXCoord, pkmnCursorYCoord);
            canUpdate = true;
        }
    }

    /**
     * Move the cursor in the pkmn menu down.
     */
    public void movePkmnCursorDown()
    {
        pkmnCursorMovementLength = ((Reader.getFileLength("playerPokemon") - 1) * 48) + 24;
        if(pkmnCursorYCoord < pkmnCursorMovementLength)
        {
            pkmnCursorYCoord = pkmnCursorYCoord + 48;
            drawCursor("Black", pkmnCursorXCoord, pkmnCursorYCoord);
            canUpdate = true;
        }
        else if(pkmnCursorYCoord == pkmnCursorMovementLength)
        {
            pkmnCursorYCoord = 24;
            drawCursor("Black", pkmnCursorXCoord, pkmnCursorYCoord);
            canUpdate = true;
        }
    }

    /**
     * Move the cursor in the pack menu up.
     */
    public void movePackCursorUp()
    {
        if(!packSelection)
        {
            if(packCursorYCoord > 48)
            {
                packCursorYCoord = packCursorYCoord - 48;
                drawCursor("Red", packCursorXCoord, packCursorYCoord);
                canUpdate = true;
            }
            else if(packCursorYCoord == 48)
            {
                int moveLength = 0;

                for(int i = 0; i < Reader.getFileLength("player" + packMenu); i++)
                {
                    if(Reader.readIntFromFile("player" + packMenu, i, 1) > 0)
                    {
                        moveLength++;
                    }
                }

                packCursorYCoord = (moveLength * 48) + 48;
                drawCursor("Red", packCursorXCoord, packCursorYCoord);
                canUpdate = true;
            }
        }
        else
        {
            if(packSelectionCursorYCoord > 192)
            {
                packSelectionCursorYCoord = packSelectionCursorYCoord - 48;
                drawCursor("Black", packSelectionCursorXCoord, packSelectionCursorYCoord);
                canUpdate = true;
            }
            else if(packSelectionCursorYCoord == 192)
            {
                packSelectionCursorYCoord = 240;
                drawCursor("Black", packSelectionCursorXCoord, packSelectionCursorYCoord);
                canUpdate = true;
            }
        }
    }

    /**
     * Move the cursor in the pack menu down.
     */
    public void movePackCursorDown()
    {
        if(!packSelection)
        {
            int moveLength = 0;

            for(int i = 0; i < Reader.getFileLength("player" + packMenu); i++)
            {
                if(Reader.readIntFromFile("player" + packMenu, i, 1) > 0)
                {
                    moveLength++;
                }
            }

            packCursorMovementLength = (moveLength * 48) + 48;
            if(packCursorYCoord < packCursorMovementLength)
            {
                packCursorYCoord = packCursorYCoord + 48;
                drawCursor("Red", packCursorXCoord, packCursorYCoord);
                canUpdate = true;
            }
            else if(packCursorYCoord == packCursorMovementLength)
            {
                packCursorYCoord = 48;
                drawCursor("Red", packCursorXCoord, packCursorYCoord);
                canUpdate = true;
            }
        }
        else
        {
            if(packSelectionCursorYCoord < 240)
            {
                packSelectionCursorYCoord = packSelectionCursorYCoord + 48;
                drawCursor("Black", packSelectionCursorXCoord, packSelectionCursorYCoord);
                canUpdate = true;
            }
            else if(packSelectionCursorYCoord == 240)
            {
                packSelectionCursorYCoord = 192;
                drawCursor("Black", packSelectionCursorXCoord, packSelectionCursorYCoord);
                canUpdate = true;
            }
        }
    }

    /**
     * Move the cursor in the pack menu left.
     */
    public void movePackCursorLeft()
    {
        packSelection = false;
        packSelectionUpdating = false;
        if(packMenu.equals("Items"))
        {
            packMenu = "TMHM";
            canUpdate = true;
        }
        else if(packMenu.equals("Balls"))
        {
            packMenu = "Items";
            canUpdate = true;
        }
        else if(packMenu.equals("KeyItems"))
        {
            packMenu = "Balls";
            canUpdate = true;
        }
        else if(packMenu.equals("TMHM"))
        {
            packMenu = "KeyItems";
            canUpdate = true;
        }
        packCursorXCoord = 171;
        packCursorYCoord = 48;
    }

    /**
     * Move the cursor in the pkmn menu right.
     */
    public void movePackCursorRight()
    {
        packSelection = false;
        packSelectionUpdating = false;
        if(packMenu.equals("Items"))
        {
            packMenu = "Balls";
            canUpdate = true;
        }
        else if(packMenu.equals("Balls"))
        {
            packMenu = "KeyItems";
            canUpdate = true;
        }
        else if(packMenu.equals("KeyItems"))
        {
            packMenu = "TMHM";
            canUpdate = true;
        }
        else if(packMenu.equals("TMHM"))
        {
            packMenu = "Items";
            canUpdate = true;
        }
        packCursorXCoord = 171;
        packCursorYCoord = 48;
    }

    /**
     * Update the image of the box.
     */
    public void updateImage()
    {
        if(type.equals("BattleBar"))
        {
            getImage().clear();
            if(battleMenu.equals("Default"))
            {
                setLocation(240, 363);
                setImage("BattleSelectBox.png");
                drawWord("FIGHT", 240, 45);
                drawWord("PACK", 240, 93);
                drawLetter("PK", 384, 45);
                drawLetter("MN", 408, 45);
                drawWord("RUN", 384, 93);
                drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
            }
            else if(battleMenu.equals("Fight"))
            {
                drawFightMenu();
            }
            else if(battleMenu.equals("PkMn"))
            {
                drawPkmnMenu();
            }
            else if(battleMenu.equals("Pack"))
            {
                drawPackMenu();
            }
            else if(battleMenu.equals("Run"))
            {
                setLocation(240, 363);
                setImage("TextBox.png");

                if(canRunAway)
                {
                    displayText("Got away safely!", true, false);
                    BattleWorld battleWorld = (BattleWorld) getWorld();
                    battleWorld.savePokemonData();
                    battleWorld.stopMusic();
                    battleWorld.newGameWorld();
                }
                else
                {
                    displayText("Can't escape!", true, false);
                    battleMenu = "Default";

                    BattleWorld battleWorld = (BattleWorld) getWorld();
                    Pokemon playerPokemon = battleWorld.getPlayerPokemon();
                    battleWorld.changeTurn();
                }
            }
        }
        else if(type.equals("MenuBox"))
        {
            getImage().clear();

            if(battleMenu.equals("Default"))
            {
                setLocation(240, 216);
                setImage("MenuBox.png");

                drawWord("POKéMON", 288, 48);
                drawWord("PACK", 288, 96);
                drawWord("SAVE", 288, 144);
                drawWord("EXIT", 288, 192);

                GreenfootImage descriptions = new GreenfootImage(240, 113);
                descriptions.drawImage(new GreenfootImage("Menu_Descriptions.png"), 0, ((defaultCursorYCoord / 48) - 1) * -113);
                getImage().drawImage(descriptions, 0, 319);

                drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
            }
            else if(battleMenu.equals("PkMn"))
            {
                drawPkmnMenu();
            }
            else if(battleMenu.equals("Pack"))
            {
                drawPackMenu();
            }
        }
        else if(type.equals("StartMenu"))
        {
            getImage().clear();

            setLocation(240, 216);
            setImage("StartMenu.png");

            drawWord("CONTINUE", 48, 48);
            drawWord("NEW GAME", 48, 96);
            drawWord("QUIT", 48, 144);

            drawCursor("Black", defaultCursorXCoord, defaultCursorYCoord);
        }
    }

    /**
     * Draws the fight menu.
     */
    public void drawFightMenu()
    {
        setLocation(240, 315);
        setImage("BattleSelectBox_Attack.png");

        String moveName = "";
        String moveType = "";
        int currentMoveID = 0;
        int currentPP = 0;
        int maxPP = 0;

        BattleWorld battleWorld = (BattleWorld) getWorld();
        currentPokemon = battleWorld.getCurrentPlayerPokemon();

        for(int i = 5; i < 9; i++)
        {
            String tempName = Reader.readStringFromFile("playerPokemon", currentPokemon, i);
            if(!tempName.equals("-") && !tempName.equals("null"))
            {
                moveName = tempName.toUpperCase();
                currentMovesList[i - 5] = moveName;
                drawWord(moveName, 144, 116 + ((i - 5) * 24));
            }
            else
            {
                currentMovesList[i - 5] = null;
            }
        }

        switch(fightCursorYCoord)
        {
            case 117: currentMoveID = 0;
            break;
            case 141: currentMoveID = 1;
            break;
            case 165: currentMoveID = 2;
            break;
            case 189: currentMoveID = 3;
            break;
        }

        movesRead:
        for(int m = 0; m < (Reader.getFileLength("movesList")); m++)
        {
            if(!(currentMovesList[currentMoveID] == null))
            {
                if(currentMovesList[currentMoveID].equals((Reader.readStringFromFile("movesList", m, 0)).toUpperCase()))
                {
                    if(currentMovesList[currentMoveID] != null)
                    {
                        moveType = (Reader.readStringFromFile("movesList", m, 1)).toUpperCase();
                        currentPP = Reader.readIntFromFile("playerPokemon", currentPokemon, currentMoveID + 9);
                        maxPP = Reader.readIntFromFile("movesList", m, 4);
                        break movesRead;                            
                    }
                }
            }
        }

        drawWord(moveType, 48, 45);
        drawNumber(currentPP, 120, 66, true);
        drawNumber(maxPP, 192, 66, true);

        drawCursor("Black", fightCursorXCoord, fightCursorYCoord);
    }

    /**
     * Draws the pack menu.
     */
    public void drawPackMenu()
    {
        setLocation(240, 216);
        setImage("BattleSelectBox_Pack.png");
        GreenfootImage packInfo = new GreenfootImage(120, 168);

        int menuID = 0;
        for(int i = 0; i < PACK_MENUS.length; i++)
        {
            if(packMenu.equals(PACK_MENUS[i]))
            {
                menuID = i;
                break;
            }
        }
        packInfo.drawImage(new GreenfootImage("PackIcons.png"), menuID * -120, 0);
        getImage().drawImage(packInfo, 0, 72);

        int tempPokemonNum = 0;
        int readLength = Reader.getFileLength("player" + packMenu);
        for(int i = 0; i < readLength; i++)
        {
            String tempItemName = Reader.readStringFromFile("player" + packMenu, i, 0).toUpperCase();
            int tempItemAmount = Reader.readIntFromFile("player" + packMenu, i, 1);
            String tempItemDesc = Reader.readStringFromFile("player" + packMenu, i, 2);

            if(tempItemAmount > 0)
            {
                drawWord(tempItemName, 192, 48 + (i * 48));
                drawLetter("×", 408, 69 + (i * 48));
                drawNumber(tempItemAmount, 480, 69 + (i * 48), false);
                drawTextBox(tempItemDesc, 24, 336);

                tempPokemonNum++;
            }
        }

        drawWord("CANCEL", 192, 48 + 48 * tempPokemonNum);
        if(!packSelection)
        {
            drawCursor("Red", packCursorXCoord, packCursorYCoord);
        }
        else
        {
            drawCursor("Red_Hollow", packCursorXCoord, packCursorYCoord);

            getImage().drawImage(new GreenfootImage("BattleSelectBox_Pack_Select.png"), 0, 168);
            drawCursor("Black", packSelectionCursorXCoord, packSelectionCursorYCoord);
        }
    }

    /**
     * Draws the pkmn menu.
     */
    public void drawPkmnMenu()
    {
        setLocation(240, 216);
        setImage("BattleSelectBox_PkMn.png");

        String tempPokemonName;
        int tempLevel;
        int tempCurrentHealth;
        int tempMaxHealth;

        for(int i = 1; i < Reader.getFileLength("playerPokemon"); i++)
        {
            tempPokemonName = Reader.readStringFromFile("PlayerPokemon", i, 0);
            tempLevel = Reader.readIntFromFile("PlayerPokemon", i, 1);
            pokemonPartyMaxHealths[i] = StatCalculator.maxHealthCalc(tempPokemonName, tempLevel);
        }

        int tempPokemonNum = 0;
        for(int i = 1; i < Reader.getFileLength("playerPokemon"); i++)
        {
            tempPokemonName = Reader.readStringFromFile("PlayerPokemon", i, 0).toUpperCase();
            tempLevel = Reader.readIntFromFile("PlayerPokemon", i, 1);
            tempCurrentHealth = Reader.readIntFromFile("PlayerPokemon", i, 3);
            tempMaxHealth = pokemonPartyMaxHealths[i];

            if(tempCurrentHealth > tempMaxHealth)
            {
                tempCurrentHealth = tempMaxHealth;
            }

            drawPartyStats(tempLevel, tempCurrentHealth, tempMaxHealth, 174, 24 + ((i - 1) * 48));
            drawStatusBar("Health", 312, 57 + ((i - 1) * 48), 144, 6, tempCurrentHealth, tempMaxHealth);
            drawIcon(tempPokemonName, 27, 14 + ((i - 1) * 48)); 
            drawWord(tempPokemonName, 72, 24 + ((i - 1) * 48));
            tempPokemonNum++;
        }
        drawWord("CANCEL", 24, 24 + 48 * tempPokemonNum);

        drawCursor("Black", pkmnCursorXCoord, pkmnCursorYCoord);
    }

    /**
     * Draw a Pokemon icon at a location.
     * @param pokemonName The name of the Pokemon.
     * @param x The X coordinate of the icon.
     * @param y The Y coordinate of the icon.
     */
    public void drawIcon(String pokemonName, int x, int y)
    {        
        try
        {
            List<String> list = Files.readAllLines(Paths.get("pokemonIconPositions.txt"), StandardCharsets.UTF_8);
            String[] pokemonIconList = list.toArray(new String[list.size()]);

            GreenfootImage allPokemonIcons = new GreenfootImage("PokemonIcons.png");
            GreenfootImage newImage = new GreenfootImage(44, 44);
            for(int listIndex = 0; listIndex < pokemonIconList.length; listIndex++)
            {
                if((pokemonIconList[listIndex]).toUpperCase().contains(pokemonName))
                {
                    if(listIndex % 8 == 0)
                    {
                        newImage.drawImage(allPokemonIcons, (listIndex / 8) * -44, 0);
                    }
                    else
                    {
                        int yPos = (listIndex % 8);
                        int xPos = (listIndex - yPos) / 8;
                        newImage.drawImage(allPokemonIcons, xPos * -44, yPos * -44);
                    }
                    getImage().drawImage(newImage, x, y);
                    break;
                }
            }
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
    }

    /**
     * Draw the stats of the player's Pokemon party.
     * @param level The Pokemon's level.
     * @param currentHp The Pokemon's current health.
     * @param maxHp The Pokemon's max health.
     * @param x The X coordinate to draw at.
     * @param y The Y coordinate to draw at.
     */
    public void drawPartyStats(int level, int currentHp, int maxHp, int x, int y)
    {
        GreenfootImage statsImage = new GreenfootImage("BattleSelectBox_PkMn_Details.png");
        this.getImage().drawImage(new GreenfootImage(statsImage), x, y);
        drawNumber(level, 195, y + 21, true);
        drawNumber(currentHp, 384, y - 3, false);
        drawNumber(maxHp, 480, y - 3, false);
    }

    /**
     * Returns the current battle menu.
     */
    public String getCurrentMenu()
    {
        return battleMenu;
    }
}