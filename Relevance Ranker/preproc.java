/**
* @author Mark Silvis
* CS 1655
* Assignment 1
* Preprocessor
**/

import java.io.*;
import java.util.*;

public class preproc
{
    // hashset to store stop words
    // used to check if processed word is a stop word
    private static Set<String> stop_words = new HashSet<String>();

    // hashmap to quickly store words and their counts
    private static Map<String, Integer> word_counts = new HashMap<String, Integer>();

    /* process entire line from a file */
    private static void processLine(String line)
    {
        // remove punctuation and special characters
        line = line.replaceAll("'[a-zA-Z0-9]*", " ").replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase();
        String [] words = line.split("\\s+");

        // process words
        for (String word: words)
        {
            // skip stop words
            if (!stop_words.contains(word))
            {
                // stem word
                word = word.replaceAll("^*(ion|ions|ive|ed|ing|ly|s|es)$", "");
                // if the stemmed word isn't a stop word, add it
                if (!stop_words.contains(word))
                {
                    // already exists, update count
                    if (word_counts.containsKey(word))
                    {
                        word_counts.put(word, word_counts.get(word)+1);
                    }
                    // does not exist, add to hashmap
                    else
                    {
                        word_counts.put(word, 1);
                    }    
                }
            }
        }
    }

    public static void main (String [] args)
    {
        if (args.length != 1)
        {
            System.out.println("Usage: java preproc filename");
            System.exit(-1);
        }
        // scan in stop words
        File stop_file = null;
        Scanner stop_scanner = null;
        try
        {
            stop_file = new File("hw1.stopwords.txt");
            stop_scanner = new Scanner(stop_file);
        }
        catch (IOException e)
        {
            System.out.println("Error: could not process stop words");
            System.exit(-1);
        }

        while(stop_scanner.hasNext())
        {
            String word = stop_scanner.next();
            stop_words.add(word);
        }
        stop_scanner.close();


        File file = null;
        Scanner scan = null;

        // find file and add to scanner
        try
        {
            file = new File(args[0]);
            scan = new Scanner(file);
        }
        catch (IOException e)
        {
            System.out.println("Error processing file");
            System.exit(-1);
        }

        // file exists and scanner is set to read from it
        // scan each line and process
        while(scan.hasNextLine())
        {
            processLine(scan.nextLine());   
        }
        scan.close();

        // make ".counts" file and write to it
        File counts_file = null;
        PrintWriter writer = null;
        try
        {
            counts_file = new File(file.toString().replaceAll("\\.[a-zA-Z0-9]*", "") + ".counts");
            writer = new PrintWriter(counts_file);
        }
        catch (FileNotFoundException fnfe)
        {
            try
            {
                counts_file.createNewFile();
                writer = new PrintWriter(counts_file); 
            }
            catch (IOException ioe)
            {
                System.out.println("Fatal error: could not create counts file");
                System.exit(-1);
            }
        }

        // write counts to file
        for (String name: word_counts.keySet())
        {
            String key = name.toString();
            String value = word_counts.get(name).toString();
            writer.println(key + "," + value.toString());
        }
        writer.close();

        System.exit(0);
    }
}
