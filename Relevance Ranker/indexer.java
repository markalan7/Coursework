/**
* @author Mark Silvis
* CS 1655
* Assignment 1
* Indexer
**/

import java.io.*;
import java.util.*;

public class indexer
{
    public static void main (String [] args)
    {
        // hashmap to store words and their indexes
        Map<String, ArrayList<String>> indexes = new HashMap<String, ArrayList<String>>();

        // get listing of files in current directory
        File folder = new File(".");
        File [] file_list = folder.listFiles();

        // find all count files
        for (int i = 0; i < file_list.length; i++)
        {
            File file = file_list[i];
            if (file.getName().endsWith(".counts"))
            {
                String filename = file.getName().substring(0, file.getName().length()-7);
                try
                {
                    // scan file for words
                    Scanner scan = new Scanner(file);
                    while (scan.hasNextLine())
                    {
                        String line = scan.nextLine();
                        String [] parts = line.split(",");
                        String word = parts[0];

                        ArrayList<String> word_index = null;
                        // word exists in index
                        // add to current list of files
                        if (indexes.containsKey(word))
                        {
                            word_index = indexes.get(word);
                        }
                        // word does not exist in index
                        // new list of files
                        else
                        {
                            word_index = new ArrayList<String>();
                        }
                        word_index.add(filename);
                        indexes.put(word, word_index);
                    }
                    scan.close();
                }
                catch (IOException ioe)
                {
                    System.out.println("Error scanning counts file");
                    System.exit(-1);
                }
            }
        }

        File index_file = new File("inverted.index");
        PrintWriter writer = null;
        try
        {
            writer = new PrintWriter(index_file);
        }
        catch (FileNotFoundException fnfe)
        {
            try
            {
                index_file.createNewFile();
                writer = new PrintWriter(index_file); 
            }
            catch (IOException ioe)
            {
                System.out.println("Could not create index file");
                System.exit(-1);
            }
        }

        for (String word: indexes.keySet())
        {
            String key = word.toString();
            ArrayList<String> values = indexes.get(word);
            writer.write(key);
            for (String value: values)
            {
                writer.print("," + value);
            }
            writer.println();
        }
        writer.close();

        System.exit(0);
    }
}