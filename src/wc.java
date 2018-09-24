/* Manuel Herrera
 * Fall 2018 */

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



    // Read the file. Includes lineCount, wordCount, characterCount
    private static void read(String[] args, int lines, int words, int characters) throws IOException {
        String[] filePaths = new String[args.length - 1];
        for (int i = 0; i < args.length - 1; i++)
            filePaths[i] = args[i + 1];
        try {
            for (String currentFile : filePaths) {
                BufferedReader lineReader = new BufferedReader(new FileReader(currentFile));
                BufferedReader wordReader = new BufferedReader(new FileReader(currentFile));
                BufferedReader charReader = new BufferedReader(new FileReader(currentFile));
                File onFile = new File(currentFile);
                if (lines == 0 && words == 0 && characters == 0){
                    System.out.println(lineCount(lineReader) + " " + wordCount(wordReader)
                            + " " + charCount(charReader) + " " + onFile.getName());
                }
                if (lines == 1) {
                    System.out.println(lineCount(lineReader) + " " + onFile.getName());
                }
                if (words == 1) {
                    System.out.println(wordCount(lineReader) + " " + onFile.getName());
                }
                if (characters == 1) {
                    System.out.println(charCount(lineReader) + " " + onFile.getName());
                }
             }
        } catch (IOException e) {
                 e.printStackTrace();
            }
}

    public static int lineCount (BufferedReader singleFile) throws IOException{
        String line;
        int numOfLines = 0;
        while((line = singleFile.readLine()) != null ){
            numOfLines++;
        }
        return numOfLines;
    }

    public static int wordCount (BufferedReader singleFile) throws IOException{
        int numOfWords = 0;
        String line;
        String[] lineHolder;
        while ((line = singleFile.readLine()) != null) {
            lineHolder = line.replaceAll("\\s+", " ").split(" ");
            for (String singleWord : lineHolder)
                if (!singleWord.isEmpty()) numOfWords++;
        }
        return numOfWords;
    }

    public static int charCount (BufferedReader singleFile) throws IOException {
        int numOfChar = 0;
        String line;
        String[] lineHolder;
        while((line = singleFile.readLine()) != null) {
            lineHolder = line.replaceAll("\\s+", " ").split(" ");
            for (String singleWord: lineHolder) {
                numOfChar += singleWord.length();
            }
        }
        return numOfChar;
    }


    public static void main(String[] args) throws Exception{
        if (args.length < 1)
            usage();
        else {
            int charPos = 0;
            int lines = 0, words = 0, characters = 0;
            if (args[0].charAt(0) != '-')
                read(args, lines, words, characters);
            else {
                while (charPos < args[0].length()) {
                    switch (args[0].charAt(charPos)) {
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
                read(args, lines, words, characters);
            }
        }

    }
}