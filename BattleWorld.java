import greenfoot.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.FileWriter;

/**
 * The battle world of the game.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class BattleWorld extends Worlds
{
    //The id's of the pokemon
    private int currentEnemyPokemon = 1;
    private int currentPlayerPokemon = 1;

    //The pokemon objects
    Pokemon playerPokemon = new Pokemon(false);
    Pokemon enemyPokemon = new Pokemon(false);

    //The pokemon names
    private String playerPokemonName = "";
    private String enemyPokemonName = "";

    //The pokemon info boxes
    PkmnInfoBox playerPkmnInfoBox = new PkmnInfoBox("PlayerPkmnInfo");
    PkmnInfoBox enemyPkmnInfoBox = new PkmnInfoBox("EnemyPkmnInfo");

    TextInfoBox textInfoBox = new TextInfoBox("BattleSelectBox"); //The box at the bottom of the screen

    private boolean wildPokemon = false; //If the battle is against a wild Pokemon or not
    private static boolean isPlayersTurn = true; //Whose turn it is
    private static boolean isBattleOver = false; //If the battle is over

    private int worldTimer = 0; //A timer, so info can be displayed at the beginning of every battle

    /**
     * Creates a new battle against a trainer, reading in a Pokemon from a file.
     * @param enemyPokemonID The id of the enemy Pokemon.
     */
    public BattleWorld(int enemyPokemonID)
    {
        wildPokemon = false;
        isBattleOver = false;
        initWorld("", 0, enemyPokemonID);
    }

    /**
     * Creates a battle against a wild Pokemon.
     * @param pokemonName The name of the wild Pokemon.
     * @param level The level of the wild Pokemon.
     */
    public BattleWorld(String pokemonName, int level)
    {
        wildPokemon = true;
        isBattleOver = false;
        initWorld(pokemonName, level, 0);
    }

    /**
     * Initialises the new battle.
     * @param inName The name of the enemy Pokemon.
     * @param inLevel The level of the enemy Pokemon.
     * @param id The id of the enemy Pokemon.
     */
    public void initWorld(String inName, int inLevel, int id)
    {
        setBackground(248);

        addObject(playerPkmnInfoBox, 334, 229);
        addObject(enemyPkmnInfoBox, 144, 49);

        currentEnemyPokemon = id;

        addNewPokemon("Player", currentPlayerPokemon, "", 0);
        if(wildPokemon)
        {
            addNewPokemon("Enemy", currentEnemyPokemon, inName, inLevel);
        }
        else
        {
            addNewPokemon("Enemy", currentEnemyPokemon, "", 0);
        }

        this.textInfoBox = new TextInfoBox("BattleSelectBox", "BattleBar", currentPlayerPokemon);
        addObject(textInfoBox, getWidth() / 2, getHeight() - 69); //Battle Text Box

        newMusic("Battle");
        Greenfoot.setSpeed(50);
        setPaintOrder(Blackout.class, TextInfoBox.class, PkmnInfoBox.class, Pokemon.class);
    }

    /**
     * Adds a new Pokemon to the world.
     * @param type The type of Pokemon (Player or Enemy).
     * @param index The index of the Pokemon.
     * @param tempName The name of the Pokemon.
     * @param tempLevel The level of the Pokemon.
     */
    public void addNewPokemon(String type, int index, String tempName, int tempLevel)
    {
        if(type.equals("Player"))
        {
            if(getObjects(Pokemon.class).size() == 2)
            {
                savePokemonData(playerPokemon, currentPlayerPokemon);
                removeObject(playerPokemon);
            }

            for(int i = 0; i < 6; i++)
            {
                if(Reader.readIntFromFile("playerPokemon", index, 3) > 0)
                {
                    makePlayerPokemon(index);
                    break;
                }
                index++;
            }

            playerPokemonName = playerPokemon.getName();

            addObject(playerPokemon, getWidth() / 4, getHeight() / 2 - 12);

            if(getObjects(Pokemon.class).size() == 2)
            {
                playerPkmnInfoBox.drawAll();
            }
        }
        else if(type.equals("Enemy"))
        {
            if(getObjects(Pokemon.class).size() == 2)
            {
                removeObject(enemyPokemon);
            }

            if(!wildPokemon)
            {
                String enemyPkmnName = Reader.readStringFromFile("pokeMonListBattle", index, 0);
                int enemyPkmnLevel = Reader.readIntFromFile("pokeMonListBattle", index, 1);
                boolean enemyPkmnGender = Reader.readBooleanFromFile("pokeMonListBattle", index, 2);
                int enemyPkmnCurrentHealth = Reader.readIntFromFile("pokeMonListBattle", index, 3);
                this.enemyPokemon = new Pokemon(currentEnemyPokemon, enemyPkmnName, enemyPkmnLevel, enemyPkmnGender, enemyPkmnCurrentHealth, 0, false);

                enemyPokemonName = enemyPkmnName;

                addObject(enemyPokemon, 372, 84);
            }
            else
            {
                this.enemyPokemon = new Pokemon(currentEnemyPokemon, tempName, tempLevel, getRandomBoolean(), 9999, 0, false);
                enemyPokemonName = tempName;
                addObject(enemyPokemon, 372, 84);
            }
        }
    }

    /**
     * Creates a new player Pokemon.
     * @param index The index of the new Pokemon.
     */
    public void makePlayerPokemon(int index)
    {
        String playerPkmnName = Reader.readStringFromFile("playerPokemon", index, 0);
        int playerPkmnLevel = Reader.readIntFromFile("playerPokemon", index, 1);
        boolean playerPkmnGender = Reader.readBooleanFromFile("playerPokemon", index, 2);
        int playerPkmnCurrentHealth = Reader.readIntFromFile("playerPokemon", index, 3);
        int playerPkmnExp = Reader.readIntFromFile("playerPokemon", index, 4);
        this.playerPokemon = new Pokemon(currentPlayerPokemon, playerPkmnName, playerPkmnLevel, playerPkmnGender, playerPkmnCurrentHealth, playerPkmnExp, true);
    }

    /**
     * The act method of the world.
     * 
     * <p> Does the following:
     * <p> -Shows intro text.
     * <p> -Shows outro text.
     * <p> -Goes back to GameWorld at the end of a battle.
     */
    public void act()
    {
        if(worldTimer == 1)
        {
            if(wildPokemon)
            {
                textInfoBox.displayText("Wild " + enemyPokemon.getName() + " appeared!", true, false);
            }
            else
            {
                textInfoBox.displayText("Enemy trainer sent out " + enemyPokemon.getName() + "!", true, false);
            }
            textInfoBox.displayText("Go " + playerPokemon.getName() + "!", true, false);
            textInfoBox.updateImage();

            worldTimer++;
        }
        else if(worldTimer == 0)
        {
            worldTimer++;
        }

        if(getObjects(Pokemon.class).size() < 2)
        {
            ArrayList<Pokemon> pokemon = (ArrayList<Pokemon>) getObjects(Pokemon.class);
            for(Pokemon pkmn : pokemon)
            {
                if(pkmn.getIsPlayers() == true)
                {
                    textInfoBox.displayText("Enemy " + enemyPokemonName.toUpperCase() + " fainted!", true, false);

                    int expGain = StatCalculator.experienceGainCalc(playerPokemon.getName(), playerPokemon.getLevel());
                    textInfoBox.displayText(playerPokemon.getName().toUpperCase() + " gained " + expGain + "\nEXP. Points!", true, false);
                    playerPokemon.addEXP(expGain);
                    if(playerPokemon.getCurrentExperience() >= StatCalculator.experienceToNextLevel(playerPokemon.getLevel()))
                    {
                        int newEXP = playerPokemon.getCurrentExperience() - StatCalculator.experienceToNextLevel(playerPokemon.getLevel());
                        playerPokemon.setEXP(newEXP);
                        playerPokemon.levelUp();
                        playerPokemon.newPokemonMoves();

                        textInfoBox.displayText(playerPokemonName.toUpperCase() + " grew to level " + playerPokemon.getLevel() + "!", true, false);
                    }
                    savePokemonData(playerPokemon, currentPlayerPokemon);

                    textInfoBox.updateImage();
                    isPlayersTurn = true;
                    battleOver();
                }
                else
                {
                    textInfoBox.displayText(playerPokemonName.toUpperCase() + " fainted!", true, false);

                    String playerName = Reader.readStringFromFile("gameInformation", 0, 0);
                    textInfoBox.displayText(playerName + " blacked out!", true, false);

                    addObject(new Blackout("Fade"), getWidth() / 2, getHeight() / 2);
                    removeAllObjects();

                    String newWorld = Reader.readStringFromFile("gameInformation", 0, 4);
                    int newX = Reader.readIntFromFile("gameInformation", 0, 5);
                    int newY = Reader.readIntFromFile("gameInformation", 0, 6);

                    GameWorld gameWorld = new GameWorld(newWorld, newX, newY, "Down");
                    gameWorld.healPokemon();
                    Greenfoot.setWorld(gameWorld);

                    isPlayersTurn = true;
                    battleOver();
                }
            }
        }        

        if(isBattleOver)
        {
            isPlayersTurn = true;
            savePokemonData(playerPokemon, currentPlayerPokemon);
            newGameWorld();
        }
    }

    /**
     * Sets the battle to be over.
     */
    public void battleOver()
    {
        isBattleOver = true;
    }

    /**
     * Changes the turn of the player's Pokemon.
     */
    public void changeTurn()
    {
        isPlayersTurn = !isPlayersTurn;
    }

    /**
     * Changes the world to GameWorld.
     */
    public void newGameWorld()
    {
        addObject(new Blackout("Fade"), getWidth() / 2, getHeight() / 2);
        removeAllObjects();

        GameWorld gameWorld = new GameWorld();
        Greenfoot.setWorld(gameWorld);
    }

    /**
     * Removes the player info box.
     */
    public void removePlayerPkmnInfoBox()
    {
        removeObject(playerPkmnInfoBox);
    }

    /**
     * Saves the data of the current Pokemon.
     */
    public void savePokemonData()
    {
        try
        {
            int currentPokemon = playerPokemon.getCurrentPokemon();

            FileWriter fw = new FileWriter("tempPlayerPokemon.txt");

            List<String> list = Files.readAllLines(Paths.get("playerPokemon.txt"), StandardCharsets.UTF_8);
            String[] pokemonList = list.toArray(new String[list.size()]);

            String currentPokemonStr = pokemonList[currentPokemon];
            String[] currentPokemonArray = currentPokemonStr.split("\\s*,\\s*");

            currentPokemonArray[1] = Integer.toString(playerPokemon.getLevel());
            currentPokemonArray[3] = Integer.toString(playerPokemon.getCurrentHealth());
            currentPokemonArray[4] = Integer.toString(playerPokemon.getCurrentExperience());
            for(int c = 0; c < 4; c++)
            {
                currentPokemonArray[5 + c] = playerPokemon.getMove(c);
            }

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
        }
        catch(Exception error)
        {
        }
    }

    /**
     * Sets the player Pokemon object to null.
     */
    public void setPlayerPkmnNull()
    {
        playerPokemon = null;
    }

    /**
     * Sets the enemy Pokemon object to null.
     */
    public void setEnemyPkmnNull()
    {
        enemyPokemon = null;
    }

    /**
     * Changes the current player Pokemon.
     * @param newID The id of the new Pokemon.
     */
    public void setCurrentPlayerPokemon(int newID)
    {
        currentPlayerPokemon = newID;
    }

    /**
     * Returns the current player Pokemon.
     */
    public int getCurrentPlayerPokemon()
    {
        return currentPlayerPokemon;
    }

    /**
     * Returns reference to the player Pokemon.
     */
    public Pokemon getPlayerPokemon()
    {
        return playerPokemon;
    }

    /**
     * Returns reference to the enemy Pokemon.
     */
    public Pokemon getEnemyPokemon()
    {
        return enemyPokemon;
    }

    /**
     * Returns reference to the player Pokemon info box.
     */
    public PkmnInfoBox getPlayerInfoBox()
    {
        return playerPkmnInfoBox;
    }

    /**
     * Returns reference to the enemy Pokemon info box.
     */
    public PkmnInfoBox getEnemyInfoBox()
    {
        return enemyPkmnInfoBox;
    }

    /**
     * Returns reference to the text info box.
     */
    public TextInfoBox getTextInfoBox()
    {
        return textInfoBox;
    }

    /**
     * Returns of the battle is over or not.
     */
    public boolean getIsBattleOver()
    {
        return isBattleOver;
    }

    /**
     * Returns if it is the player's turn or not.
     */
    public static boolean getIsPlayersTurn()
    {
        return isPlayersTurn;
    }

    /**
     * Returns a random boolean.
     */
    public boolean getRandomBoolean()
    {
        return Math.random() < 0.5;
    }

    /**
     * Returns if the battle is against a wild pokemon or not.
     */
    public boolean getWildPokemon()
    {
        return wildPokemon;
    }
}