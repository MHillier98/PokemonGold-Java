import greenfoot.*;
import java.io.FileWriter;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The Pokemon class, to allow adding of a Pokemon object in the BattleWorld.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class Pokemon extends Battle
{
    private int currentPokemon; //A counter for which pokemon is in play

    //Displayed info
    private String name;
    private int level;
    private boolean gender; //True = male, False = female

    //The Pokemon's stats
    private int currentHealth;
    private int currentExperience;
    private int maxHealth; //aka HP
    private int attack;
    private int defence;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private String type[];

    private String currentMovesList[] = new String[4]; //The Pokemon's moves

    //Determines if it is the back or front of the pkmn
    private boolean isPlayers; //True = player's, False = enemy's
    private boolean isPlayersTurn; //True = player's, False = enemy's

    /**
     * Constructs a new Pokemon.
     * @param inCurrentPokemon A counter for which Pokemon is in play.
     * @param inName The Pokemon's name.
     * @param inLevel The Pokemon's level.
     * @param inGender The Pokemon's gender.
     * @param inCurrentHealth The Pokemon's current health.
     * @param inExperience The Pokemon's current experience.
     * @param inIsPlayers Whether the Pokemon is the player's or not.
     */
    public Pokemon(int inCurrentPokemon, String inName, int inLevel, boolean inGender, int inCurrentHealth, int inExperience, boolean inIsPlayers)
    {
        currentPokemon = inCurrentPokemon;
        name = inName;
        level = inLevel;
        gender = inGender;
        currentHealth = inCurrentHealth;
        maxHealth = StatCalculator.maxHealthCalc(name, level);

        if(currentHealth < 0 || currentHealth > maxHealth)
        {
            currentHealth = maxHealth;
        }

        currentExperience = inExperience;

        int stats[] = StatCalculator.generalStatsCalc(name, level);
        attack = stats[0];
        defence = stats[1];
        specialAttack = stats[2];
        specialDefense = stats[3];
        speed = stats[4];

        isPlayers = inIsPlayers;

        type = StatCalculator.determineType(name);

        drawPokemon(inIsPlayers);

        if(isPlayers)
        {
            isPlayersTurn = true;

            for(int i = 5; i < 9; i++)
            {
                String tempName = Reader.readStringFromFile("playerPokemon", currentPokemon, i);
                if(!tempName.equals("-") && !tempName.equals("null"))
                {
                    currentMovesList[i - 5] = (Reader.readStringFromFile("playerPokemon", currentPokemon, i)).toUpperCase();
                }
                else
                {
                    break;
                }
            }
        }
        else
        {
            isPlayersTurn = false;
            newPokemonMoves();
        }
    }

    /**
     * A temporary constructor for when the Pokemon are first initialised.
     */
    public Pokemon(Boolean isGeneric)
    {
    }

    /**
     * The act method for the Pokemon.
     * 
     * This does:
     * <p> -Removes the Pokemon when it has no health.
     * <p> -AI for enemy Pokemon.
     */
    public void act()
    {
        if(currentHealth <= 0)
        {
            currentHealth = 0;

            BattleWorld battleWorld = (BattleWorld) getWorld();
            if(isPlayers)
            {
                battleWorld.savePokemonData();
                battleWorld.setPlayerPkmnNull();
            }
            else
            {
                battleWorld.setEnemyPkmnNull();
            }

            battleWorld.removeObject(this);
            battleWorld.removePlayerPkmnInfoBox();
        }

        if(!isPlayers)
        {
            BattleWorld battleWorld = (BattleWorld) getWorld();

            try
            {
                if(!battleWorld.getIsBattleOver())
                {
                    if(!battleWorld.getIsPlayersTurn())
                    {
                        int maxRandomNum = 0;
                        for(int i = 0; i < currentMovesList.length; i++)
                        {
                            if(currentMovesList[i] != null && !currentMovesList[i].equals("-"))
                            {
                                maxRandomNum++;
                            }
                        }
                        int randomNum = Greenfoot.getRandomNumber(maxRandomNum);
                        String currentMove = currentMovesList[randomNum];

                        inflictDamage(currentMove);
                        battleWorld.changeTurn();
                    }
                }
            }
            catch(Exception error)
            {
                return;
            }
        }
    }

    /**
     * Draw the Pokemon image.
     * @param isPlayers If it is the player's Pokemon or not.
     */
    public void drawPokemon(boolean isPlayers)
    {
        int pokemonID = Reader.getPokemonID(name);

        if(!isPlayers)
        {
            GreenfootImage newImage = new GreenfootImage(168, 168);

            if(pokemonID % 10 == 0)
            {
                newImage.drawImage(new GreenfootImage("AllPokemon.png"), 9 * -168, ((pokemonID / 10) - 1) * -168);
            }
            else
            {
                int newX = (pokemonID % 10 - 1);
                int newY = (pokemonID - newX) / 10;
                newImage.drawImage(new GreenfootImage("AllPokemon.png"), newX * -168, newY * -168);
            }
            setImage(newImage);
        }
        else
        {
            GreenfootImage newImage = new GreenfootImage(159, 168);

            if(pokemonID % 10 == 0)
            {
                newImage.drawImage(new GreenfootImage("BackSprites.png"), 9 * -159, ((pokemonID / 10) - 1) * -168);
            }
            else
            {
                int newX = (pokemonID % 10) - 1;
                int newY = (pokemonID - newX) / 10;
                newImage.drawImage(new GreenfootImage("BackSprites.png"), newX * -159, newY * -168);
            }
            setImage(newImage);
        }
    }

    /**
     * Deal damage to the other pokemon.
     * @param moveName The name of the move used.
     */
    public void inflictDamage(String moveName)
    {
        BattleWorld battleWorld = (BattleWorld) getWorld();

        Pokemon otherPokemon = null;
        PkmnInfoBox otherPkmnInfoBox = null;
        if(isPlayers)
        {
            otherPokemon = battleWorld.getEnemyPokemon();
            otherPkmnInfoBox = battleWorld.getEnemyInfoBox();
        }
        else
        {
            otherPokemon = battleWorld.getPlayerPokemon();
            otherPkmnInfoBox = battleWorld.getPlayerInfoBox();
        }

        String enemyDescriptor = "";
        TextInfoBox textInfoBox = battleWorld.getTextInfoBox();
        if(otherPokemon.getIsPlayers())
        {
            enemyDescriptor = "Enemy ";
        }
        textInfoBox.displayText(enemyDescriptor + name.toUpperCase() + " used " + moveName + "!", false, false);

        String moveType = "";
        int damageAmount = 0;
        String effect = "";
        for(int m = 0; m < Reader.getFileLength("movesList"); m++)
        {
            try
            {
                if((moveName.toUpperCase()).equals((Reader.readStringFromFile("movesList", m, 0)).toUpperCase()))
                {
                    moveType = Reader.readStringFromFile("movesList", m, 1);
                    damageAmount = Reader.readIntFromFile("movesList", m, 2);
                    effect = Reader.readStringFromFile("movesList", m, 5);
                }
            }
            catch(Exception error)
            {
                System.out.println(error.getMessage());
            }
        }

        if(damageAmount > 0)
        {
            double damage1 = (2.0 * this.level + 10.0) / 250.0;
            double damage2 = this.attack + 0.0 / otherPokemon.getDefence();
            double damage3 = damageAmount + 0.0;
            double damageTotalOne = damage1 * damage2 * damage3;

            double damageModifier = 1.0;

            double stab = 1.0; //Same-Type Attack Bonus

            if(moveType.equals(type))
            {
                stab = 1.5;
            }

            double typeEffectiveness = 1.0; //Can be either 0, 0.25, 0.5, 1, or 2

            typeRead:
            for(int i = 0; i < otherPokemon.getTypeLength(); i++)
            {
                String tempEnemyType = otherPokemon.getType(i);

                typeMod:
                for(int t = 1; t < Reader.getFileLength("typeModifiers"); t++)
                {
                    String tempPlayerTypeMod = Reader.readStringFromFile("typeModifiers", t, 0);
                    if(type.equals(tempPlayerTypeMod))
                    {
                        String tempEnemyTypeMod = Reader.readStringFromFile("typeModifiers", t, 0);
                        if(tempEnemyType.equals(tempEnemyTypeMod))
                        {
                            typeEffectiveness = Reader.readDoubleFromFile("typeModifiers", t, 2);
                        }
                    }
                }
            }

            int randomMod1 = Greenfoot.getRandomNumber(16) + 85;
            double randomMod = (randomMod1 / 100.0) + 1.0;

            damageModifier = stab * typeEffectiveness * randomMod;

            double damageTotalTwo = damageTotalOne * damageModifier;
            int totalDamage = Integer.valueOf((int) Math.round(damageTotalTwo)) / 8;

            if(effect.equals("Multi"))
            {
                int randomMulti = Greenfoot.getRandomNumber(6);
                totalDamage *= randomMulti;
                textInfoBox.displayText("Hit " + randomMulti + " times.", true, false);
            }

            otherPokemon.takeDamage(totalDamage);
            otherPkmnInfoBox.toggleCanUpdate();

            if(typeEffectiveness == 0.5)
            {
                textInfoBox.displayText("It wasn't very effective.", true, false);
            }
            else if(typeEffectiveness == 2.0)
            {
                textInfoBox.displayText("It was super effective!", true, false);
            }
        }
        else
        {
            if(effect.equals("Defence-"))
            {
                otherPokemon.changeStat("Defence", -(level / 4));
                textInfoBox.displayText(enemyDescriptor + otherPokemon.getName().toUpperCase() + "'s DEFENSE fell!", true, false);
            }
            else if(effect.equals("Defence+"))
            {
                changeStat("Defence", (level / 4));
                textInfoBox.displayText(enemyDescriptor + name.toUpperCase() + "'s DEFENSE rose!", true, false);
            }
            else if(effect.equals("Attack-"))
            {
                otherPokemon.changeStat("Attack", -(level + 1 / 4));
                textInfoBox.displayText(enemyDescriptor + otherPokemon.getName().toUpperCase() + "'s ATTACK fell!", true, false);
            }
            else if(effect.equals("Attack+"))
            {
                changeStat("Attack", (level + 1 / 4));
                textInfoBox.displayText(enemyDescriptor + name.toUpperCase() + "'s ATTACK rose!", true, false);
            }
        }

        textInfoBox.updateImage();
        otherPkmnInfoBox.drawAll();
    }

    /**
     * Take a certain amount of damage.
     * @param damageAmount The amount of health to lose.
     */
    public void takeDamage(int damageAmount)
    {
        BattleWorld battleWorld = (BattleWorld) getWorld();
        if(damageAmount < 0)
        {
            damageAmount -= (damageAmount * 2);
        }
        this.currentHealth = currentHealth - damageAmount;

        if(isPlayers)
        {
            PkmnInfoBox enemyInfoBox = battleWorld.getPlayerInfoBox();
            enemyInfoBox.toggleCanUpdate();
        }
        else
        {
            PkmnInfoBox playerInfoBox = battleWorld.getEnemyInfoBox();
            playerInfoBox.toggleCanUpdate();
        }
    }

    /**
     * Increase the Pokemon's health by a certain amount.
     * @param healAmount The amount of health to increase by.
     */
    public void heal(int healAmount)
    {
        BattleWorld battleWorld = (BattleWorld) getWorld();

        if(healAmount > 0)
        {
            if(currentHealth <= maxHealth - 20)
            {
                currentHealth = currentHealth + healAmount;
            }
            else if(currentHealth > maxHealth - 20)
            {
                currentHealth = maxHealth;
            }

            if(isPlayers)
            {
                PkmnInfoBox enemyInfoBox = battleWorld.getPlayerInfoBox();
                enemyInfoBox.toggleCanUpdate();
            }
            else
            {
                PkmnInfoBox playerInfoBox = battleWorld.getEnemyInfoBox();
                playerInfoBox.toggleCanUpdate();
            }
        }
    }

    /**
     * Increase or decrease a Pokemon's stat.
     * @param stat The stat to change.
     * @param amount The difference to change by (can be positive or negative).
     */
    public void changeStat(String stat, int amount)
    {
        if(stat.equals("Defence"))
        {
            defence += amount;
        }
        else if(stat.equals("Attack"))
        {
            attack += amount;
        }
    }

    /**
     * Generate a new moveset for a Pokemon.
     */
    public void newPokemonMoves()
    {
        pokemonMovesLoop:
        for(int i = 0; i < Reader.getFileLength("pokemonLevelsMoves"); i++)
        {
            String tempPokemonName = Reader.readStringFromFile("pokemonLevelsMoves", i, 0);

            if(tempPokemonName.equals(name))
            {
                try
                {
                    List<String> list = Files.readAllLines(Paths.get("pokemonLevelsMoves.txt"), StandardCharsets.UTF_8);
                    String[] allMovesList = list.toArray(new String[list.size()]);

                    String currentMovesListStr = allMovesList[i];
                    String[] movesArray = currentMovesListStr.split("\\s*,\\s*");

                    String[] newMovesArray = Arrays.copyOfRange(movesArray, 1, movesArray.length);

                    int moveID = 0;
                    for(int m = 0; m < newMovesArray.length; m++)
                    {
                        String strTempMoveLevel = newMovesArray[m].substring(0, 2);
                        int intTempMoveLevel = Integer.parseInt(strTempMoveLevel);

                        if(moveID < 4)
                        {
                            if(intTempMoveLevel <= level)
                            {
                                if(!(Arrays.asList(currentMovesList).contains(newMovesArray[m].substring(3))))
                                {
                                    currentMovesList[moveID] = (newMovesArray[m].substring(3));
                                    moveID++;
                                }
                            }
                            else
                            {
                                break pokemonMovesLoop;
                            }
                        }
                        else
                        {
                            moveID = 0;
                        }
                    }
                }
                catch(Exception error)
                {
                    break pokemonMovesLoop;
                }
            }
        }
    }

    /**
     * Returns the Pokemon's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the Pokemon's level.
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Returns the Pokemon's gender.
     */
    public boolean getGender()
    {
        return gender;
    }

    /**
     * Returns the Pokemon's current health.
     */
    public int getCurrentHealth()
    {
        return currentHealth;
    }

    /**
     * Returns the Pokemon's max health.
     */
    public int getMaxHealth()
    {
        return maxHealth;
    }

    /**
     * Returns the Pokemon's current EXP.
     */
    public int getCurrentExperience()
    {
        return currentExperience;
    }

    /**
     * Returns a list of the Pokemon's current moves.
     */
    public String[] getMovesNames()
    {
        return currentMovesList;
    }

    /**
     * Returns the PP of each of the Pokemon's current moves.
     */
    public int[] getMovesPP()
    {
        int tempPPs[] = new int[4];
        for(int i = 0; i < tempPPs.length; i++)
        {
            for(int m = 0; m < Reader.getFileLength("movesList"); m++)
            {
                if((Reader.readStringFromFile("movesList", m, 0)).equals(currentMovesList[i]))
                {
                    tempPPs[i] = Reader.readIntFromFile("movesList", m, 4);
                }
            }
        }
        return tempPPs;
    }

    /**
     * Returns the Pokemon's defence stat.
     */
    public int getDefence()
    {
        return defence;
    }

    /**
     * Returns whether the Pokemon is the player's or not.
     */
    public boolean getIsPlayers()
    {
        return isPlayers;
    }

    /**
     * Returns how many types the Pokemon is.
     */
    public int getTypeLength()
    {
        return type.length;
    }

    /**
     * Returns the Pokemon's types.
     */
    public String getType(int index)
    {
        return type[index];
    }

    /**
     * Returns whether it is the Pokemon's move or not.
     */
    public boolean getTurn()
    {
        return isPlayersTurn;
    }

    /**
     * Returns the current Pokemon id.
     */
    public int getCurrentPokemon()
    {
        return currentPokemon;
    }

    /**
     * Adds a certain amount of EXP to the current amount of EXP.
     * @param newXp The amount of EXP to be increased by.
     */
    public void addEXP(int newXp)
    {
        currentExperience += newXp;
    }

    /**
     * Sets the amount of EXP to a certain amount.
     * @param newXP The new amount of EXP.
     */
    public void setEXP(int newXp)
    {
        currentExperience = newXp;
    }

    /**
     * Increase the Pokemon's level by one.
     */
    public void levelUp()
    {
        level++;
    }

    /**
     * Returns one of the Pokemon's moves.
     * @param id Which move to return (the move number).
     */
    public String getMove(int id)
    {
        try
        {
            return currentMovesList[id];
        }
        catch(Exception error)
        {
            return "";
        }
    }
}