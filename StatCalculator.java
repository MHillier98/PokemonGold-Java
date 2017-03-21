/**
 * The StatCalculator class to calculate Pokemon stats.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class StatCalculator  
{
    /**
     * Calculates the max health of a Pokemon.
     * @param name The name of the Pokemon.
     * @param level The level of the Pokemon.
     */
    public static int maxHealthCalc(String name, int level)
    {
        int health = 0;
        int baseHealth = 0;

        for(int i = 0; i < Reader.getFileLength("allPokemonBaseStats"); i++)
        {
            if((name.toUpperCase()).equals(Reader.readStringFromFile("allPokemonBaseStats", i, 0)))
            {
                baseHealth = Reader.readIntFromFile("allPokemonBaseStats", i, 1);
                break;
            }
        }

        baseHealth *= 2;
        baseHealth *= level;
        health = baseHealth / 100;
        health += level;
        health += 12;

        return health;
    }

    /**
     * Calculates the stats of a Pokemon.
     * @param name The name of the Pokemon.
     * @param level The level of the Pokemon.
     */
    public static int[] generalStatsCalc(String name, int level)
    {
        int statsReturn[] = new int[5];

        read:
        for(int i = 0; i < Reader.getFileLength("allPokemonBaseStats"); i++)
        {
            if((name.toUpperCase()).equals(Reader.readStringFromFile("allPokemonBaseStats", i, 0)))
            {
                for(int s = 2; s < 7; s++)
                {
                    int statNum = s - 2;
                    statsReturn[statNum] = Reader.readIntFromFile("allPokemonBaseStats", i, s);
                    statsReturn[statNum] *= 2;
                    statsReturn[statNum] *= level;
                    statsReturn[statNum] /= 100;
                    statsReturn[statNum] += 6;
                }
                break read;
            }
        }

        return statsReturn;
    }

    /**
     * Determines the type of a Pokemon.
     * @param name The name of the Pokemon.
     */
    public static String[] determineType(String name)
    {
        String[] returnType = new String[2];
        int pokemonID = Reader.getPokemonID(name);

        returnType[0] = Reader.readStringFromFile("allPokemonList", pokemonID, 1);
        returnType[1] = Reader.readStringFromFile("allPokemonList", pokemonID, 2);

        return returnType;
    }

    /**
     * Calculates how much EXP a Pokemon should gain.
     * @param name The name of the Pokemon.
     * @param level The level of the Pokemon.
     */
    public static int experienceGainCalc(String name, int level)
    {
        int expReturn = 0;
        int baseExp = 0;

        read:
        for(int i = 0; i < Reader.getFileLength("allPokemonBaseStats"); i++)
        {
            if((name.toUpperCase()).equals(Reader.readStringFromFile("allPokemonBaseStats", i, 0)))
            {
                baseExp = Reader.readIntFromFile("allPokemonBaseStats", i, 7);
                break read;
            }
        }

        double expCalc = 0;
        expCalc = (baseExp * level) * 1.5;
        expReturn = (int) expCalc / 8;

        return expReturn;
    }

    /**
     * Calculates how much EXP is needed to get to the next level.
     * @param The current level of the Pokemon.
     */
    public static int experienceToNextLevel(int level)
    {
        int xpReturn = 0;

        double xpCalc = 4.0;
        xpCalc *= Math.pow(level, 3);
        xpCalc /= 5.0;

        xpReturn = (int) xpCalc;

        return xpReturn;
    }
}