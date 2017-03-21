import greenfoot.*;
import java.awt.Color;

/**
 * The InfoBoxes class, to allow drawing of various text on boxes to be displayed.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class InfoBoxes extends Battle
{
    private GreenfootImage fonts = new GreenfootImage("Fonts.png"); //The font image

    protected final String FONT_POSITIONS[][] = {
            {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"},
            {"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "(", ")", ":", ";", "[", "]"},
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p"},
            {"q", "r", "s", "t", "u", "v", "w", "x", "y", "z", " ", "", "", "", "", ""},
            {"'", "", "", "-", "?", "!", ".", "&", "é", "", "", "", "×", ".", "/", ","},
            {"$", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "♂", "♀", "PK", "MN", ""},
        }; //The positions of each letter or symbol

    private final Color HEALTH = new Color (0, 184, 0); //The colour of the health bar
    private final Color EXPERIENCE = new Color (32, 136, 248); //The colour of the EXP bar

    private String boxName = ""; //The name of the box

    /**
     * Creates a new InfoBox.
     * @param newBoxName The name of the box.
     */
    public InfoBoxes(String newBoxName)
    {
        boxName = newBoxName;
        setImage(boxName + ".png");
    }

    /**
     * Draws text on the box.
     * @param textToDraw The text to be drawn.
     * @param x The X coordinate of the text.
     * @param y The Y coordinate of the text.
     */
    public void drawTextBox(String textToDraw, int x, int y)
    {
        String[] textArr = textToDraw.split(" ");
        int textDrawLength1 = 0;
        int textDrawLength2 = 0;
        int lineLength = 15;
        String line1 = "";
        String line2 = "";

        for(int i = 0; i < textArr.length; i++)
        {
            if(textDrawLength1 + (textArr[i].length()) < lineLength)
            {
                line1 += textArr[i];
                line1 += " ";
                textDrawLength1 += textArr[i].length();
            }
            else if(textDrawLength1 + (textArr[i].length()) >= lineLength)
            {
                textDrawLength1 = lineLength;
                line2 += textArr[i];
                line2 += " ";
                textDrawLength2 += textArr[i].length();
            }
        }

        
        line1 = line1.trim();
        line2 = line2.trim();

        drawWord(line1, x, y);
        drawWord(line2, x, y + 48);
    }

    /**
     * Draws a word.
     * @param word The word to be drawn.
     * @param nameX The X coordinate of the word.
     * @param nameY The Y coordinate of the word.
     */
    public void drawWord(String word, int nameX, int nameY)
    {
        String[] nameLetters = word.split("");

        for(int x = 0; x < nameLetters.length; x++)
        {
            drawLetter(nameLetters[x], nameX + (x * 24), nameY);
        }
    }

    /**
     * Draws a number.
     * @param number The number to be drawn.
     * @param numberX The X coordinate of the number.
     * @param numberY The Y coordinate of the number.
     * @parm direction The direction to draw the number.
     */
    public void drawNumber(int number, int numberX, int numberY, boolean direction)
    {
        String[] numberDraw = (Integer.toString(number)).split("");

        if(direction) //direction = true- left to right, otherwise right to left
        {
            for(int n = 0; n < numberDraw.length; n++)
            {
                drawLetter(numberDraw[n], numberX + (n * 24), numberY);
            }
        }
        else
        {
            int tempNum = 0;
            for(int x = numberDraw.length; x > 0; x--)
            {
                drawLetter(numberDraw[tempNum], numberX + (-x * 24), numberY);
                tempNum++;
            }
        }
    }

    /**
     * Draws a letter.
     * @param letter The letter to be drawn.
     * @param x The X coordinate of the text.
     * @param y The Y coordinate of the text.
     */
    public void drawLetter(String letter, int x, int y)
    {
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 6; j++)
            {
                if(FONT_POSITIONS[j][i].equals(letter))
                {
                    GreenfootImage newImage = new GreenfootImage(24, 24); //Makes a new image for the individual letter
                    newImage.drawImage(fonts, i * -24, j * -24); //Draws the letter onto the new image
                    GreenfootImage currentImage = this.getImage(); //Gets the current image
                    currentImage.drawImage(newImage, x, y); //Draws the letter onto the current image
                    break;
                }
            }
        }
    }

    /**
     * Draws the status bar.
     * @param type The type of bar.
     * @param x The X coordinate of the bar to be drawn.
     * @param y The Y coordinate of the bar to be drawn.
     * @param barLength The length of the bar to be drawn.
     * @param barHeight The height of the bar to be drawn.
     * @param currentNum The current length of the bar to be drawn.
     * @param maxNum The maximum length of the bar to be drawn.
     */
    public void drawStatusBar(String type, int x, int y, int barLength, int barHeight, int currentNum, int maxNum)
    {
        GreenfootImage tempBarImage = new GreenfootImage(barLength, barHeight);

        if(type.equals("Health"))
        {
            tempBarImage.setColor(HEALTH);
        }
        else if(type.equals("Experience"))
        {
            tempBarImage.setColor(EXPERIENCE);
        }
        tempBarImage.fill();

        GreenfootImage barImage = new GreenfootImage(barLength, barHeight);

        Double newDrawPos = (((double) currentNum / (double) maxNum) * barLength); //A percentage of the total length

        if(type.equals("Health"))
        {
            barImage.drawImage(tempBarImage, -barLength + (newDrawPos.intValue()), 0);
            //This will break if currentNum is greater than maxHealth, which should never happen!
        }
        else if(type.equals("Experience"))
        {
            barImage.drawImage(tempBarImage, barLength - (newDrawPos.intValue()), 0); //Draws it backwards
        }
        this.getImage().drawImage(barImage, x, y);

        if(currentNum == 0)
        {
            barImage.clear();
            this.getImage().drawImage(barImage, x, y);
        }
    }

    /**
     * Displays text.
     * @param text The text to be displayed.
     * @param keyToExit Whether or not the user has to press a button to continue from the text.
     * @param toRemove Whether or not the box removes itself after displaying the text.
     */
    public void displayText(String text, boolean keyToExit, boolean toRemove)
    {
        setImage("TextBox.png");
        setLocation(240, 363);

        boolean imageState = true;
        int imageNum = 4;
        int initImageNum = imageNum;
        GreenfootImage cursorDown = new GreenfootImage("Cursor_Black_Down.png");

        if(!text.contains("\n"))
        {
            drawTextBox(text, 30, 27);
            GreenfootImage oldImage = getImage();
            GreenfootImage currentImage = new GreenfootImage(oldImage);
            currentImage.drawImage(cursorDown, 432, 123);

            if(keyToExit)
            {
                while(!"l".equals(Greenfoot.getKey())) //is lowercase, because its checking the ascii character
                {
                    if(imageNum < -initImageNum)
                    {
                        imageState = !imageState;
                        imageNum = initImageNum;
                    }

                    if(imageState)
                    {
                        setImage(currentImage);
                    }
                    else
                    {
                        setImage(oldImage);
                    }

                    imageNum--;
                    Greenfoot.delay(1);
                }
            }
            else
            {
                int timeLeft = 150;
                while(timeLeft > 0)
                {
                    timeLeft--;
                    Greenfoot.delay(1);
                }
            }
        }
        else
        {
            String[] textArr = text.split("\n");

            for(int i = 0; i < textArr.length; i++)
            {
                setImage("TextBox.png");
                drawTextBox(textArr[i], 30, 27);
                GreenfootImage oldImage = getImage();
                GreenfootImage currentImage = new GreenfootImage(oldImage);
                currentImage.drawImage(cursorDown, 432, 123);

                while(!"l".equals(Greenfoot.getKey())) //is lowercase, because its checking the ascii character
                {
                    if(imageNum < -initImageNum)
                    {
                        imageState = !imageState;
                        imageNum = initImageNum;
                    }

                    if(imageState)
                    {
                        setImage(currentImage);
                    }
                    else
                    {
                        setImage(oldImage);
                    }

                    imageNum--;
                    Greenfoot.delay(1);
                }
            }
        }

        if(toRemove)
        {
            getWorld().removeObject(this);
        }
    }

    /**
     * Draws a cursor.
     * @param colour The colour of the cursor.
     * @param x The X coordinate of the cursor.
     * @param y The Y coordinate of the cursor.
     */
    public void drawCursor(String colour,int x, int y)
    {
        this.getImage().drawImage(new GreenfootImage("Cursor_" + colour + ".png"), x, y);
    }

    /**
     * Returns the name of the box.
     */
    public String getBoxName()
    {
        return boxName;
    }
}