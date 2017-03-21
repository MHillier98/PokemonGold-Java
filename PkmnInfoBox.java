import greenfoot.*;

/**
 * The PkmnInfoBox class, to display Pokemon's stats while in a battle.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class PkmnInfoBox extends InfoBoxes
{
    private boolean canUpdate = true; //If the box can update or not

    /**
     * Creates a new PkmnInfoBox.
     * @param newBoxName The name of the box.
     */
    public PkmnInfoBox(String newBoxName)
    {
        super(newBoxName);
    }

    /**
     * The act method of the box.
     * 
     * Updates the image when possible.
     */
    public void act()
    {
        if(canUpdate)
        {
            drawAll();
            canUpdate = false;
        }
    }

    /**
     * Draws the information.
     */
    public void drawAll()
    {
        try
        {
            getImage().clear();
            setImage(getBoxName() + ".png");

            if(getBoxName().equals("PlayerPkmnInfo"))
            {
                BattleWorld battleWorld = (BattleWorld) getWorld();
                Pokemon playerPokemon = battleWorld.getPlayerPokemon();

                drawWord(playerPokemon.getName().toUpperCase(), 30, 0);
                int pokemonLevel = playerPokemon.getLevel();
                drawStatsPkmnInfo(pokemonLevel, playerPokemon.getGender());

                int currentHp = playerPokemon.getCurrentHealth();
                int maxHp = playerPokemon.getMaxHealth();
                if(currentHp > maxHp) 
                {
                    currentHp = maxHp;
                }
                drawHpPkmnInfo(currentHp, maxHp);

                drawStatusBar("Experience", 30, 105, 192, 6, playerPokemon.getCurrentExperience(), StatCalculator.experienceToNextLevel(pokemonLevel));
            }
            else if(getBoxName().equals("EnemyPkmnInfo"))
            {
                BattleWorld battleWorld = (BattleWorld) getWorld();
                Pokemon enemyPokemon = battleWorld.getEnemyPokemon();

                drawWord(enemyPokemon.getName().toUpperCase(), 6, 0);

                int currentHp = enemyPokemon.getCurrentHealth();
                int maxHp = enemyPokemon.getMaxHealth();
                if(currentHp > maxHp) 
                {
                    currentHp = maxHp;
                }
                drawStatsPkmnInfo(enemyPokemon.getLevel(), enemyPokemon.getGender());
                drawHpPkmnInfo(currentHp, maxHp);
            }
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
    }

    /**
     * Draws the Pokemon's level and gender.
     * @param level The Pokemon's level.
     * @param gender The Pokemon's gender.
     */
    public void drawStatsPkmnInfo(int level, boolean gender)
    {    
        drawNumber(level, 126, 21, true);

        if(gender == true)//Is male
        {
            drawLetter("♂", 198, 24);
        }
        else
        {
            drawLetter("♀", 198, 24);
        }
    }

    /**
     * Draws the Pokemon's health.
     * @param currentHp The Pokemon's current health.
     * @param maxHp The Pokemon's max health.
     */
    public void drawHpPkmnInfo(int currentHp, int maxHp)
    {
        if(getBoxName().equals("PlayerPkmnInfo"))
        {
            drawNumber(currentHp, 123, 69, false);
            drawNumber(maxHp, 219, 69, false);
        }
        drawStatusBar("Health", 78, 57, 144, 6, currentHp, maxHp);
    }
    
    /**
     * Toggles whether or not the box can update.
     */
    public void toggleCanUpdate()
    {
        canUpdate = !canUpdate;
    }
}