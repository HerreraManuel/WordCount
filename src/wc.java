/* Manuel Herrera */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class wc {

    // No file, prints instructions
    private static void usage(){
        System.out.println("wc will print instructions for how to use wc \n" +
                "wc -l <filename> will print the line count of a file\n" +
                "wc -c <filename> will print the character count\n" +
                "wc -w <filename> will print the word count\n" +
                "wc <filename> will print all the above");
    }
    // Read the file. Includes lineCount, wordCount, characterCount
    private static void read(String fileName) {
        int lineCounter = 0, wordCounter = 0, characterCount =0;
        File currentFile = new File(fileName);
        try {
            String line;
            String[] lineHolder;
            BufferedReader reader= new BufferedReader (new FileReader(fileName));
            while((line = reader.readLine()) != null) {
                lineHolder = line.split("\\s");
                if (lineHolder[0].equals("") && lineHolder.length > 1)
                wordCounter += lineHolder.length;
                lineCounter++;
            }
            System.out.println(lineCounter);
            System.out.println(wordCounter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // wc -l prints line count of the file
    private static int lineCount(String fileName) {
        int lineCounter = 0;
        // insert directory name to the File object
        File currentFile = new File(fileName);
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(currentFile));
            while ((line = reader.readLine()) != null) lineCounter++;
            return lineCounter;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return 0;
    }
    private static int wordCount(String fileName){
        int wordCounter = 0;
        File currentFile = new File(fileName);
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(currentFile));
            while ((line = reader.readLine()) != null) {
                String[] holder = line.split("\\s");
                    wordCounter += holder.length;
            }
            return wordCounter;
        } catch (IOException e) {
            e.printStackTrace();
        }        return 0;
    }

    public static void main(String[] args) {
        // Checking to see if any file was inserted.
        read("C:\\Users\\mherr\\Desktop\\Java HW\\WordCount\\src\\random.py");
        /*if (args.length > 0) {
            //System.out.println(lineCount(args[0]));
            System.out.println(wordCount("C:\\Users\\mherr\\Desktop\\Java HW\\WordCount\\src\\random.py"));
        }
        else {
            usage();
        }*/
    }
}
