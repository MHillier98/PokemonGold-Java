import greenfoot.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;

/**
 * The Reader class, to read data in from '.txt' files.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class Reader
{
    /**
     * Returns a string read from a file.
     * @param fileName The name of the file to read from.
     * @param lineNum The line of the file to read from.
     * @param lineIndex The index of the line to read from.
     */
    public static String readStringFromFile(String fileName, int lineNum, int lineIndex)
    {
        String toReturn = "";
        try
        {
            List<String> list = Files.readAllLines(Paths.get(fileName + ".txt"), StandardCharsets.UTF_8);
            String[] pokemonListBattle = list.toArray(new String[list.size()]);

            String currentPokemon = pokemonListBattle[lineNum];
            String[] currentPokemonArray = currentPokemon.split("\\s*,\\s*");

            toReturn = currentPokemonArray[lineIndex];
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
        return toReturn;
    }

    /**
     * Returns an integer read from a file.
     * @param fileName The name of the file to read from.
     * @param lineNum The line of the file to read from.
     * @param lineIndex The index of the line to read from.
     */
    public static int readIntFromFile(String fileName, int lineNum, int lineIndex)
    {
        int toReturn = 0;
        try
        {
            List<String> list = Files.readAllLines(Paths.get(fileName + ".txt"), StandardCharsets.UTF_8);
            String[] pokemonListBattle = list.toArray(new String[list.size()]);

            String currentPokemon = pokemonListBattle[lineNum];
            String[] currentPokemonArray = currentPokemon.split("\\s*,\\s*");

            toReturn = Integer.parseInt(currentPokemonArray[lineIndex]);
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
        return toReturn;
    }

    /**
     * Returns a double read from a file.
     * @param fileName The name of the file to read from.
     * @param lineNum The line of the file to read from.
     * @param lineIndex The index of the line to read from.
     */
    public static double readDoubleFromFile(String fileName, int lineNum, int lineIndex)
    {
        double toReturn = 0;
        try
        {
            List<String> list = Files.readAllLines(Paths.get(fileName + ".txt"), StandardCharsets.UTF_8);
            String[] pokemonListBattle = list.toArray(new String[list.size()]);

            String currentPokemon = pokemonListBattle[lineNum];
            String[] currentPokemonArray = currentPokemon.split("\\s*,\\s*");

            toReturn = Double.parseDouble(currentPokemonArray[lineIndex]);
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
        return toReturn;
    }

    /**
     * Returns a boolean read from a file.
     * @param fileName The name of the file to read from.
     * @param lineNum The line of the file to read from.
     * @param lineIndex The index of the line to read from.
     */
    public static boolean readBooleanFromFile(String fileName, int lineNum, int lineIndex)
    {
        boolean toReturn = false;
        try
        {
            List<String> list = Files.readAllLines(Paths.get(fileName + ".txt"), StandardCharsets.UTF_8);
            String[] pokemonListBattle = list.toArray(new String[list.size()]);

            String currentPokemon = pokemonListBattle[lineNum];
            String[] currentPokemonArray = currentPokemon.split("\\s*,\\s*");

            toReturn = Boolean.parseBoolean(currentPokemonArray[lineIndex]);
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
        return toReturn;
    }

    /**
     * Returns the length of a file.
     * @param fileName The name of the file.
     */
    public static int getFileLength(String fileName)
    {
        int toReturn = 0;
        try
        {
            List<String> list = Files.readAllLines(Paths.get(fileName + ".txt"), StandardCharsets.UTF_8);
            String[] pokemonList = list.toArray(new String[list.size()]);

            toReturn = pokemonList.length;
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
        return toReturn;
    }

    /**
     * Returns the ID of a Pokemon.
     * @param pokemonName The name of the Pokemon.
     */
    public static int getPokemonID(String pokemonName)
    {
        int total = 1;
        try
        {
            List<String> list = Files.readAllLines(Paths.get("allPokemonList.txt"), StandardCharsets.UTF_8);
            String[] allPokemonList = list.toArray(new String[list.size()]);

            idLoop:
            for(int i = 0; i < allPokemonList.length; i++)//Have to +2 becuase whitespace is added in
            {
                if(allPokemonList[i].startsWith(pokemonName + ","))
                {
                    break idLoop;
                }
                else
                {
                    total++;
                }
            }
        }
        catch(IOException error)
        {
            System.out.println(error.getMessage());
        }

        if(total > 256)
        {
            return -1;
        }
        else
        {
            return total;
        }
    }

    /**
     * Returns the directory of the game.
     */
    public String getDirectory()
    {
        return System.getProperty("user.dir");
    }
}