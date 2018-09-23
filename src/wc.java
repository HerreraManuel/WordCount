/* Manuel Herrera */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class wc {


    static void err (char errInput) throws IOException {
        throw new IOException("wc: invalid option --" + errInput);
    }

    // No file, prints instructions
    private static void usage() {
        System.out.println("wc will print instructions for how to use wc \n" +
                "wc -l <filename> will print the line count of a file\n" +
                "wc -c <filename> will print the character count\n" +
                "wc -w <filename> will print the word count\n" +
                "wc <filename> will print all the above");
    }

    // Determine user request
    public static void userInput(String args) throws IOException{

        int charPos = 0;
        int lines = 0, words = 0, characters = 0;
        if (args.charAt(0) != '-')
            read(args);
        else {
            while (charPos < args.length()) {
                switch (args.charAt(charPos)) {
                    case 'w':
                        words++;
                        break;
                    case 'c':
                        characters++;
                        break;
                    case 'l':
                        lines++;
                        break;
                    default:

                        break;
                }
                charPos++;
            }
        }
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


    public static void main(String[] args) throws Exception{
        if (args.length < 1)
            usage();
        else {
            userInput(args[0]);
        }

    }
}