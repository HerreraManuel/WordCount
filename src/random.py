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

    // wc -l prints line count of the file
    private static int lineCount(String fileName) {
        int lineCounter = 0;
        File currentFile = new File(fileName);
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(currentFile));
            while ((line = reader.readLine()) != null) lineCounter++;
            return lineCounter;
            }catch (IOException e){
                e.printStackTrace();
            }
        return 1;
    }

    public static void main(String[] args) {
            System.out.println(lineCount("C:\\Users\\mherr\\Desktop\\Java HW\\WordCount\\src\\random.c"));

    }
}