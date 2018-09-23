/* Manuel Herrera */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class wc {

    // No file, prints instructions
    private static void usage() {
        System.out.println("wc will print instructions for how to use wc \n" +
                "wc -l <filename> will print the line count of a file\n" +
                "wc -c <filename> will print the character count\n" +
                "wc -w <filename> will print the word count\n" +
                "wc <filename> will print all the above");
    }

    // Determine user request
    public static void userInput() {

    }

    // Read the file. Includes lineCount, wordCount, characterCount
    private static void read(String fileName) {
        int lineCounter = 0, wordCounter = 0, characterCount = 0;
        File currentFile = new File(fileName);
        try {
            String line;
            String[] lineHolder;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                lineHolder = line.replaceAll("\\s+", " ").split(" ");
                for (String singleWord : lineHolder) {
                    characterCount += singleWord.length();
                    if (!singleWord.isEmpty())
                        wordCounter++;
                }
                lineCounter++;
            }
            System.out.println(lineCounter);
            System.out.println(wordCounter);
            System.out.println(characterCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        if (args.length <= 0)
            usage();
        else {
            String statsRequested = args[0];
            switch (statsRequested) {
                case "-l":
                    System.out.println("line count");
                    break;
                case "-c":
                    System.out.println("character count");
                    break;
                case "-w":
                    System.out.println("word count");

            }

            // Checking to see if any file was inserted.
           // read("C:\\Users\\mherr\\Desktop\\Java HW\\WordCount\\src\\random.txt");
        /*if (args.length > 0) {
            //System.out.println(lineCount(args[0]));
            System.out.println(wordCount("C:\\Users\\mherr\\Desktop\\Java HW\\WordCount\\src\\random.py"));
        }
        else {
            usage();
        }*/
        }
    }
}