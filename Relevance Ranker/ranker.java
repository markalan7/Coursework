/**
* @author Mark Silvis
* CS 1655
* Assignment 1
* Ranker
**/

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class ranker
{
    // preprocess a word
    private static String processWord(HashSet stops, String preword)
    {
        preword = preword.replaceAll("'[a-zA-Z0-9]*", " ").replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase();
        String [] parts = preword.split("\\s+");
        String word = parts[0];

        // check for stopword
        if (!stops.contains(word))
        {
            // stem word
            word = word.replaceAll("^*(ion|ions|ive|ed|ing|ly|s|es)$", "");
            // check again for stopword
            if (!stops.contains(word))
            {
                return word;
            }
        }
        return null;
    }

    // rank the words
    private static void rankWords(HashSet keywords)
    {
        // get inverted.index
        File inverted = null;
        Scanner scan = null;
        try
        {
            inverted = new File("inverted.index");
            scan = new Scanner(inverted);
        }
        catch (IOException ioe)
        {
            System.out.println("Error: inverted.index\nDid you run preproc and indexer?");
            System.exit(-1);
        }

        // find N = total number of documents
        int num_docs = 0;
        // get listing of files in current directory
        File folder = new File(".");
        File [] file_list = folder.listFiles();
        // find all count files
        for (int i = 0; i < file_list.length; i++)
        {
            File file = file_list[i];
            //String filename = file.getName().substring(0, file.getName().length()-7);
            if (file.getName().endsWith(".counts"))
            {
                num_docs++;
            }
        }

        // scan inverted.index, searching for keywords
        Map<String, HashSet<String>> word_indexes = new HashMap<String, HashSet<String>>();
        while (scan.hasNextLine())
        {
            String line = scan.nextLine();
            String [] parts = line.split(",");
            // found keword in inverted.index
            if (keywords.contains(parts[0]))
            {
                // get docs and add to hashmap
                HashSet<String> docs = new HashSet<String>();
                for (int i = 1; i < parts.length; i++)
                {
                    docs.add(parts[i]);
                }
                word_indexes.put(parts[0], docs);
            }

            // break once all keywords are found
            // potentially prevents scanning whole inverted.index, which would be good if it's large
            if (word_indexes.size() == keywords.size())
            {
                break;
            }
        }
        scan.close();

        // word_indexes now holds each word that exists in inverted.index as the key
        // and a HashSet of the documents they are found in as the value
        if (word_indexes.size() == 0)
        {
            System.out.println("No relevant documents");
            System.exit(0);
        }
        else
        {
            Map<String, Double> scores = new HashMap<String, Double>();
            for (String word: word_indexes.keySet())
            {
                String key = word.toString();
                HashSet<String> values = word_indexes.get(key);
                for (String val: values)
                {
                    try
                    {
                        File cfile = new File(val+".counts");
                        Scanner cscan = new Scanner(cfile);
                        while(cscan.hasNextLine())
                        {
                            String line = cscan.nextLine();
                            String [] parts = line.split(",");
                            if (parts[0].equals(key))
                            {
                                Double score = ((1 + log(2, Double.parseDouble(parts[1]))) * (log(2, (double) num_docs/values.size())));
                                if (scores.containsKey(val))
                                {
                                    scores.put(val, scores.get(val)+score);
                                }
                                else
                                {
                                    scores.put(val, score);
                                }
                            }
                        }
                    }
                    catch (IOException ioe)
                    {
                        System.out.println("Error: count file not found\nDid you run preproc and indexer?");
                        System.exit(-1);
                    }
                }
            }
            
            // rank the scores
            Pair [] ranks = new Pair [scores.size()];
            int index = 0;
            for (String x: scores.keySet())
            {
                String key = x.toString();
                Double val = scores.get(x);
                Pair p = new Pair(key, val);
                ranks[index] = p;
                index++;
            }

            // lastly, print the scores
            int rank = 1;
            DecimalFormat df = new DecimalFormat("##0.0##");
            Arrays.sort(ranks);
            for (Pair p: ranks)
            {
                System.out.println(rank + "," + p.getDoc() + "," + df.format(p.getScore()));
                rank++;
            }
        }
    }

    // log function
    private static double log(int base, Double n)
    {
        return Math.log(n)/Math.log(base);
    }

    // document:score pair
    // makes sorting quick and easy
    private static class Pair implements Comparable<Pair>
    {
        private String document;
        private Double score;

        private Pair (String document, Double score)
        {
            this.document = document;
            this.score = score;
        }

        private String getDoc()
        {
            return document;
        }

        private Double getScore()
        {
            return score;
        }

        public int compareTo(Pair pair)
        {
            if (this.score > pair.getScore())
                return -1;
            else if (this.score < pair.getScore())
                return 1;
            return 0;
        }

        public static Comparator<Pair> compareScores = new Comparator<Pair>()
        {
            public int compare(Pair one, Pair two)
            {
                return one.compareTo(two);
            }
        };

    }

    public static void main (String [] args)
    {
        if (args.length == 0)
        {
            System.out.println("Please enter at least one keyword\nUsage: java ranker keyword1 keyword2 keyword3");
            System.exit(0);
        }
        else if (args.length > 3)
        {
            System.out.println("Please enter no more than three keywords\nUsage: java ranker keyword1 keyword2 keyword3");
            System.exit(0);
        }
        else
        {
            // hashset to store stop words
            HashSet<String> stop_words = new HashSet<String>();
            
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

            // process keywords
            // HashSet prevents duplicates
            HashSet<String> keywords = new HashSet<String>();
            for (int i = 0; i < args.length; i++)
            {
                String word = processWord(stop_words, args[i]);
                if (word != null)
                {
                    keywords.add(word);
                }

                // added all keywords
                // potentially prevents scanning entire inverted.index
                if (keywords.size() == args.length)
                {
                    break;
                }
            }

            if (keywords.size() == 0)
            {
                System.out.println("No relevant terms");
                System.exit(0);
            }

            // rank keywords
            rankWords(keywords);

            System.exit(0);
        }
    }
}
