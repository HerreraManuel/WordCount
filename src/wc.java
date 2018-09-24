/* Manuel Herrera
 * Fall 2018 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class wc {
    private int totalLines;
    private int totalWords;
    private int totalChars;

    private wc() {
        totalLines = 0;
        totalWords = 0;
        totalChars = 0;
    }

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

    // Read the file(s).
    public void read(String[] args, int lines, int words, int characters) throws IOException {
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
                    lines = 1;
                    words = 1;
                    characters = 1;
                }
                else {
                    if (lines == 1) System.out.print(lineCount(lineReader) + " ");
                    if (words == 1) System.out.print(wordCount(wordReader) + " ");
                    if (characters == 1) System.out.print(charCount(charReader) + " ");
                    System.out.print(onFile.getName() + "\n");
                }
             }
            printTotals(lines, words, characters);
        } catch (IOException e) {
                 e.printStackTrace();
            }
}

    public int lineCount (BufferedReader singleFile) throws IOException{
        String line;
        int numOfLines = 0;
        while((line = singleFile.readLine()) != null ){
            numOfLines++;
        }
        totalLines += numOfLines;
        return numOfLines;
    }

    public int wordCount (BufferedReader singleFile) throws IOException{
        int numOfWords = 0;
        String line;
        String[] lineHolder;
        while ((line = singleFile.readLine()) != null) {
            lineHolder = line.replaceAll("\\s+", " ").split(" ");
            for (String singleWord : lineHolder)
                if (!singleWord.isEmpty()) numOfWords++;
        }
        totalWords += numOfWords;
        return numOfWords;
    }

    public int charCount (BufferedReader singleFile) throws IOException {
        int numOfChar = 0;
        String line;
        String[] lineHolder;
        while((line = singleFile.readLine()) != null) {
            lineHolder = line.replaceAll("\\s+", " ").split(" ");
            for (String singleWord: lineHolder) {
                numOfChar += singleWord.length();
            }
        }
        totalChars += numOfChar;
        return numOfChar;
    }

    private void printTotals(int lines, int words, int characters) {
        String finalLine = new String();
        if (lines == 1) finalLine += totalLines + " ";
        if (words == 1) finalLine += totalWords + " ";
        if (characters == 1) finalLine += totalChars + " ";
        System.out.println(finalLine + " total");
    }


    public static void main(String[] args) throws IOException{
        if (args.length < 1)
            usage();
        else {
            int charPos = 1;
            int lines = 0, words = 0, characters = 0;
            wc filesToBeRead = new wc();
            if (args[0].charAt(0) != '-') filesToBeRead.read(args, lines, words, characters);
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
                        default :
                            err (args[0].charAt(1));
                            System.exit(0);
                    }
                    charPos++;
                }
                // send triggers to retrieve correct stats.
                filesToBeRead.read(args, lines, words, characters);
            }
        }

    }
}