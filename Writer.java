import java.io.*;

/**
 * The Write class, to write to files.
 * 
 * @author Matthew Hillier
 * @version 1.0
 */
public class Writer  
{
    /**
     * Overwrites an old file with a new file.
     * @param newFile The new file to write with.
     * @param oldFile The old file to overwrite.
     */
    public static void overwriteFile(String newFile, String oldFile)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(newFile + ".txt"));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");

            FileWriter writer = new FileWriter(oldFile + ".txt");

            try
            {
                while((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

                writer.write(stringBuilder.toString());
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                reader.close();
                writer.close();
            }
        }
        catch(Exception error)
        {
            System.out.println(error.getMessage());
        }
    }
}