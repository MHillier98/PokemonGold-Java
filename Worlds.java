import greenfoot.*;
import java.awt.Color;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * The Worlds superclass.
 * This holds methods used by all worlds.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class Worlds extends World
{
    private GreenfootSound currentMusic = new GreenfootSound("Route_30.mp3"); //The default music

    /**
     * The world constructor for all worlds in the game.
     */
    public Worlds()
    {
        super(480, 432, 1, false);
    }

    /**
     * 'Blacks out' the world- aka putting an overlay over the world.
     * @param type The type of blackout to happen.
     */
    public void blackout(String type)
    {
        addObject(new Blackout(type), getWidth() / 2, getHeight() / 2);
    }

    /**
     * Plays a song on loop.
     * @param songName The name of the new song.
     */
    public void newMusic(String songName)
    {
        currentMusic.stop();
        currentMusic = new GreenfootSound(songName + ".mp3");
        currentMusic.playLoop();
    }

    /**
     * Stops the currently playing music.
     */
    public void stopMusic()
    {
        currentMusic.stop();
    }

    /**
     * Removes all objects currently in the world.
     */
    public void removeAllObjects()
    {
        List<Actor> actors = getObjects(null);
        removeObjects(actors);
    }

    /**
     * Saves the data of a Pokemon.
     * @param newPokemon The pokemon object to be passed in.
     * @param currentPokemon The current Pokemon counter.
     */
    public void savePokemonData(Pokemon newPokemon, int currentPokemon)
    {
        try
        {
            FileWriter fw = new FileWriter("tempPlayerPokemon.txt");

            List<String> list = Files.readAllLines(Paths.get("playerPokemon.txt"), StandardCharsets.UTF_8);
            String[] pokemonList = list.toArray(new String[list.size()]);

            String currentPokemonStr = pokemonList[currentPokemon];
            String[] currentPokemonArray = currentPokemonStr.split("\\s*,\\s*");

            currentPokemonArray[0] = newPokemon.getName();
            currentPokemonArray[1] = Integer.toString(newPokemon.getLevel());
            currentPokemonArray[3] = Integer.toString(newPokemon.getCurrentHealth());
            currentPokemonArray[4] = Integer.toString(newPokemon.getCurrentExperience());
            for(int c = 0; c < 4; c++)
            {
                currentPokemonArray[5 + c] = newPokemon.getMove(c);
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
     * Sets the background colour to the off-white colour of the menus.
     * @param num The RGB colour value.
     */
    public void setBackground(int num)
    {
        GreenfootImage background = new GreenfootImage(480, 432);
        background.setColor(new Color(num, num, num));
        background.fill();
        setBackground(background);
    }

    /**
     * Shows a string of text in the world.
     * @param showText The text to display.
     */
    public void showText(String showText)
    {
        Greenfoot.setSpeed(50);//so that the flashing arrow displays at the correct speed
        InfoBoxes infoBox = new InfoBoxes("TextBox");
        addObject(infoBox, 240, 363);
        infoBox.displayText(showText, true, true);
        Greenfoot.setSpeed(30);//so the game goes back to its default speed
    }
}