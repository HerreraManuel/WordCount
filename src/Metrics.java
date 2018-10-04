/* Manuel Herrera
 * Fall 2018
 * Sprint 2*/

/*
Counting and file reading methods obtained from:
www.vogella.com/tutorials/JavaIO/article.html
https://stackoverflow.com/questions/4094119/counting-number-of-words-in-a-file
https://stackoverflow.com/questions/16802147/java-i-want-to-read-a-file-name-from-command-line-then-use-a-bufferedreader-to

Counting comments implementation by"
https://gist.github.com/shiva27/1432290
 */


import picocli.CommandLine.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


@Command(name = "Metrics", footer = "\nCSC131: Individual Project - Sprint 2. Design document available.", description =
        "If no option is declared, prints lines, words, character, comment lines, source line counts for each" +
                "FILE, and a total line if more than one FILE is specified. Otherwise, prints specified request for FILE(s).\n" +
                "\"Source lines of code\" is determined on the [author & journal].\n", sortOptions = false)

class CodeReader{
    public void readLines(BufferedReader reader) throws IOException {
        int count = 0;
        boolean commentStart = false;
        String currentLine = null;
        while ((currentLine = reader.readLine()) != null) {
            currentLine = currentLine.trim();
        }
    }

    public boolean commentStart(String line){
        int index = line.indexOf("/*");
        if (index < 0) return false;
        int startIndex = line.indexOf("\"");
        return true;
    }
}

public class Metrics {
    public long numLines;
    public long numWords;
    public long numChars;
    public long numCmts;
    public long numSrcLns;
    public String fileExt;

    public Metrics() {}

    @Parameters(paramLabel = "Stat Requested", description = "Contents to display") List<File> files;
    @Option(names = {"-l", "--lines"}, description = "Print the number of lines") boolean lineStat;
    @Option(names = {"-w", "--words"}, description = "Print the number of words") boolean wordStat;
    @Option(names = {"-c", "--characters"}, description = "Print the number of characters") boolean charStat;
    @Option(names = {"-s", "--sourceLines"}, description = "Print the number of source lines") boolean srcLnStat;
    @Option(names = {"-C", "--commentlines"}, description = "Print the number of comment lines") boolean cmtStat;
    @Option(names = {"-h", "--help"}, usageHelp = true,  description = "Display this help and exit") boolean help;


    public void run(List<File> inFiles) throws Exception {
        try {
            if (srcLnStat || cmtStat) printHeader();
            for (File temp : inFiles) {
                lineCount(temp);
                wordAndCharCount(temp);
                getExtension(temp);
                printStats(temp);
                numLines = numWords = numChars = numSrcLns = numCmts = 0;
            }

            System.out.println();
            System.out.println("Line Count = " + numLines);
            System.out.println("Word Count = " + numWords);
            System.out.println("Char Count = " + numChars);
            System.out.println("File Ext = " + fileExt);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lineCount(File currFile) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(currFile));
        numLines = reader.lines().count();
        //while(reader.readLine() != null) numLines++;
    }

    public void wordAndCharCount(File currFile) throws Exception{
        BufferedReader wordReader = new BufferedReader(new FileReader(currFile));
        String lineHolder;
        while ((lineHolder = wordReader.readLine()) != null){
            numChars += lineHolder.length();
            if (!lineHolder.isEmpty()){
                String[] lineOfWords = lineHolder.trim().split("\\s+");
                numWords += lineOfWords.length;
            }
        }
    }

    public void getExtension(File tempFile){fileExt = tempFile.getName().substring(tempFile.getName().indexOf("."));}

    public void printHeader(){
            if (lineStat) System.out.printf("%-10s", "Lines");
            if (wordStat) System.out.printf("%-10s", "Words");
            if (charStat) System.out.printf("%-10s", "Chars");
            if (cmtStat) System.out.printf("%-10s", "Cmmts");
            if (srcLnStat) System.out.printf("%-10s", "SrcLines");
            System.out.printf("%8s", "File\n");
    }

    public void printStats(File temp){
        if (!lineStat && !wordStat && !charStat && !cmtStat && !srcLnStat)
            System.out.printf("%-5d %-5d %-8d %10s\n", numLines, numWords, numChars, temp.getName());
        else {
            if (lineStat) System.out.printf("%-10s", numLines);
            if (wordStat) System.out.printf("%-10s", numWords);
            if (charStat) System.out.printf("%-10s", numChars);
            if (cmtStat) System.out.printf("%-10s", numCmts);
            if (srcLnStat) System.out.printf("%-10s", numSrcLns);
            System.out.printf("%8s\n", temp.getName());
        }
    }

    //Currently set for simple test
    public static void main(String[] args){
        if (args.length < 1) picocli.CommandLine.usage(new Metrics(), System.out);
        else {
            //Metrics m = new Metrics();
            try {

                Metrics m = picocli.CommandLine.populateCommand(new Metrics(), args);
                if (m.help) {
                    picocli.CommandLine.usage(new Metrics(), System.out);}
                //if (m.lineStat) { m.run(m.files);}
                m.run(m.files);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}